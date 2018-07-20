package com.puxiang.mall.module.seller.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivitySellerNotifyBinding;
import com.puxiang.mall.utils.ActivityUtil;

/**
 * Created by zhaoyong bai on 2017/12/18.
 */

public class SellerNotifyActivity extends BaseBindActivity {
    private ActivitySellerNotifyBinding binding;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_seller_notify);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("温馨提示");
        binding.toolbar.setTextColor(R.color.text_black);
        binding.toolbar.setColor(R.color.white);
        binding.toolbar.setBackSrc(R.mipmap.nav_back_g);
        setBarHeight(binding.toolbar.ivBar);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.btn_go_home:
                onBackPressed();
                break;
            case R.id.tv_check:

                break;
            case R.id.btn_to_dealer:
                ActivityUtil.startApplySellerActivity(this);
//                onBackPressed();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {

    }
}
