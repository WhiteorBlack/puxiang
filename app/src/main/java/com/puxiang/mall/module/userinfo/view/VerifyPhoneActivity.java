package com.puxiang.mall.module.userinfo.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityVerifyMobileBinding;
import com.puxiang.mall.module.userinfo.viewmodel.VerifyPhoneViewModel;

/**
 * Created by zhaoyong bai on 2017/11/25.
 */

public class VerifyPhoneActivity extends BaseBindActivity implements View.OnClickListener {
    private ActivityVerifyMobileBinding binding;
    private VerifyPhoneViewModel viewModel;
    @Override
    protected void initBind() {
        binding= DataBindingUtil.setContentView(this, R.layout.activity_verify_mobile);
        viewModel=new VerifyPhoneViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("验证手机号码");
    }

    @Override
    protected void viewModelDestroy() {

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_register:
                viewModel.verifyPhone(binding.etCode.getText().toString());
                break;
            case R.id.cb_sms:
                viewModel.getSmsCode();
                break;
        }
    }
}
