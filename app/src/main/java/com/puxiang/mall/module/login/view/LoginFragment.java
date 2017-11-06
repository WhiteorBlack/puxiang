package com.puxiang.mall.module.login.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityLoginBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.login.viewmodel.LoginViewModel;
import com.puxiang.mall.utils.ActivityUtil;

public class LoginFragment extends BaseBindFragment implements EditText.OnEditorActionListener, CompoundButton.OnCheckedChangeListener {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_login, container, false);
        viewModel = new LoginViewModel(this);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding.etAccounts.setOnEditorActionListener(this);
        binding.etPassword.setOnEditorActionListener(this);
        binding.chbPass.setOnCheckedChangeListener(this);
        binding.etPassword.addTextChangedListener(viewModel.passwordWatcher);
        binding.etAccounts.addTextChangedListener(viewModel.accountWatcher);
        binding.chbLogin.setOnCheckedChangeListener(this);
    }


    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean isOK = true;
        switch (actionId) {
            case EditorInfo.IME_ACTION_NEXT:
                binding.etAccounts.clearFocus();
                binding.etPassword.requestFocus();
                break;
            case EditorInfo.IME_ACTION_DONE:
                closeInput();
                break;
            default:
                isOK = false;
                break;
        }
        return isOK;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.chb_login:

                break;
            case R.id.chb_pass:
                binding.etPassword.setTransformationMethod(!b ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
                break;
        }
    }
}
