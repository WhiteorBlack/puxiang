package com.puxiang.mall.module.userinfo.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityVersionBinding;
import com.puxiang.mall.module.userinfo.viewmodel.VersionViewModel;

public class VersionActivity extends BaseBindActivity {
    private ActivityVersionBinding binding;
    private VersionViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_version);
        viewModel = new VersionViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("版本");
        setBarHeight(binding.toolbar.ivBar);
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
            case R.id.ll_version_comment:
                viewModel.marketComment();
                break;
            case R.id.ll_version_explain:
                viewModel.mIntent.setClass(this, VersionExplainActivity.class);
                startActivity(viewModel.mIntent);
                break;
            case R.id.rl_version_update:
                viewModel.checkUpdate();
                break;
        }
    }
}
