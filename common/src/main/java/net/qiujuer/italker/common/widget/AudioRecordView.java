package net.qiujuer.italker.common.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.FloatActionButton;
import net.qiujuer.italker.common.R;

/**
 * 和QQ录音面板类似的控件，充当录音的发起者功能
 * 发起录音-结束录音（正常结束，取消，播放，删除）四种结束方式
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class AudioRecordView extends FrameLayout implements View.OnTouchListener {
    // 相关的结束方式
    // 结束是因为取消
    public static final int END_TYPE_CANCEL = 0;
    // 正常结束
    public static final int END_TYPE_NONE = 1;
    // 结束后想要播放
    public static final int END_TYPE_PLAY = 2;
    // 结束后想要删除
    public static final int END_TYPE_DELETE = 3;

    // 相关删除／播放按钮的透明度
    private static final float MIN_ALPHA = 0.4f;
    // 触摸的点坐标
    private final float[] mTouchPoint = new float[2];
    // 播放的位置
    private final Rect mPlayLocation = new Rect();
    // 删除的位置
    private final Rect mDeleteLocation = new Rect();
    // 录制按钮的位置
    private final Rect mRectLocation = new Rect();
    // 浮动的录制按钮
    private FloatActionButton mRecordButton;
    // 播放按钮和删除按钮
    private ImageView mPlayButton, mDeleteButton;
    // 标示正在录制的状态
    private boolean mIsRecording;
    // 回调方法
    private Callback mCallback;

    public AudioRecordView(@NonNull Context context) {
        super(context);
        init();
    }

    public AudioRecordView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AudioRecordView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.lay_record_view, this);
        mRecordButton = (FloatActionButton) findViewById(R.id.btn_record);
        mPlayButton = (ImageView) findViewById(R.id.im_play);
        mDeleteButton = (ImageView) findViewById(R.id.im_delete);
        mRecordButton.setOnTouchListener(this);
        turnRecord();
    }

    /**
     * 初始化开始方法
     *
     * @param callback 设置一个状态回调
     */
    public void setup(Callback callback) {
        this.mCallback = callback;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 当界面改变时得到对应的坐标位置

        mPlayLocation.left = mPlayButton.getLeft() - left;
        mPlayLocation.top = mPlayButton.getTop() - top;
        mPlayLocation.right = mPlayButton.getRight() - left;
        mPlayLocation.bottom = mPlayButton.getBottom() - top;

        mDeleteLocation.left = mDeleteButton.getLeft() - left;
        mDeleteLocation.top = mDeleteButton.getTop() - top;
        mDeleteLocation.right = mDeleteButton.getRight() - left;
        mDeleteLocation.bottom = mDeleteButton.getBottom() - top;

        mRectLocation.left = mRecordButton.getLeft() - left;
        mRectLocation.top = mRecordButton.getTop() - top;
        mRectLocation.right = mRecordButton.getRight() - left;
        mRectLocation.bottom = mRecordButton.getBottom() - top;
    }


    // 切换录音状态
    private void turnRecord() {
        if (mIsRecording) {
            mPlayButton.animate()
                    .alpha(MIN_ALPHA)
                    .scaleX(1)
                    .scaleY(1)
                    .setDuration(320)
                    .setInterpolator(new AnticipateOvershootInterpolator())
                    .start();
            mDeleteButton.animate()
                    .alpha(MIN_ALPHA)
                    .scaleX(1)
                    .scaleY(1)
                    .setDuration(320)
                    .setInterpolator(new AnticipateOvershootInterpolator())
                    .start();
        } else {
            mPlayButton.animate()
                    .alpha(0)
                    .scaleX(0)
                    .scaleY(0)
                    .setDuration(260)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
            mDeleteButton.animate()
                    .alpha(0)
                    .scaleX(0)
                    .scaleY(0)
                    .setDuration(260)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }
    }

    // 当用户按下按钮时开始进行录音
    private void onStart() {
        this.mIsRecording = true;
        turnRecord();


        Callback callback = mCallback;
        if (callback != null) {
            callback.requestStartRecord();
        }
    }

    // 当松开时结束
    private void onStop(boolean isCancel) {
        if (!mIsRecording)
            return;

        mIsRecording = false;
        turnRecord();

        Callback callback = mCallback;
        if (callback != null) {
            callback.requestStopRecord(isCancel ? END_TYPE_CANCEL :
                    (mActiveView == null ? END_TYPE_NONE : (
                            mActiveView == mPlayButton ? END_TYPE_PLAY : END_TYPE_DELETE)));
        }
    }

    // 监听用户的手指触摸，并相应对应的开始，停止，取消等操作
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                float pointX = event.getX();
                float pointY = event.getY();

                mTouchPoint[0] = pointX + mRectLocation.left;
                mTouchPoint[1] = pointY + mRectLocation.top;

                boolean inLeft = mTouchPoint[0] < mRectLocation.centerX();

                Rect rect = inLeft ? mPlayLocation : mDeleteLocation;
                double spaceLen = calculatePointDistance(mTouchPoint[0], mTouchPoint[1],
                        rect.centerX(), rect.centerY());

                refreshAlpha(inLeft, spaceLen);
                break;
            case MotionEvent.ACTION_DOWN:
                onStart();
                break;
            case MotionEvent.ACTION_CANCEL:
                onStop(true);
                break;
            case MotionEvent.ACTION_UP:
                onStop(false);
                break;
        }
        return false;
    }

    // 计算两个点之间的距离
    private double calculatePointDistance(float px1, float py1, float px2, float py2) {
        double spaceX = Math.abs(px1 - px2);
        double spaceY = Math.abs(py1 - py2);
        return Math.sqrt(spaceX * spaceX + spaceY * spaceY);
    }

    // 记录最后一次的进度值，当进度值一致时不做刷新
    private float mLastProgress;
    // 当前被激活的布局(播放，或者停止)
    private View mActiveView;

    // 根据手指滑动更改透明度
    private void refreshAlpha(boolean inLeft, double spaceLen) {
        Rect rect = inLeft ? mPlayLocation : mDeleteLocation;
        double maxLen = calculatePointDistance(mRectLocation.centerX(), mRectLocation.centerY(),
                rect.centerX(), rect.centerY());


        float progress = Math.round(spaceLen / maxLen * 1000) / 1000f;
        if (mLastProgress == progress)
            return;
        mLastProgress = progress;
        Log.e("TAG", "mLastProgress:" + mLastProgress);
        progress = 1 - Math.max(0, Math.min(1, progress));

        float[] touchPoint = mTouchPoint;
        boolean overFlowIcon = rect.contains((int) touchPoint[0], (int) touchPoint[1]);

        ImageView activeView, noneView;
        if (inLeft) {
            activeView = mPlayButton;
            noneView = mDeleteButton;
        } else {
            activeView = mDeleteButton;
            noneView = mPlayButton;
        }

        activeView.setAlpha(MIN_ALPHA + (1 - MIN_ALPHA) * progress);
        int actionTintColor = overFlowIcon ? UiCompat.getColor(getResources(), R.color.colorAccent) :
                UiCompat.getColor(getResources(), R.color.textPrimary);
        DrawableCompat.setTint(activeView.getDrawable(), actionTintColor);
        DrawableCompat.setTint(activeView.getBackground(), actionTintColor);

        noneView.setAlpha(MIN_ALPHA);
        int noneTintColor = UiCompat.getColor(getResources(), R.color.textPrimary);
        DrawableCompat.setTint(noneView.getDrawable(), noneTintColor);
        DrawableCompat.setTint(noneView.getBackground(), noneTintColor);

        float scale = 1 + 0.2f * progress;
        activeView.setScaleX(scale);
        activeView.setScaleY(scale);

        mActiveView = overFlowIcon ? activeView : null;
    }


    // 回调的状态类
    public interface Callback {
        // 请求开始录音
        void requestStartRecord();

        // 请求结束录音，并携带结束标示
        void requestStopRecord(int type);
    }
}
