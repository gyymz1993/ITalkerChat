package net.qiujuer.italker.common.tools;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.util.Log;

import net.qiujuer.italker.common.app.Application;
import net.qiujuer.lame.Lame;
import net.qiujuer.lame.LameAsyncEncoder;
import net.qiujuer.lame.LameOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 录制语音的工具类，该工具类可以实现录制语音的工作
 * 录制语音我们采取AudioRecord进行录制，录制的语音为高保真语音
 * 所以我们需要对这个语音进行一定的转码操作，在这里为了通用转码为mp3格式
 * <p>
 * 而要把原始声音转码为mp3我们采用的是：Lame进行转码：Lame的官网：http://lame.sourceforge.net/
 * Lame是一个比较老牌的转码工具，口碑非常不错是我选中他的原因。
 * 当然Lame本身是c语言的实现，并不是java；所以我写了一个框架来辅助调用Lame进行转码操作
 * Lame 的java封装基本上满足了我们的现有需求，可以实现一边录制一边进行转码操作
 * 如果有对这一块感兴趣的同学可以加群，或者联系我QQ：756069544
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class AudioRecordHelper {
    // 打印日志使用
    private static final String TAG = AudioRecordHelper.class.getSimpleName();
    // 采样频率集合，用于适应不同手机情况
    private static final int[] SAMPLE_RATES = new int[]{44100, 22050, 11025, 8000};
    // 状态回调
    private RecordCallback callback;
    // 缓存文件，无论那一个录音都复用同一个缓存文件
    private File tmpFile;
    // 进行初始化时需要的buffer大小, 通过AudioRecord.getMinBufferSize运算得到
    // AudioRecord.getMinBufferSize得到的是bytes的大小；而我们读取的时候是short
    // 所以需要该值为AudioRecord.getMinBufferSize/2
    private int minShortBufferSize;
    // 录制完成
    private boolean isDone;
    // 是否取消
    private boolean isCancel;

    /**
     * 构造函数
     *
     * @param tmpFile  缓存文件
     * @param callback 录制的状态回调
     */
    public AudioRecordHelper(File tmpFile, RecordCallback callback) {
        this.tmpFile = tmpFile;
        this.callback = callback;
    }

    /**
     * 初始化一个录音器
     *
     * @return 返回一个录音器
     */
    private AudioRecord initAudioRecord() {
        // 遍历采样频率
        for (int rate : SAMPLE_RATES) {
            // 编码比特率
            for (short audioFormat : new short[]{AudioFormat.ENCODING_PCM_16BIT, AudioFormat.ENCODING_PCM_8BIT}) {
                // 录音通道：双通道，单通道
                for (short channelConfig : new short[]{AudioFormat.CHANNEL_IN_STEREO, AudioFormat.CHANNEL_IN_MONO}) {
                    try {
                        Log.d(TAG, "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                                + channelConfig);
                        // 尝试获取最小的缓存区间大小
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // 如果初始化成功
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);
                            // 尝试进行构建
                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                                // 在后面的使用中我们使用的类型是short，得到的是byte类型的缓冲区间大小
                                // 所以是其一般的大小即可，很多人不注意这一点很容易导致多余内存消耗
                                minShortBufferSize = bufferSize / 2;
                                return recorder;
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, rate + "Exception, keep trying.", e);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 初始化缓存的文件
     * 文件已经存在则重新进行覆盖新文件
     *
     * @return 新的文件
     */
    private File initTmpFile() {
        if (tmpFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            tmpFile.delete();
        }
        try {
            if (tmpFile.createNewFile())
                return tmpFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
    private MediaRecorder createNewMediaRecorder() {
        MediaRecorder record = new MediaRecorder();
        record.setAudioSource(MediaRecorder.AudioSource.MIC);
        record.setAudioChannels(1);
        // 先写输出
        record.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
        record.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        record.setAudioEncodingBitRate(96000);
        record.setAudioSamplingRate(44100);
        record.setOutputFile(tmpFile.getAbsolutePath());

        // Start
        try {
            record.prepare();
            record.start();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            record.release();
            return null;
        }

        // Stop
        record.stop();
        record.release();

        return record;
    }
    */

    /**
     * 进行异步录制
     */
    public void recordAsync() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                record();
            }
        };
        thread.start();
    }

    /**
     * 进行同步录制
     *
     * @return 录制完成后返回一个文件，文件就是缓存的文件
     */
    public File record() {
        isCancel = false;
        isDone = false;

        // 开始进行初始化
        AudioRecord audioRecorder;
        File file;
        if ((audioRecorder = initAudioRecord()) == null
                || (file = initTmpFile()) == null) {
            Application.showToast("Record init error!");
            return null;
        }

        // 初始化输出到文件的流
        BufferedOutputStream outputStream;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        final int shortBufferSize = minShortBufferSize;
        final RecordCallback callback = this.callback;

        // 初始化Lame转码库相关参数，传入当前的输入采样率，通道，以及输出的mp3格式的采样率
        Lame lame = new Lame(audioRecorder.getSampleRate(),
                audioRecorder.getChannelCount(),
                audioRecorder.getSampleRate());
        // 构建一个输出流，定向到文件流上面
        LameOutputStream lameOutputStream = new LameOutputStream(lame, outputStream, shortBufferSize);
        // 构建一个异步的编码器，这样可以避免阻塞当前线程读取用户的录音
        LameAsyncEncoder lameAsyncEncoder = new LameAsyncEncoder(lameOutputStream, shortBufferSize);

        int readSize;
        long endTime;

        // 通知开始
        audioRecorder.startRecording();
        callback.onRecordStart();
        // 记录开始的时间
        final long startTime = SystemClock.uptimeMillis();

        // 在当前线程中循环的读取系统录制的用户音频
        while (true) {
            // 从异步Lame编码器中获取一个缓存的buffer，然后把用户的录音读取到里边
            final short[] buffer = lameAsyncEncoder.getFreeBuffer();
            // 开始进行读取
            readSize = audioRecorder.read(buffer, 0, shortBufferSize);
            // 如果读取成功
            if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                // 那么把读取成功的数据，push到异步转码器Lame中，进行异步的处理
                lameAsyncEncoder.push(buffer, readSize);
            }

            // 回调进度
            endTime = SystemClock.uptimeMillis();
            callback.onProgress(endTime - startTime);

            // 如果没有完成标示则继续录制
            if (isDone) {
                break;
            }
        }

        // 进行录制完成
        audioRecorder.stop();
        // 释放录制器
        audioRecorder.release();
        // 当前线程等待异步处理器完成处理
        lameAsyncEncoder.awaitEnd();

        // 如果说不是取消，则通知回调
        if (!isCancel) {
            callback.onRecordDone(file, endTime - startTime);
        }

        // 返回文件
        return file;
    }

    /**
     * 停止录制语音
     * 传递一个参数标示是取消录音还是完成录音
     *
     * @param isCancel True 则代表想要取消录音；False 则代表正常完成录音
     */
    public void stop(boolean isCancel) {
        this.isCancel = isCancel;
        this.isDone = true;
    }

    /**
     * 录制的回调
     */
    public interface RecordCallback {
        // 录制开始的回调
        void onRecordStart();

        // 回调进度，当前的时间
        void onProgress(long time);

        // 录制完成的回调，如果停止录制时传递的是取消，那么则不会回调该方法
        void onRecordDone(File file, long time);
    }
}
