package net.qiujuer.italker.factory.presenter.group;

import net.qiujuer.italker.factory.model.db.view.MemberUserModel;
import net.qiujuer.italker.factory.presenter.BaseContract;

/**
 * 群成员的契约
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface GroupMembersContract {
    interface Presenter extends BaseContract.Presenter {
        // 具有一个刷新的方法
        void refresh();
    }

    // 界面
    interface View extends BaseContract.RecyclerView<Presenter, MemberUserModel> {
        // 获取群的ID
        String getGroupId();
    }
}
