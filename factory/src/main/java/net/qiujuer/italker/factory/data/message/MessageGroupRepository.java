package net.qiujuer.italker.factory.data.message;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.qiujuer.italker.factory.data.BaseDbRepository;
import net.qiujuer.italker.factory.model.db.Message;
import net.qiujuer.italker.factory.model.db.Message_Table;

import java.util.Collections;
import java.util.List;

/**
 * 跟群聊天的时候的聊天列表
 * 关注的内容一定是我发给群或者是别人发送到群的信息
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class MessageGroupRepository extends BaseDbRepository<Message>
        implements MessageDataSource {
    // 聊天的群Id
    private String receiverId;

    public MessageGroupRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SucceedCallback<List<Message>> callback) {
        super.load(callback);


        // 无论是直接发还是别人发，只要是发到这个群的，
        // 那个这个group_id就是receiverId
        SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();

    }

    @Override
    protected boolean isRequired(Message message) {
        // 如果消息的Group不为空，则一定是发送到一个群的
        // 如果群Id等于我们需要的，那就是通过
        return message.getGroup() != null
                && receiverId.equalsIgnoreCase(message.getGroup().getId());
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        // 反转返回的集合
        Collections.reverse(tResult);
        // 然后再调度
        super.onListQueryResult(transaction, tResult);
    }
}
