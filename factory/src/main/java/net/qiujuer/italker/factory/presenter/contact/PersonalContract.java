package net.qiujuer.italker.factory.presenter.contact;

import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.presenter.BaseContract;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface PersonalContract {
    interface Presenter extends BaseContract.Presenter {
        // 获取用户信息
        User getUserPersonal();
    }

    interface View extends BaseContract.View<Presenter> {
        String getUserId();

        // 加载数据完成
        void onLoadDone(User user);

        // 是否发起聊天
        void allowSayHello(boolean isAllow);

        // 设置关注状态
        void setFollowStatus(boolean isFollow);
    }
}
