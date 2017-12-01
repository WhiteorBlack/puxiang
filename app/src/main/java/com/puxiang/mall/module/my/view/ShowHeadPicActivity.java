package com.puxiang.mall.module.my.view;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityShowBinding;
import com.puxiang.mall.module.my.viewmodel.ShowViewModel;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;

import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;

public class ShowHeadPicActivity extends BaseBindActivity implements EasyPermission.PermissionCallback {
    private static final int REQUEST_CODE_CAMERA = 1000;
    private ActivityShowBinding binding;
    private ShowViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show);
        viewModel = new ShowViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("头像");
        binding.toolbar.setBtnName("修改");
        if (!viewModel.isOwn) {
            binding.toolbar.tvToolbarBtn.setVisibility(View.GONE);
        }
        binding.sdvShow.setOnLongClickListener(view -> {
            viewModel.saveImg();
            return true;
        });
        mImmersionBar.statusBarDarkFont(false).init();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_toolbar_btn:
                viewModel.getPic();
                break;
        }
    }


    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case PermissionCode.RG_CAMERA_PERM:
                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, GalleryFinal.getCoreConfig().getFunctionConfig(),
                        viewModel.resultCallback);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case PermissionCode.RG_CAMERA_PERM:
                ToastUtil.toast(getString(R.string.rationale_camera));
                break;
        }
    }


    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }
}
