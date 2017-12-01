package com.puxiang.mall.module.login.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxAuthorizeUserInfo;
import com.puxiang.mall.module.main.view.MainActivity;
import com.puxiang.mall.module.userinfo.viewmodel.ForgetRequest;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ACache;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.puxiang.mall.utils.StringUtil.getString;


public class BindingMobileViewModel implements ViewModel {
    private final BaseBindActivity activity;
    public ObservableBoolean isCounting = new ObservableBoolean();
    public ObservableField<String> msg = new ObservableField<>("获取验证码");
    public ObservableBoolean isShowBar = new ObservableBoolean();

    public BindingMobileViewModel(BaseBindActivity activity) {
        EventBus.getDefault().register(this);
        this.activity = activity;
    }

    /**
     * 验证手机号码
     */
    public void checkMobile(String account) {
        if (StringUtil.isEmpty(account)) {
            ToastUtil.toast("手机号码不能为空");
        } else {
            if (!StringUtil.isPhoneNumberValid(account)) {
                ToastUtil.toast(getString(R.string.phoneNumberInvalid));
            } else {
                getSmsCode(account);
            }
        }
    }

    /**
     * 验证输入信息
     */
    public void verifyInput(String accounts, String smsCode) {
        if (StringUtil.isEmpty(accounts)) {
            ToastUtil.toast("手机号码不能为空");
        } else if (!StringUtil.isPhoneNumberValid(accounts)) {
            ToastUtil.toast(getString(R.string.phoneNumberInvalid));
        } else {
            isShowBar.set(true);
            binding(accounts,smsCode);
//            MainActivity.handler.postDelayed(() -> binding(accounts, smsCode), 1500);

        }
    }

    /**
     * 绑定手机
     *
     * @param accounts
     * @param code
     */
    private void binding(String accounts, String code) {
        ApiWrapper.getInstance()
                .bindingMobile(code, accounts)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> isShowBar.set(false))
                .subscribe(new NetworkSubscriber<RxAuthorizeUserInfo>() {
                    @Override
                    public void onSuccess(RxAuthorizeUserInfo bean) {
                        ACache.saveInfo(bean.getUserInfo(), bean.getToken());
                        EventBus.getDefault().post(Event.KILL_LOGIN);
                        activity.onBackPressed();
                    }
                });
    }


    /**
     * 获取验证码
     *
     * @param mobile 手机号码
     */
    private void getSmsCode(final String mobile) {
        ForgetRequest.isStart = true;
        ApiWrapper.getInstance()
                .getSmsCode(mobile)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        isCounting.set(false);
                    }

                    @Override
                    public void onSuccess(String bean) {
                        ForgetRequest.smsCodeTime();
                        isCounting.set(true);
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
