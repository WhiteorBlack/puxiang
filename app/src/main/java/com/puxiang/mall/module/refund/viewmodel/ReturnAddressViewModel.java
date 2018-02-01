package com.puxiang.mall.module.refund.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.text.TextUtils;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhaoyong bai on 2017/12/27.
 */

public class ReturnAddressViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;

    private String postName = "";
    private String postNo = "";
    private String postCost = "";
    private String postDate = "";
    private String refundApplicationId;
    public ObservableBoolean isLogging = new ObservableBoolean(false);

    public ReturnAddressViewModel(BaseBindActivity activity) {
        this.activity = activity;
        refundApplicationId = activity.getIntent().getStringExtra("reId");
    }


    @Bindable
    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    @Bindable
    public String getPostNo() {
        return postNo;
    }

    public void setPostNo(String postNo) {
        this.postNo = postNo;
    }

    @Bindable
    public String getPostCost() {
        return postCost;
    }

    public void setPostCost(String postCost) {
        this.postCost = postCost;
    }

    @Bindable
    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                verifyData();

                break;
        }
    }

    private void commitData() {
        isLogging.set(true);
        ApiWrapper.getInstance()
                .addLogistics(refundApplicationId, postName, postNo, postCost, postDate)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                        EventBus.getDefault().post(Event.RELOAD);
                        activity.onBackPressed();
                    }
                });
    }

    private void verifyData() {
        if (TextUtils.isEmpty(postCost)) {
            ToastUtil.toast("请输入运费价格");
            return;
        }

        if (TextUtils.isEmpty(postDate)) {
            ToastUtil.toast("请输入发货时间");
            return;
        }

        if (TextUtils.isEmpty(postName)) {
            ToastUtil.toast("请输入快递公司名称");
            return;
        }

        if (TextUtils.isEmpty(postNo)) {
            ToastUtil.toast("请输入快递单号");
            return;
        }
        commitData();
    }

    @Override
    public void destroy() {

    }
}
