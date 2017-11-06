package net.qiujuer.italker.factory.presenter.group;

import android.text.TextUtils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.italker.factory.Factory;
import net.qiujuer.italker.factory.R;
import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.data.helper.GroupHelper;
import net.qiujuer.italker.factory.data.helper.UserHelper;
import net.qiujuer.italker.factory.model.api.group.GroupCreateModel;
import net.qiujuer.italker.factory.model.card.GroupCard;
import net.qiujuer.italker.factory.model.db.view.UserSampleModel;
import net.qiujuer.italker.factory.net.UploadHelper;
import net.qiujuer.italker.factory.presenter.BaseRecyclerPresenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 群创建界面的Presenter
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class GroupCreatePresenter extends BaseRecyclerPresenter<GroupCreateContract.ViewModel, GroupCreateContract.View>
        implements GroupCreateContract.Presenter, DataSource.Callback<GroupCard> {

    private Set<String> users = new HashSet<>();

    public GroupCreatePresenter(GroupCreateContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        // 加载
        Factory.runOnAsync(loader);
    }

    @Override
    public void create(final String name, final String desc, final String picture) {
        GroupCreateContract.View view = getView();
        view.showLoading();

        // 判断参数
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) ||
                TextUtils.isEmpty(picture) || users.size() == 0) {
            view.showError(R.string.label_group_create_invalid);
            return;
        }

        // 上传图片
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = uploadPicture(picture);
                if (TextUtils.isEmpty(url))
                    return;
                // 进行网络请求
                GroupCreateModel model = new GroupCreateModel(name, desc, url, users);
                GroupHelper.create(model, GroupCreatePresenter.this);
            }
        });
    }

    @Override
    public void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected) {
        if (isSelected)
            users.add(model.author.getId());
        else
            users.remove(model.author.getId());
    }

    // 同步上传操作
    private String uploadPicture(String path) {
        String url = UploadHelper.uploadPortrait(path);
        if (TextUtils.isEmpty(url)) {
            // 切换到UI线程 提示信息
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    GroupCreateContract.View view = getView();
                    if (view != null) {
                        view.showError(R.string.data_upload_error);
                    }
                }
            });
        }
        return url;
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            List<UserSampleModel> sampleModels = UserHelper.getSampleContact();
            List<GroupCreateContract.ViewModel> models = new ArrayList<>();
            for (UserSampleModel sampleModel : sampleModels) {
                GroupCreateContract.ViewModel viewModel = new GroupCreateContract.ViewModel();
                viewModel.author = sampleModel;
                models.add(viewModel);
            }

            refreshData(models);
        }
    };

    @Override
    public void onDataLoaded(GroupCard groupCard) {
        // 成功
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                GroupCreateContract.View view = getView();
                if (view != null) {
                    view.onCreateSucceed();
                }
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        // 失败情况
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                GroupCreateContract.View view = getView();
                if (view != null) {
                    view.showError(strRes);
                }
            }
        });
    }
}
