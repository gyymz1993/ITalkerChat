package net.qiujuer.italker.utils;

import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 *          <p>
 *          对文件流的操作工具类
 */

@SuppressWarnings("WeakerAccess")
public class StreamUtil {

    /**
     * Copy 文件
     *
     * @param in           文件
     * @param outputStream 输出流
     * @return 是否copy成功
     */
    public static boolean copy(File in, OutputStream outputStream) {
        if (!in.exists())
            return false;

        InputStream stream;
        try {
            stream = new FileInputStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return copy(stream, outputStream);
    }

    /**
     * 把一个文件copy到另外一个文件
     *
     * @param in  输入文件
     * @param out 输出文件
     * @return 是否copy成功
     */
    public static boolean copy(File in, File out) {
        if (!in.exists())
            return false;

        InputStream stream;
        try {
            stream = new FileInputStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return copy(stream, out);
    }

    /**
     * 把输入流输出到文件
     *
     * @param inputStream 输入流
     * @param out         输出文件
     * @return 是否copy成功
     */
    public static boolean copy(InputStream inputStream, File out) {
        if (!out.exists()) {
            File fileParentDir = out.getParentFile();
            if (!fileParentDir.exists()) {
                if (!fileParentDir.mkdirs())
                    return false;
            }
            try {
                if (!out.createNewFile())
                    return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return copy(inputStream, outputStream);
    }

    /**
     * 把一个输入流定向到输出流
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @return 是否输出成功
     */
    public static boolean copy(InputStream inputStream, OutputStream outputStream) {
        try {
            byte buffer[] = new byte[1024];
            int realLength;
            while ((realLength = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, realLength);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(inputStream);
            close(outputStream);
        }
    }

    /**
     * 对流进行close操作
     *
     * @param closeables Closeable
     */
    public static void close(Closeable... closeables) {
        if (closeables == null)
            return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除某路径的文件
     *
     * @param path 文件路径
     * @return 删除是否成功
     */
    public static boolean delete(String path) {
        if (TextUtils.isEmpty(path))
            return false;
        File file = new File(path);
        return file.exists() && file.delete();
    }
}
