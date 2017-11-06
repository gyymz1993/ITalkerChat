package net.qiujuer.italker.factory.presenter.group;

import net.qiujuer.italker.factory.presenter.BaseContract;

/**
 * 群成员添加的契约
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface GroupMemberAddContract {
    interface Presenter extends BaseContract.Presenter {
        // 提交成员
        void submit();

        // 更改一个Model的选中状态
        void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected);
    }

    // 界面
    interface View extends BaseContract.RecyclerView<Presenter, GroupCreateContract.ViewModel> {
        // 添加群成员成功
        void onAddedSucceed();

        // 获取群的Id
        String getGroupId();
    }
}
