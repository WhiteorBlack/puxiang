package com.puxiang.mall.module.login.view;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityLoginNewBinding;
import com.puxiang.mall.module.shop.adapter.SimpleFragmentAdapter;
import com.puxiang.mall.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2017/10/30.
 */

public class LoginActivity extends BaseBindActivity {
    private ActivityLoginNewBinding binding;
    private String[] titles = new String[]{"登录", "注册"};

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_new);
    }

    @Override
    public void initView() {
        binding.tbLogin.setupWithViewPager(binding.viewpager);
        binding.viewpager.setAdapter(new SimpleFragmentAdapter(getSupportFragmentManager(), getFragments(), titles));
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LoginFragment());
        fragments.add(new RegisterFragment());
        return fragments;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {

    }
}
