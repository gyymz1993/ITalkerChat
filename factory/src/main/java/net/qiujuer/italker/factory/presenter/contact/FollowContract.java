package net.qiujuer.italker.factory.presenter.contact;

import net.qiujuer.italker.factory.model.card.UserCard;
import net.qiujuer.italker.factory.presenter.BaseContract;

/**
 * 关注的接口定义
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface FollowContract {
    // 任务调度者
    interface Presenter extends BaseContract.Presenter {
        // 关注一个人
        void follow(String id);
    }

    interface View extends BaseContract.View<Presenter> {
        // 成功的情况下返回一个用户的信息
        void onFollowSucceed(UserCard userCard);
    }
}
