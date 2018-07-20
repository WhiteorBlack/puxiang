package com.puxiang.mall.module.login.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityBindingMobileBinding;
import com.puxiang.mall.module.login.viewmodel.BindingMobileViewModel;
import com.puxiang.mall.utils.MyTextUtils;
public class BindingMobileActivity extends BaseBindActivity {
    private ActivityBindingMobileBinding binding;
    private BindingMobileViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_binding_mobile);
        viewModel = new BindingMobileViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("绑定手机号");
        setWhiteTitle(binding.toolbar);
        setBarHeight(binding.toolbar.ivBar);
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
