package com.puxiang.mall.module.userinfo.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.module.login.view.LoginFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.puxiang.mall.utils.StringUtil.getString;

public class ForgetActivityViewModel extends BaseObservable implements ViewModel {

    private final BaseBindActivity activity;
    public ObservableBoolean isCounting = new ObservableBoolean();
    public ObservableBoolean isShowBar = new ObservableBoolean();
    public ObservableField<String> msg = new ObservableField<>("获取验证码");
    private Map<String, String> map;

    public ForgetActivityViewModel(BaseBindActivity activity) {
        EventBus.getDefault().register(this);
        this.activity = activity;
    }

    /**
     * 校验手机号码合法性
     *
     * @param accounts 手机号码
     */
    public void checkMobile(String accounts) {
        isCounting.notifyChange();
        boolean isEmpty = StringUtil.isEmpty(accounts);
        if (isEmpty) {
            ToastUtil.toast("账号不能为空");
        } else if (!StringUtil.isPhoneNumberValid(accounts)) {
            ToastUtil.toast(getString(R.string.phoneNumberInvalid));
        } else {
            if (map == null) {
                map = new HashMap<>();
            }
            if (StringUtil.isEmpty(map.get(accounts))) {
                ApiWrapper.getInstance()
                        .checkMobile(accounts)
                        .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new NetworkSubscriber<String>() {
                            @Override
                            public void onSuccess(String s) {
                                if (s.equals("1")) {
                                    getSmsCode(accounts);
                                } else {
                                    map.put(accounts, "0");
                                    ToastUtil.toast("该手机号码未注册");
                                }
                            }
                        });
            } else {
                ToastUtil.toast("该手机号码未注册");
            }
        }
    }

    /**
     * 获取短信验证码
     *
     * @param accounts 手机号码
     */
    private void getSmsCode(String accounts) {
        ApiWrapper.getInstance().getSmsCode(accounts)
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
                        ForgetRequest.isStart = true;
                    }
                });
    }

    /**
     * 修改密码
     *
     * @param account     手机号码
     * @param code        短信验证码
     * @param newPassword 新密码
     */
    private void modifyPassword(String account, String code, String newPassword) {
        ApiWrapper.getInstance()
                .modifyLoginPassword(account, code, newPassword)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        isShowBar.set(false);
                    }

                    @Override
                    public void onSuccess(String bean) {
                        ToastUtil.toast("修改成功");
                        isShowBar.set(false);
                        SettingsViewModel.logOut();
                        MyApplication.mCache.remove(CacheKey.PASSWORD_MD5);
                        EventBus.getDefault().post(Event.FINISH);
                        activity.startActivity(new Intent(activity, LoginFragment.class));
                        activity.onBackPressed();
                    }
                });
    }

    /**
     * 事件订阅
     *
     * @param smsTime
     */
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

    /**
     * 修改密码校验
     *
     * @param accounts     手机号码
     * @param newPassword1 新密码1
     * @param newPassword2 新密码2
     * @param code         短信验证码
     */
    public void checkPassword(String accounts, String newPassword1, String newPassword2, String code) {
        boolean accountsEmpty = StringUtil.isEmpty(accounts);
        boolean password1Empty = StringUtil.isEmpty(newPassword1);
        boolean password2Empty = StringUtil.isEmpty(newPassword2);
        if (accountsEmpty || password1Empty || password2Empty) {
            ToastUtil.toast(getString(R.string.verify_null_tip));
        } else if (!StringUtil.isPhoneNumberValid(accounts)) {
            ToastUtil.toast(getString(R.string.phoneNumberInvalid));
        } else {
            if (newPassword1.length() < 6) {
                ToastUtil.toast(getString(R.string.verify_password_length_tip));
                return;
            }
            if (!newPassword1.equals(newPassword2)) {
                ToastUtil.toast(getString(R.string.verify_password_tip));
                return;
            }
            isShowBar.set(true);
            Observable.timer(1500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> modifyPassword(accounts, code, newPassword2));
        }
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
