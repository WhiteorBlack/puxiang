package com.puxiang.mall.module.integral.view;

import android.databinding.DataBindingUtil;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityIntegraShopBinding;

/**
 * Created by zhaoyong bai on 2017/11/18.
 */

public class IntegralShopActivity extends BaseBindActivity {
    private ActivityIntegraShopBinding binding;

    @Override
    protected void initBind() {
        binding= DataBindingUtil.setContentView(this, R.layout.activity_integra_shop);
    }

    @Override
    public void initView() {

    }

    @Override
    protected void viewModelDestroy() {

    }
}
