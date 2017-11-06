package net.qiujuer.italker.factory.presenter.group;

import net.qiujuer.italker.factory.model.db.Group;
import net.qiujuer.italker.factory.presenter.BaseContract;

/**
 * 我的群列表契约
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface GroupsContract {
    // 什么都不需要额外定义，开始就是调用start即可
    interface Presenter extends BaseContract.Presenter {

    }

    // 都在基类完成了
    interface View extends BaseContract.RecyclerView<Presenter, Group> {

    }
}
