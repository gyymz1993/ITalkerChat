package net.qiujuer.italker.common;

/**
 * @author qiujuer
 */

public class Common {
    /**
     * 一些不可变的永恒的参数
     * 通常用于一些配置
     */
    public interface Constance {
        // 手机号的正则,11位手机号
        String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";

        // 基础的网络请求地址
        String API_URL = "http://192.168.232.161:8080/api/";

        // 最大的上传图片大小860kb
        long MAX_UPLOAD_IMAGE_LENGTH = 860 * 1024;
    }
}
