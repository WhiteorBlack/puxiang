package com.puxiang.mall.module.userinfo.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityNickBinding;
import com.puxiang.mall.module.userinfo.viewmodel.NameViewModel;

public class NickActivity extends BaseBindActivity {
    private ActivityNickBinding binding;
    private NameViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nick);
        viewModel = new NameViewModel(this);
        binding.setViewModel(viewModel);
    }

    public void initView() {
        binding.etInfo.setHint("请输入昵称");
        binding.toolbar.setTitle("个人信息");
        binding.toolbar.setBtnName("保存");
        binding.toolbar.setBackSrc(R.mipmap.nav_back_g);
        binding.toolbar.setColor(R.color.white);
        binding.toolbar.setTextColor(R.color.text_black);
        setBarHeight(binding.toolbar.ivBar);
    }

    public void onClick(View view) {
        closeInput();
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_toolbar_btn:
                viewModel.modifyNick();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }
}
