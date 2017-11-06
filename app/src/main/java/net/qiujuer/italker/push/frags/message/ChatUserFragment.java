package net.qiujuer.italker.push.frags.message;


import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.italker.common.widget.PortraitView;
import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.presenter.message.ChatContract;
import net.qiujuer.italker.factory.presenter.message.ChatUserPresenter;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.activities.PersonalActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户聊天界面
 */
public class ChatUserFragment extends ChatFragment<User>
        implements ChatContract.UserView {
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    private MenuItem mUserInfoMenuItem;

    public ChatUserFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getHeaderLayoutId() {
        return R.layout.lay_chat_header_user;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        Glide.with(this)
                .load(R.drawable.default_banner_chat)
                .centerCrop()
                .into(new ViewTarget<CollapsingToolbarLayout, GlideDrawable>(mCollapsingLayout) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setContentScrim(resource.getCurrent());
                    }
                });

    }

    @Override
    protected void initToolbar() {
        super.initToolbar();

        Toolbar toolbar = mToolbar;
        toolbar.inflateMenu(R.menu.chat_user);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_person) {
                    onPortraitClick();
                }
                return false;
            }
        });

        // 拿到菜单Icon
        mUserInfoMenuItem = toolbar.getMenu().findItem(R.id.action_person);
    }

    // 进行高度的综合运算，透明我们的头像和Icon
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        View view = mPortrait;
        MenuItem menuItem = mUserInfoMenuItem;

        if (view == null || menuItem == null)
            return;


        if (verticalOffset == 0) {
            // 完全展开
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);

            // 隐藏菜单
            menuItem.setVisible(false);
            menuItem.getIcon().setAlpha(0);
        } else {
            // abs 运算
            verticalOffset = Math.abs(verticalOffset);
            final int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScrollRange) {
                // 关闭状态
                view.setVisibility(View.INVISIBLE);
                view.setScaleX(0);
                view.setScaleY(0);
                view.setAlpha(0);

                // 显示菜单
                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha(255);

            } else {
                // 中间状态
                float progress = 1 - verticalOffset / (float) totalScrollRange;
                view.setVisibility(View.VISIBLE);
                view.setScaleX(progress);
                view.setScaleY(progress);
                view.setAlpha(progress);
                // 和头像恰好相反
                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha(255 - (int) (255 * progress));
            }
        }
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        PersonalActivity.show(getContext(), mReceiverId);
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        // 初始化Presenter
        return new ChatUserPresenter(this, mReceiverId);
    }

    @Override
    public void onInit(User user) {
        // 对和你聊天的朋友的信息进行初始化操作
        mPortrait.setup(Glide.with(this), user.getPortrait());
        mCollapsingLayout.setTitle(user.getName());
    }
}
