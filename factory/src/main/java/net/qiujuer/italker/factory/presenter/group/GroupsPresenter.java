package net.qiujuer.italker.factory.presenter.group;

import android.support.v7.util.DiffUtil;

import net.qiujuer.italker.factory.data.group.GroupsDataSource;
import net.qiujuer.italker.factory.data.group.GroupsRepository;
import net.qiujuer.italker.factory.data.helper.GroupHelper;
import net.qiujuer.italker.factory.data.helper.UserHelper;
import net.qiujuer.italker.factory.model.db.Group;
import net.qiujuer.italker.factory.presenter.BaseSourcePresenter;
import net.qiujuer.italker.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 我的群组Presenter
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class GroupsPresenter extends BaseSourcePresenter<Group, Group,
        GroupsDataSource, GroupsContract.View> implements GroupsContract.Presenter {

    public GroupsPresenter(GroupsContract.View view) {
        super(new GroupsRepository(), view);
    }

    @Override
    public void start() {
        super.start();

        // 加载网络数据, 以后可以优化到下拉刷新中
        // 只有用户下拉进行网络请求刷新
        GroupHelper.refreshGroups();
    }

    @Override
    public void onDataLoaded(List<Group> groups) {
        final GroupsContract.View view = getView();
        if (view == null)
            return;

        // 对比差异
        List<Group> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(old, groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 界面刷新
        refreshData(result, groups);
    }
}
