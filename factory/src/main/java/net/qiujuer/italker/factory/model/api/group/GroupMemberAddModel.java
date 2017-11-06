package net.qiujuer.italker.factory.model.api.group;

import java.util.HashSet;
import java.util.Set;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class GroupMemberAddModel {
    private Set<String> users = new HashSet<>();

    public GroupMemberAddModel(Set<String> users) {
        this.users = users;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
