package com.puxiang.mall.module.im.view;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.View;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityConversationListBinding;
import com.puxiang.mall.model.data.RxUnreadMessage;
import com.puxiang.mall.module.im.viewmodel.ImListViewModel;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;

import java.util.List;

public class ImListActivity extends BaseBindActivity implements EasyPermission.PermissionCallback {

    private ActivityConversationListBinding binding;
    private ImListViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_conversation_list);
        viewModel = new ImListViewModel(this);
        binding.setViewModel(viewModel);
        binding.toolbar.setBackSrc(R.mipmap.nav_back_g);
        binding.toolbar.setColor(R.color.white);
        binding.toolbar.setTextColor(R.color.bbsText);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("我的消息");
        EasyPermission.with(this)
                .rationale(getString(R.string.rationale_audio))
                .permissions(Manifest.permission.RECORD_AUDIO)
                .request();
        setBarHeight(binding.toolbar.ivBar);
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case PermissionCode.RG_RECORD_AUDIO:
                ToastUtil.toast(getString(R.string.rationale_audio));
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.rl_system:
                RxUnreadMessage systemMessage = viewModel.systemMessage.get();
                if (systemMessage != null) {
                    systemMessage.setUnreadCount(0);
                }
                WebUtil.jumpWeb(URLs.HTML_SYSTEM_MESSAGES, this);
                break;
            case R.id.rl_notice:
                RxUnreadMessage noticeMessage = viewModel.noticeMessage.get();
                if (noticeMessage != null) {
                    noticeMessage.setUnreadCount(0);
                }
                WebUtil.jumpWeb(URLs.HTML_NOTICE_MESSAGES, this);
                break;
        }
    }
}