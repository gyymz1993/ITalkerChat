package net.qiujuer.italker.factory.data.message;

import net.qiujuer.italker.factory.data.DbDataSource;
import net.qiujuer.italker.factory.model.db.Message;

/**
 * 消息的数据源定义，他的实现是：MessageRepository, MessageGroupRepository
 * 关注的对象是Message表
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface MessageDataSource extends DbDataSource<Message> {
}
