package com.puxiang.mall.module.refund.view;

import android.databinding.DataBindingUtil;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityReturnAddressBinding;
import com.puxiang.mall.module.refund.viewmodel.ReturnAddressViewModel;

/**
 * Created by zhaoyong bai on 2017/12/27.
 */

public class ReturnAddressActivity extends BaseBindActivity {
    private ReturnAddressViewModel viewModel;
    private ActivityReturnAddressBinding binding;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_return_address);
        viewModel = new ReturnAddressViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        setWhiteTitle(binding.toolbar);
        setBarHeight(binding.toolbar.ivBar);
        binding.toolbar.setTitle("填写发货信息");setBarHeight(binding.toolbar.ivBar);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
