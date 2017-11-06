package net.qiujuer.italker.factory.model.api.message;

import net.qiujuer.italker.factory.model.card.MessageCard;
import net.qiujuer.italker.factory.model.db.Message;
import net.qiujuer.italker.factory.persistence.Account;

import java.util.Date;
import java.util.UUID;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class MsgCreateModel {
    // ID从客户端生产，一个UUID
    private String id;
    private String content;
    private String attach;

    // 消息类型
    private int type = Message.TYPE_STR;

    // 接收者 可为空
    private String receiverId;

    // 接收者类型，群，人
    private int receiverType = Message.RECEIVER_TYPE_NONE;

    private MsgCreateModel() {
        // 随机生产一个UUID
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAttach() {
        return attach;
    }

    public int getType() {
        return type;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public int getReceiverType() {
        return receiverType;
    }


    // 当我们需要发送一个文件的时候，content刷新的问题

    private MessageCard card;

    // 返回一个Card
    public MessageCard buildCard() {
        if (card == null) {
            MessageCard card = new MessageCard();
            card.setId(id);
            card.setContent(content);
            card.setAttach(attach);
            card.setType(type);
            card.setSenderId(Account.getUserId());

            // 如果是群
            if (receiverType == Message.RECEIVER_TYPE_GROUP) {
                card.setGroupId(receiverId);
            } else {
                card.setReceiverId(receiverId);
            }

            // 通过当前model建立的Card就是一个初步状态的Card
            card.setStatus(Message.STATUS_CREATED);
            card.setCreateAt(new Date());
            this.card = card;
        }
        return this.card;
    }

    // 同步到卡片的最新状态
    public void refreshByCard() {
        if (card == null)
            return;
        // 刷新内容和附件信息
        this.content = card.getContent();
        this.attach = card.getAttach();
    }


    /**
     * 建造者模式，快速的建立一个发送Model
     */
    public static class Builder {
        private MsgCreateModel model;

        public Builder() {
            this.model = new MsgCreateModel();
        }

        // 设置接收者
        public Builder receiver(String receiverId, int receiverType) {
            this.model.receiverId = receiverId;
            this.model.receiverType = receiverType;
            return this;
        }

        // 设置内容
        public Builder content(String content, int type) {
            this.model.content = content;
            this.model.type = type;
            return this;
        }

        public Builder attach(String attach) {
            this.model.attach = attach;
            return this;
        }

        public MsgCreateModel build() {
            return this.model;
        }

    }

    /**
     * 把一个Message消息，转换为一个创建状态的CreateModel
     *
     * @param message Message
     * @return MsgCreateModel
     */
    public static MsgCreateModel buildWithMessage(Message message) {
        MsgCreateModel model = new MsgCreateModel();
        model.id = message.getId();
        model.content = message.getContent();
        model.type = message.getType();
        model.attach = message.getAttach();

        if (message.getReceiver() != null) {
            // 如果接收者不为null，则是给人发送消息
            model.receiverId = message.getReceiver().getId();
            model.receiverType = Message.RECEIVER_TYPE_NONE;
        } else {
            model.receiverId = message.getGroup().getId();
            model.receiverType = Message.RECEIVER_TYPE_GROUP;
        }

        return model;
    }
}
