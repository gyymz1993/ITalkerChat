package net.qiujuer.italker.push.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.italker.common.app.Activity;
import net.qiujuer.italker.common.app.Fragment;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.frags.account.AccountTrigger;
import net.qiujuer.italker.push.frags.account.LoginFragment;
import net.qiujuer.italker.push.frags.account.RegisterFragment;

import butterknife.BindView;

public class AccountActivity extends Activity implements AccountTrigger {
    private Fragment mCurFragment;
    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;

    @BindView(R.id.im_bg)
    ImageView mBg;

    /**
     * 账户Activity显示的入口
     *
     * @param context Context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        // 初始化Fragment
        mCurFragment = mLoginFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();

        // 初始化背景
        Glide.with(this)
                .load(R.drawable.bg_src_tianjin)
                .centerCrop() //居中剪切
                .into(new ViewTarget<ImageView, GlideDrawable>(mBg) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        // 拿到glide的Drawable
                        Drawable drawable = resource.getCurrent();
                        // 使用适配类进行包装
                        drawable = DrawableCompat.wrap(drawable);
                        drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent),
                                PorterDuff.Mode.SCREEN); // 设置着色的效果和颜色，蒙板模式
                        // 设置给ImageView
                        this.view.setImageDrawable(drawable);
                    }
                });
    }

    @Override
    public void triggerView() {
        Fragment fragment;
        if (mCurFragment == mLoginFragment) {
            if (mRegisterFragment == null) {
                //默认情况下为null，
                //第一次之后就不为null了
                mRegisterFragment = new RegisterFragment();
            }
            fragment = mRegisterFragment;
        } else {
            // 因为默认请求下mLoginFragment已经赋值，无须判断null
            fragment = mLoginFragment;
        }

        // 重新赋值当前正在显示的Fragment
        mCurFragment = fragment;
        // 切换显示ø
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.lay_container, fragment)
                .commit();
    }
}
