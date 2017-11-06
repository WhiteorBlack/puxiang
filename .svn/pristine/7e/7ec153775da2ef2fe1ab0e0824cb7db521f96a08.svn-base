package com.puxiang.mall.module.pay.viewmodel;

import android.app.Activity;
import android.content.Intent;

import com.puxiang.mall.R;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.WebUtil;

public class PayResultViewModel implements ViewModel {
    private final Activity activity;
    public boolean isSucceed;
    public String resultStr;
    public String explainStr;
    public double amount;
    private String orderId;

    public PayResultViewModel(Activity activity) {
        this.activity = activity;
        getPayResult();
    }

    /**
     * 获取支付结果
     */
    private void getPayResult() {
        Intent intent = activity.getIntent();
        int result = intent.getIntExtra("result", -1);
        amount = intent.getDoubleExtra("totalPrice", -1);
        orderId = intent.getStringExtra("orderId");
        switch (result) {
            case -2:
            case -1:
                isSucceed = false;
                resultStr = StringUtil.getString(R.string.pay_fail_result_text);
                explainStr = StringUtil.getString(R.string.pay_fail_explain);
                break;
            default:
                isSucceed = true;
                resultStr = StringUtil.getString(R.string.pay_succeed_result_text);
                explainStr = StringUtil.getString(R.string.pay_succeed_explain);
                break;
        }
    }

    public void depart() {
        if (isSucceed) {
            ActivityUtil.startSearchActivity(activity, "");
        } else {
            viewOrder();
        }
        activity.finish();
    }

    private void viewOrder() {
        if (!StringUtil.isEmpty(orderId)) {
            String url = URLs.HTML_ORDER_DETAIL + orderId;
            WebUtil.jumpMyWeb(url, activity);
        }
    }

    @Override
    public void destroy() {

    }
}
