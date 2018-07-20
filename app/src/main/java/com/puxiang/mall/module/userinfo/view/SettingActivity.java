package com.puxiang.mall.module.userinfo.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.databinding.ActivitySettingsBinding;
import com.puxiang.mall.module.userinfo.viewmodel.SettingsViewModel;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.WebUtil;

public class SettingActivity extends BaseBindActivity implements View.OnClickListener {
    private Intent mIntent;
    private ActivitySettingsBinding binding;
    private SettingsViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        viewModel = new SettingsViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("设置");
        mIntent = getIntent();
        boolean isNewestVersion = mIntent.getBooleanExtra(CacheKey.IS_NEWEST_VERSION, false);
        binding.setIsNewestVersion(isNewestVersion);
        binding.toolbar.setBackSrc(R.mipmap.nav_back_g);
        binding.toolbar.setColor(R.color.white);
        binding.toolbar.setTextColor(R.color.bbsText);
        setBarHeight(binding.toolbar.ivBar);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_settings_password:
                //修改为账户与安全
                ActivityUtil.startForgetActivity(this);
                break;
            case R.id.ll_settings_info:
                ActivityUtil.startInfoActivity(this);
                break;
            case R.id.ll_settings_version:
                Intent intent = mIntent;
                intent.setClass(this, VersionActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_about:
                WebUtil.jumpWeb(URLs.HTML_MY_ABOUT, this);
                break;
            case R.id.ll_settings_logout:
                viewModel.logOutDialog();
                break;
            case R.id.ll_service:

                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

}
