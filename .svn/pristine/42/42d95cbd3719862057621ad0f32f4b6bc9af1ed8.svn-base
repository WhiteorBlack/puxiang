package com.puxiang.mall.module.userinfo.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivitySexBinding;
import com.puxiang.mall.module.userinfo.viewmodel.SexViewModel;

public class SexActivity extends BaseBindActivity {
    private int sex;
    private ActivitySexBinding binding;
    private SexViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sex);
        viewModel = new SexViewModel(this);
    }

    @Override
    public void initView() {
        initData();
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }


    private void initData() {
        sex = getIntent().getIntExtra("info", 3);
        binding.toolbar.setTitle("个人信息");
        binding.toolbar.setBtnName("保存");

        switch (sex) {
            case 0:
                binding.rgSexSelect.check(R.id.rb_woman);
                break;
            case 1:
                binding.rgSexSelect.check(R.id.rb_man);
                break;
            case 2:
                binding.rgSexSelect.check(R.id.rb_unknown);
                break;
        }

        binding.rgSexSelect.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_woman:
                    sex = 0;
                    break;
                case R.id.rb_man:
                    sex = 1;
                    break;
                case R.id.rb_unknown:
                    sex = 2;
                    break;
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_toolbar_btn:
                viewModel.modifySex(sex + "");
                break;
        }
    }
}
