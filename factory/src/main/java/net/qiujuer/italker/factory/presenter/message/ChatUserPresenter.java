package net.qiujuer.italker.factory.presenter.message;

import net.qiujuer.italker.factory.data.helper.UserHelper;
import net.qiujuer.italker.factory.data.message.MessageRepository;
import net.qiujuer.italker.factory.model.db.Message;
import net.qiujuer.italker.factory.model.db.User;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView>
        implements ChatContract.Presenter {

    public ChatUserPresenter(ChatContract.UserView view, String receiverId) {
        // 数据源，View，接收者，接收者的类型
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);


    }

    @Override
    public void start() {
        super.start();

        // 从本地拿这个人的信息
        User receiver = UserHelper.findFromLocal(mReceiverId);
        getView().onInit(receiver);
    }
}
