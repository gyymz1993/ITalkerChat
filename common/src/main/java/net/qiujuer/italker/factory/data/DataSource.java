package net.qiujuer.italker.factory.data;

import android.support.annotation.StringRes;

/**
 * 数据源接口定义
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface DataSource {

    /**
     * 同时包括了成功与失败的回调接口
     *
     * @param <T> 任意类型
     */
    interface Callback<T> extends SucceedCallback<T>, FailedCallback {

    }

    /**
     * 只关注成功的接口
     *
     * @param <T> 任意类型
     */
    interface SucceedCallback<T> {
        // 数据加载成功, 网络请求成功
        void onDataLoaded(T t);

    }

    /**
     * 只关注失败的接口
     */
    interface FailedCallback {
        // 数据加载失败, 网络请求失败
        void onDataNotAvailable(@StringRes int strRes);
    }


    /**
     * 销毁操作
     */
    void dispose();

}
