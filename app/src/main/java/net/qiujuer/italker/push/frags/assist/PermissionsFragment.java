package net.qiujuer.italker.push.frags.assist;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.qiujuer.italker.common.app.Application;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.frags.media.GalleryFragment;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 权限申请弹出框
 */
public class PermissionsFragment extends BottomSheetDialogFragment
        implements EasyPermissions.PermissionCallbacks {
    // 权限回调的标示
    private static final int RC = 0x0100;

    public PermissionsFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 复用即可
        return new GalleryFragment.TransStatusBottomSheetDialog(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 获取布局中的控件
        View root = inflater.inflate(R.layout.fragment_permissions, container, false);

        // 找到按钮
        root.findViewById(R.id.btn_submit)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 点击时进行申请权限
                        requestPerm();
                    }
                });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 界面显示的时候进行刷新
        refreshState(getView());
    }

    /**
     * 刷新我们的布局中的图片的状态
     *
     * @param root 跟布局
     */
    private void refreshState(View root) {
        if (root == null)
            return;

        Context context = getContext();

        root.findViewById(R.id.im_state_permission_network)
                .setVisibility(haveNetworkPerm(context) ? View.VISIBLE : View.GONE);

        root.findViewById(R.id.im_state_permission_read)
                .setVisibility(haveReadPerm(context) ? View.VISIBLE : View.GONE);

        root.findViewById(R.id.im_state_permission_write)
                .setVisibility(haveWritePerm(context) ? View.VISIBLE : View.GONE);

        root.findViewById(R.id.im_state_permission_record_audio)
                .setVisibility(haveRecordAudioPerm(context) ? View.VISIBLE : View.GONE);
    }

    /**
     * 获取是否有网络权限
     *
     * @param context 上下文
     * @return True则有
     */
    private static boolean haveNetworkPerm(Context context) {
        // 准备需要检查的网络权限
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 获取是否有外部存储读取权限
     *
     * @param context 上下文
     * @return True则有
     */
    private static boolean haveReadPerm(Context context) {
        // 准备需要检查的读取权限
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 获取是否有外部存储写入权限
     *
     * @param context 上下文
     * @return True则有
     */
    private static boolean haveWritePerm(Context context) {
        // 准备需要检查的写入权限
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 获取是否录音权限
     *
     * @param context 上下文
     * @return True则有
     */
    private static boolean haveRecordAudioPerm(Context context) {
        // 准备需要检查的录音权限
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO
        };

        return EasyPermissions.hasPermissions(context, perms);
    }


    // 私有的show方法
    private static void show(FragmentManager manager) {
        // 调用BottomSheetDialogFragment以及准备好的显示方法
        new PermissionsFragment()
                .show(manager, PermissionsFragment.class.getName());
    }


    /**
     * 检查是否具有所有的权限
     *
     * @param context Context
     * @param manager FragmentManager
     * @return 是否有权限
     */
    public static boolean haveAll(Context context, FragmentManager manager) {
        // 检查是否具有所有的权限
        boolean haveAll = haveNetworkPerm(context)
                && haveReadPerm(context)
                && haveWritePerm(context)
                && haveRecordAudioPerm(context);

        // 如果没有则显示当前申请权限的界面
        if (!haveAll) {
            show(manager);
        }

        return haveAll;
    }


    /**
     * 申请权限的方法
     */
    @AfterPermissionGranted(RC)
    private void requestPerm() {
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };

        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            Application.showToast(R.string.label_permission_ok);
            // Fragment 中调用getView得到跟布局，前提是在onCreateView方法之后
            refreshState(getView());
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.title_assist_permissions),
                    RC, perms);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // 如果权限有没有申请成功的权限存在，则弹出弹出框，用户点击后去到设置界面自己打开权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog
                    .Builder(this)
                    .build()
                    .show();
        }
    }

    /**
     * 权限申请的时候回调的方法，在这个方法中把对应的权限申请状态交给EasyPermissions框架
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 传递对应的参数，并且告知接收权限的处理者是我自己
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
