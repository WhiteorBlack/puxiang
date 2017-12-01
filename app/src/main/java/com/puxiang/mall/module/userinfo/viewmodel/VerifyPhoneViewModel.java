package com.puxiang.mall.module.userinfo.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.model.data.RxMyUserInfo;
import com.puxiang.mall.model.data.RxTicket;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhaoyong bai on 2017/11/25.
 */

public class VerifyPhoneViewModel implements ViewModel {
    private BaseBindActivity activity;
    public ObservableField<String> phone = new ObservableField<>();
    public ObservableBoolean isCounting = new ObservableBoolean();
    public ObservableField<String> msg = new ObservableField<>("获取验证码");

    public VerifyPhoneViewModel(BaseBindActivity activity) {
        this.activity = activity;
        getInfo();
        EventBus.getDefault().register(this);
    }


    /**
     * 请求网络，获取用户信息
     */
    public void getInfo() {
//        ApiWrapper.getInstance()
//                .getMyUserInfo()
//                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(new NetworkSubscriber<RxMyUserInfo>() {
//                    @Override
//                    public void onSuccess(RxMyUserInfo bean) {
//                        userInfo = bean;
//                        phone.set(bean.getMobile());
//                    }
//                });
        String phone = activity.getIntent().getStringExtra("phone");
        this.phone.set(phone);
    }

    public void verifyPhone(String code) {
        if (TextUtils.isEmpty(code)) {
            ToastUtil.toast("请输入正确的验证码");
            return;
        }

        ApiWrapper.getInstance()
                .checkOldMobile(code)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxTicket>() {
                    @Override
                    public void onSuccess(RxTicket data) {
                        ActivityUtil.startChangePhoneActivity(activity, data.getTicket());
                        ForgetRequest.cancelCodeTime();
                        ForgetRequest.stopCount();
                        activity.onBackPressed();
                    }
                });

    }

    /**
     * 获取验证码
     */
    public void getSmsCode() {
        if (TextUtils.isEmpty(phone.get())) {
            return;
        }
        ForgetRequest.isStart = true;
        ForgetRequest.smsCodeTime();
        isCounting.set(true);
        ApiWrapper.getInstance()
                .getSmsCode(phone.get())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        isCounting.set(false);
                    }

                    @Override
                    public void onSuccess(String bean) {

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ForgetRequest.SmsTime smsTime) {
        int time = smsTime.getTime();
        if (time == 60) {
            isCounting.set(false);
            msg.set("重新发送");
        } else {
            isCounting.set(true);
            msg.set(time + "秒后重发");
        }
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
