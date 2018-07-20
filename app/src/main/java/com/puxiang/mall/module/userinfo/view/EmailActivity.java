package com.puxiang.mall.module.userinfo.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityEmialBinding;
import com.puxiang.mall.databinding.ActivityNickBinding;
import com.puxiang.mall.module.userinfo.viewmodel.EmailViewModel;
import com.puxiang.mall.module.userinfo.viewmodel.NameViewModel;

public class EmailActivity extends BaseBindActivity {

    private ActivityEmialBinding binding;
    private EmailViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_emial);
        viewModel = new EmailViewModel(this);
        binding.setViewModel(viewModel);
    }

    public void initView() {
        binding.etInfo.setHint("请输入邮箱地址");
        binding.toolbar.setTitle("邮箱地址");
        binding.toolbar.setBtnName("保存");
        setBarHeight(binding.toolbar.ivBar);
    }

    public void onClick(View view) {
        closeInput();
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_toolbar_btn:
                viewModel.modifyName();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }
}
