package com.puxiang.mall.module.userinfo.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityBindingMobileBinding;
import com.puxiang.mall.databinding.ActivityChangeMobileBinding;
import com.puxiang.mall.module.login.viewmodel.BindingMobileViewModel;
import com.puxiang.mall.module.userinfo.viewmodel.ChangeMobileViewModel;
import com.puxiang.mall.utils.MyTextUtils;

public class ChangeMobileActivity extends BaseBindActivity {
    private ActivityChangeMobileBinding binding;
    private ChangeMobileViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_mobile);
        viewModel = new ChangeMobileViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("变更手机号");
    }

    public void onClick(View v) {
        closeInput();
        switch (v.getId()) {
            case R.id.btn_register:
                String account = MyTextUtils.getEditTextString(binding.etAccount);
                String smsCode = MyTextUtils.getEditTextString(binding.etCode);
                viewModel.verifyInput(account, smsCode);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.cb_sms:
                viewModel.checkMobile(MyTextUtils.getEditTextString(binding.etAccount));
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }
}
