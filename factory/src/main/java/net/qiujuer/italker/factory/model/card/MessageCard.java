package net.qiujuer.italker.factory.model.card;

import net.qiujuer.italker.factory.model.db.Group;
import net.qiujuer.italker.factory.model.db.Message;
import net.qiujuer.italker.factory.model.db.User;

import java.util.Date;


/**
 * 消息的卡片，用于接收服务器返回信息
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class MessageCard {
    private String id;
    private String content;
    private String attach;
    private int type;
    private Date createAt;
    private String groupId;
    private String senderId;
    private String receiverId;

    // 两个额外的本地字段
    // transient 不会被Gson序列化和反序列化
    private transient int status = Message.STATUS_DONE; //当前消息状态
    private transient boolean uploaded = false; // 上传是否完成（对应的是文件）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 要构建一个消息，必须准备好3个外键对应的Model
     *
     * @param sender   发送者
     * @param receiver 接收者
     * @param group    接收者-群
     * @return 一个消息
     */
    public Message build(User sender, User receiver, Group group) {
        Message message = new Message();
        message.setId(id);
        message.setContent(content);
        message.setAttach(attach);
        message.setType(type);
        message.setCreateAt(createAt);
        message.setGroup(group);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setStatus(status);
        return message;
    }
}
