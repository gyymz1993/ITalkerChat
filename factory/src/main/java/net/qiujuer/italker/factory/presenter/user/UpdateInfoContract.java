package net.qiujuer.italker.factory.presenter.user;

import net.qiujuer.italker.factory.presenter.BaseContract;

/**
 * 更新用户信息的基本的契约
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface UpdateInfoContract {
    interface Presenter extends BaseContract.Presenter {
        // 更新
        void update(String photoFilePath, String desc, boolean isMan);
    }

    interface View extends BaseContract.View<Presenter> {
        // 回调成功
        void updateSucceed();
    }
}
