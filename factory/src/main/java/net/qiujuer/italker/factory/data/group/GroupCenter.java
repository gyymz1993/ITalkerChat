package net.qiujuer.italker.factory.data.group;

import net.qiujuer.italker.factory.model.card.GroupCard;
import net.qiujuer.italker.factory.model.card.GroupMemberCard;

/**
 * 群中心的接口定义
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface GroupCenter {
    // 群卡片的处理
    void dispatch(GroupCard... cards);

    // 群成员的处理
    void dispatch(GroupMemberCard... cards);
}
