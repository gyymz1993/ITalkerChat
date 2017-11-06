package net.qiujuer.italker.factory.data.group;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.qiujuer.italker.factory.data.BaseDbRepository;
import net.qiujuer.italker.factory.data.helper.GroupHelper;
import net.qiujuer.italker.factory.model.db.Group;
import net.qiujuer.italker.factory.model.db.Group_Table;
import net.qiujuer.italker.factory.model.db.view.MemberUserModel;

import java.util.List;

/**
 * 我的群组的数据仓库 是对GroupsDataSource的实现
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class GroupsRepository extends BaseDbRepository<Group>
        implements GroupsDataSource {


    @Override
    public void load(SucceedCallback<List<Group>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Group.class)
                .orderBy(Group_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Group group) {
        // 一个群的信息，只可能两种情况出现在数据库
        // 一个是你被别人加入群，第二是你直接建立一个群
        // 无论什么情况，你拿到的都只是群的信息，没有成员的信息
        // 你需要进行成员信息初始化操作
        if (group.getGroupMemberCount() > 0) {
            // 以及初始化了成员的信息
            group.holder = buildGroupHolder(group);
        } else {
            // 待初始化的群的信息
            group.holder = null;
            GroupHelper.refreshGroupMember(group);
        }

        // 所有的群我都需要关注显示
        return true;
    }

    // 初始化界面显示的成员信息
    private String buildGroupHolder(Group group) {
        List<MemberUserModel> userModels = group.getLatelyGroupMembers();
        if (userModels == null || userModels.size() == 0)
            return null;

        StringBuilder builder = new StringBuilder();
        for (MemberUserModel userModel : userModels) {
            builder.append(TextUtils.isEmpty(userModel.alias) ? userModel.name : userModel.alias);
            builder.append(", ");
        }

        builder.delete(builder.lastIndexOf(", "), builder.length());

        return builder.toString();
    }
}
