package com.puxiang.mall.module.bbs.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.databinding.ActivityPublishBinding;
import com.puxiang.mall.module.bbs.adapter.PublishAdapter;
import com.puxiang.mall.module.bbs.viewmodel.PublishViewModel;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.MyTextUtils;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;

import java.util.List;

import static com.puxiang.mall.module.bbs.viewmodel.PublishViewModel.SELECT_PLATE_CODE;

public class PublishActivity extends BaseBindActivity implements EasyPermission.PermissionCallback {
    private PublishAdapter adapter;
    private ActivityPublishBinding binding;
    private PublishViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_publish);
        adapter = new PublishAdapter(R.layout.item_publish);
        viewModel = new PublishViewModel(this, adapter);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        initToolBar();
        initRV();
    }

    private void initToolBar() {
        binding.toolbar.setTitle(getString(R.string.publish_title));
        binding.toolbar.setBtnName(getString(R.string.publish_toolbar_btn_name));
    }

    private void initRV() {
        binding.rv.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        binding.rv.setAdapter(adapter);
        binding.rv.addOnItemTouchListener(viewModel.itemClickListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SELECT_PLATE_CODE) {
            viewModel.plateId = data.getStringExtra(Config.PLATE_ID);
            String plateName = data.getStringExtra(Config.PLATE_NAME);
            viewModel.plateName.set(getString(R.string.publish_to) + plateName);
        }
//        if (resultCode==-1){
//            List<Uri> picList=data.getParcelableArrayListExtra("android.intent.extra.RETURN_RESULT");
//            viewModel.setPicList(picList);
//        }
    }

    public void onClick(View view) {
        closeInput();
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_select_plate:
                ActivityUtil.startSelectPlateActivityForResult(this, viewModel.plateId);
                break;
            case R.id.tv_toolbar_btn:
                if (StringUtil.isEmpty(viewModel.plateId)) {
                    ToastUtil.toast(getString(R.string.publish_no_select_plate_tips));
                    return;
                }
                viewModel.content = MyTextUtils.getEditTextString(binding.etContent);
                if (StringUtil.isEmpty(viewModel.content)) {
                    ToastUtil.toast(getString(R.string.publish_none_post_content_tips));
                    return;
                }
                viewModel.uploadImage();
                break;
        }
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {
        if (requestCode == PermissionCode.RG_CAMERA_PERM) {
            if (viewModel != null) {
                viewModel.openCamera();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
    protected void onDestroy() {
        super.onDestroy();
        adapter.getData().clear();
        binding.rv.removeAllViews();
        adapter = null;
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }
}
