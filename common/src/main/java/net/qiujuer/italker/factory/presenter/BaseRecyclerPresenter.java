package net.qiujuer.italker.factory.presenter;

import android.support.v7.util.DiffUtil;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.italker.common.widget.recycler.RecyclerAdapter;

import java.util.List;

/**
 * 对RecyclerView进行的一个简单的Presenter封装
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class BaseRecyclerPresenter<ViewMode, View extends BaseContract.RecyclerView>
        extends BasePresenter<View> {
    public BaseRecyclerPresenter(View view) {
        super(view);
    }

    /**
     * 刷新一堆新数据到界面中
     *
     * @param dataList 新数据
     */
    protected void refreshData(final List<ViewMode> dataList) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                View view = getView();
                if (view == null)
                    return;

                // 基本的更新数据并刷新界面
                RecyclerAdapter<ViewMode> adapter = view.getRecyclerAdapter();
                adapter.replace(dataList);
                view.onAdapterDataChanged();
            }
        });
    }

    /**
     * 刷新界面操作，该操作可以保证执行方法在主线程进行
     *
     * @param diffResult 一个差异的结果集
     * @param dataList   具体的新数据
     */
    protected void refreshData(final DiffUtil.DiffResult diffResult, final List<ViewMode> dataList) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                // 这里是主线程运行时
                refreshDataOnUiThread(diffResult, dataList);
            }
        });
    }


    private void refreshDataOnUiThread(final DiffUtil.DiffResult diffResult, final List<ViewMode> dataList) {
        View view = getView();
        if (view == null)
            return;
        // 基本的更新数据并刷新界面
        RecyclerAdapter<ViewMode> adapter = view.getRecyclerAdapter();
        // 改变数据集合并不通知界面刷新
        adapter.getItems().clear();
        adapter.getItems().addAll(dataList);
        // 通知界面刷新占位布局
        view.onAdapterDataChanged();

        // 进行增量更新
        diffResult.dispatchUpdatesTo(adapter);
    }

}
