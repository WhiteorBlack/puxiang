package com.puxiang.mall.module.orders.view;

import android.databinding.DataBindingUtil;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityMyOrdersBinding;

/**
 * Created by zhaoyong bai on 2017/12/14.
 */

public class MyOrdersActivity extends BaseBindActivity {
    private ActivityMyOrdersBinding binding;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_orders);
    }

    @Override
    public void initView() {
        setBarHeight(binding.toolbar.ivBar);
    }

    @Override
    protected void viewModelDestroy() {

    }
}
