package com.puxiang.mall.module.my.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityMyCouponBinding;
import com.puxiang.mall.module.my.adapter.CouponAdapter;
import com.puxiang.mall.module.my.model.CouponViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2017/12/25.
 */

public class CouponActivity extends BaseBindActivity {
    private ActivityMyCouponBinding binding;
    private CouponViewModel viewModel;
    private CouponAdapter adapter;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_coupon);
        adapter = new CouponAdapter(R.layout.item_coupon);
        viewModel = new CouponViewModel(this, adapter);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("我的优惠券");
        setWhiteTitle(binding.toolbar);
        initRV(binding.rvCoupon);
    }

    private void initRV(RecyclerView rvCoupon) {
        rvCoupon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCoupon.setAdapter(adapter);
        RecycleViewUtils.setEmptyView(adapter, rvCoupon, LayoutInflater.from(this), "还没有领取优惠券");
        rvCoupon.addOnItemTouchListener(viewModel.onItemTouchListener());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {

    }
}
