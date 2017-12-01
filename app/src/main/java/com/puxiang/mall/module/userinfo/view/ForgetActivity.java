package com.puxiang.mall.module.userinfo.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityForgetBinding;
import com.puxiang.mall.module.userinfo.viewmodel.ForgetActivityViewModel;
import com.puxiang.mall.utils.MyTextUtils;
import com.puxiang.mall.utils.StringUtil;

public class ForgetActivity extends BaseBindActivity implements View.OnClickListener {
    private ActivityForgetBinding binding;
    private ForgetActivityViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget);
        viewModel = new ForgetActivityViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("忘记密码");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.cb_sms:
                viewModel.checkMobile(MyTextUtils.getEditTextString(binding.etAccounts));
                break;
            case R.id.btn_save:
                String code = MyTextUtils.getEditTextString(binding.etCode);
                if (StringUtil.isEmpty(code)) {
                    binding.etCode.setError("请填入验证码");
                } else {
                    viewModel.checkPassword(MyTextUtils.getEditTextString(binding.etAccounts),
                            MyTextUtils.getEditTextString(binding.etNewPassword),
                            MyTextUtils.getEditTextString(binding.etNewPassword2),
                            code);
                }
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel!=null){
            viewModel.destroy();
        }
    }
}
