package com.puxiang.mall.module.pay.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.databinding.ActivityPayResultBinding;
import com.puxiang.mall.module.pay.viewmodel.PayResultViewModel;
import com.puxiang.mall.utils.ActivityUtil;

import org.greenrobot.eventbus.EventBus;

public class PayResultActivity extends BaseBindActivity {

    private ActivityPayResultBinding binding;
    private PayResultViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pay_result);
        viewModel = new PayResultViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("支付结果");
        EventBus.getDefault().post(Event.FINISH);
        EventBus.getDefault().post(Event.RELOAD_WEB);
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
            case R.id.btn_pay_result:
                viewModel.depart();
                break;
            case R.id.btn_goHome_:
                EventBus.getDefault().post(Event.GO_HOME);
                ActivityUtil.startMainActivity(this);
                break;
        }
    }
}
