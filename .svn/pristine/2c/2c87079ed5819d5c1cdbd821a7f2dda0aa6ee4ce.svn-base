package com.puxiang.mall.module.pay.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityPayBinding;
import com.puxiang.mall.module.pay.viewmodel.PayViewModel;

public class PayActivity extends BaseBindActivity {

    private ActivityPayBinding binding;
    private PayViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pay);
        viewModel = new PayViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("确认支付");
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_aliPay:
                viewModel.aliPay();
                break;
            case R.id.ll_wxPay:
                viewModel.getWeixinPayInfo();
                break;
            case R.id.ll_unionPay:
                /*************************************************
                 * 步骤1：从网络开始,获取交易流水号即TN
                 ************************************************/
                viewModel.unionpayPay();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        viewModel.showPayResult(data);
    }

}
