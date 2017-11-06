package com.puxiang.mall.module.login.view;

import android.databinding.DataBindingUtil;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityRegisterBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.login.viewmodel.RegisterViewModel;
import com.puxiang.mall.utils.MyTextUtils;

public class RegisterFragment extends BaseBindFragment implements CompoundButton.OnCheckedChangeListener {

    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_register, container, false);
        viewModel = new RegisterViewModel(this);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        MyTextUtils.setUnderline(binding.tvAgreement);
        binding.chbPass.setOnCheckedChangeListener(this);
        binding.chbPassTwo.setOnCheckedChangeListener(this);
    }

    public void setCodeError(String error) {
        binding.etCode.setError(error);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.chb_pass:
                binding.etPassword.setTransformationMethod(!b ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
                break;
            case R.id.chb_pass_two:
                binding.etPassword2.setTransformationMethod(!b ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
                break;
        }
    }
}
