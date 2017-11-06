package net.qiujuer.italker.push.frags.main;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.qiujuer.italker.common.app.PresenterFragment;
import net.qiujuer.italker.common.widget.EmptyView;
import net.qiujuer.italker.common.widget.PortraitView;
import net.qiujuer.italker.common.widget.recycler.RecyclerAdapter;
import net.qiujuer.italker.factory.model.db.Group;
import net.qiujuer.italker.factory.presenter.group.GroupsContract;
import net.qiujuer.italker.factory.presenter.group.GroupsPresenter;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.activities.MessageActivity;

import butterknife.BindView;

public class GroupFragment extends PresenterFragment<GroupsContract.Presenter>
        implements GroupsContract.View {

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    // 适配器，User，可以直接从数据库查询数据
    private RecyclerAdapter<Group> mAdapter;

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group;
    }


    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        // 初始化Recycler
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<Group>() {
            @Override
            protected int getItemViewType(int position, Group group) {
                // 返回cell的布局id
                return R.layout.cell_group_list;
            }

            @Override
            protected ViewHolder<Group> onCreateViewHolder(View root, int viewType) {
                return new GroupFragment.ViewHolder(root);
            }
        });

        // 点击事件监听
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Group>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Group group) {
                // 跳转到聊天界面
                MessageActivity.show(getContext(), group);
            }
        });

        // 初始化占位布局
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);

    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        // 进行一次数据加载
        mPresenter.start();
    }

    @Override
    protected GroupsContract.Presenter initPresenter() {
        return new GroupsPresenter(this);
    }

    @Override
    public RecyclerAdapter<Group> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        // 进行界面操作
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<Group> {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_desc)
        TextView mDesc;

        @BindView(R.id.txt_member)
        TextView mMember;


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Group group) {
            mPortraitView.setup(Glide.with(GroupFragment.this), group.getPicture());
            mName.setText(group.getName());
            mDesc.setText(group.getDesc());

            if (group.holder != null && group.holder instanceof String) {
                mMember.setText((String) group.holder);
            } else {
                mMember.setText("");
            }
        }

    }
}
