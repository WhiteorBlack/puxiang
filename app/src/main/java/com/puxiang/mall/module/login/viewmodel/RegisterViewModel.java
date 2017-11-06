package com.puxiang.mall.module.login.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.HttpResult;
import com.puxiang.mall.model.data.RxMyUserInfo;
import com.puxiang.mall.module.login.view.RegisterFragment;
import com.puxiang.mall.module.main.view.MainActivity;
import com.puxiang.mall.module.userinfo.viewmodel.ForgetRequest;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ACache;
import com.puxiang.mall.utils.MyTextUtils;
import com.puxiang.mall.utils.ScreenUtil;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.puxiang.mall.utils.StringUtil.getString;


public class RegisterViewModel extends BaseObservable implements ViewModel {
    private final Activity activity;
    private RegisterFragment fragment;
    private HashMap<String, String> map;
    public ObservableBoolean isCounting = new ObservableBoolean();
    public ObservableField<String> msg = new ObservableField<>("获取验证码");
    public ObservableBoolean isShowBar = new ObservableBoolean();

    public ObservableField<String> account = new ObservableField<>();
    public ObservableField<String> smsCode = new ObservableField<>();
    public ObservableField<String> password1 = new ObservableField<>();
    public ObservableField<String> password2 = new ObservableField<>();
    public ObservableBoolean isChecked = new ObservableBoolean(true);

    public RegisterViewModel(RegisterFragment fragment) {
        EventBus.getDefault().register(this);
        this.fragment = fragment;
        this.activity = fragment.getActivity();
    }

    /**
     * 验证输入信息
     */
    public void verifyInput() {

        String accountValue = account.get();
        boolean accountEmpty = StringUtil.isEmpty(accountValue);
        String passwordValue1 = password1.get();
        boolean passwordEmpty1 = StringUtil.isEmpty(passwordValue1);
        String passwordValue2 = password2.get();
        boolean passwordEmpty2 = StringUtil.isEmpty(passwordValue2);

        if (accountEmpty || passwordEmpty1 || passwordEmpty2) {
            ToastUtil.toast(getString(R.string.verify_null_tip));
        } else if (!isChecked.get()) {
            ToastUtil.toast(getString(R.string.no_select_agreement_tips));

        } else if (!StringUtil.isPhoneNumberValid(accountValue)) {
            ToastUtil.toast(getString(R.string.phoneNumberInvalid));
        } else if (passwordValue1.length() < 6) {
            ToastUtil.toast(getString(R.string.verify_password_length_tip));
        } else if (!passwordValue1.equals(passwordValue2)) {
            ToastUtil.toast(getString(R.string.verify_password_tip));
        } else {
            // this.password = MD5_Utils.MD5_16(password1);
            isShowBar.set(true);
            MainActivity.handler.postDelayed(() -> registerRequest(accountValue, passwordValue1,
                    smsCode.get()), 1500);
        }
    }

    /**
     * 注册请求
     *
     * @param account
     * @param password
     * @param code
     */
    private void registerRequest(final String account, final String password, final String code) {
        ApiWrapper.getInstance().register(account, code, password,
                ScreenUtil.getUniquePsuedoID())
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .doOnTerminate(() -> isShowBar.set(false))
                .flatMap(new Function<String, Observable<HttpResult<RxMyUserInfo>>>() {
                    @Override
                    public Observable<HttpResult<RxMyUserInfo>> apply(@NonNull String s) throws
                            Exception {
                        return login(account, password);
                    }
                })
                .subscribe(new NetworkSubscriber<HttpResult<RxMyUserInfo>>() {
                    @Override
                    public void onSuccess(HttpResult<RxMyUserInfo> bean) {
                        if (bean.isSuccess()) {
                            ACache.saveAccount(account, password, bean);
                            EventBus.getDefault().post(Event.KILL_LOGIN);
                            activity.onBackPressed();
                        }
                        String errorMessage = bean.getErrorMessage();
                        if (!StringUtil.isEmpty(errorMessage)) {
                            ToastUtil.toast(errorMessage);
                        }
                    }
                });
    }

    /**
     * 登录请求体
     *
     * @param account  帐号
     * @param password 密码
     * @return 请求体
     */
    private Observable<HttpResult<RxMyUserInfo>> login(String account, String password) {
        return ApiWrapper.getInstance().login(account, password)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 验证手机号码
     */
    public void checkMobile(String account) {
        boolean accountEmpty = StringUtil.isEmpty(account);
        if (accountEmpty) {
            ToastUtil.toast("账号不能为空");
            setIsCounting(false);
        } else if (!StringUtil.isPhoneNumberValid(account)) {
            ToastUtil.toast(getString(R.string.phoneNumberInvalid));
            setIsCounting(false);
        } else {
            if (map == null) {
                map = new HashMap<>();
            }
            if (StringUtil.isEmpty(map.get(account))) {
                ApiWrapper.getInstance()
                        .checkMobile(account)
                        .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                        .subscribe(new NetworkSubscriber<String>() {
                            @Override
                            public void onSuccess(String s) {
                                if (s.equals("1")) {
                                    map.put(account, "1");
                                } else {
                                    getSmsCode(account);
                                }
                            }
                        });
            } else {
                setIsCounting(false);
                ToastUtil.toast("该手机号码已经被注册");
            }
        }
    }

    /**
     * 获取验证码
     *
     * @param mobile 手机号码
     */
    private void getSmsCode(final String mobile) {
        Logger.e("send---"+mobile);
        ApiWrapper.getInstance()
                .getSmsCode(mobile)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        isCounting.set(false);
                    }

                    @Override
                    public void onSuccess(String bean) {
                       ToastUtil.toast("发送成功");
                        ForgetRequest.smsCodeTime();
                        ForgetRequest.isStart = true;
                        isCounting.set(true);
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ForgetRequest.SmsTime smsTime) {
        int time = smsTime.getTime();
        if (time == 60) {
            setIsCounting(false);
            setMsg("重新发送");
        } else {
            setIsCounting(true);
            setMsg(time + "秒后重发");
        }
    }

    public void onClick(View view) {
        fragment.closeInput();
        switch (view.getId()) {
            case R.id.cb_sms:
                if (getIsCounting()) {
                    notifyPropertyChanged(BR.isCounting);
                    return;
                }
                checkMobile(getAccount());
                break;
            case R.id.btn_register:
                if (StringUtil.isEmpty(smsCode.get())) {
                    ToastUtil.toast("请输入验证码");
                } else {
                    verifyInput();
                }
                break;
            case R.id.tv_agreement:
                WebUtil.jumpWeb(URLs.AGREEMENT, activity);
                break;
            case R.id.iv_account_clear:
                setAccount("");
                break;
            case R.id.iv_code_clear:
                setMsg("");
                break;
            case R.id.iv_pwd_clear:
                setPassword1("");
                break;
            case R.id.iv_pwd2_clear:
                setPassword2("");
                break;
        }
    }

    @Bindable
    public boolean getIsCounting() {
        return isCounting.get();
    }

    public void setIsCounting(boolean isCounting) {
        this.isCounting.set(isCounting);
        notifyPropertyChanged(BR.isCounting);
    }

    @Bindable
    public String getMsg() {
        return msg.get();
    }

    public void setMsg(String msg) {
        this.msg.set(msg);
        notifyPropertyChanged(BR.msg);
    }

    @Bindable
    public boolean getIsShowBar() {
        return isShowBar.get();
    }

    public void setIsShowBar(boolean isShowBar) {
        this.isShowBar.set(isShowBar);
        notifyPropertyChanged(BR.isShowBar);
    }

    @Bindable
    public String getAccount() {
        return account.get();
    }

    public void setAccount(String account) {
        this.account.set(account);
        notifyPropertyChanged(BR.account);
    }

    @Bindable
    public String getSmsCode() {
        return smsCode.get();
    }

    public void setSmsCode(String smsCode) {
        this.smsCode.set(smsCode);
        notifyPropertyChanged(BR.smsCode);
    }

    @Bindable
    public String getPassword1() {
        return password1.get();
    }

    public void setPassword1(String password1) {
        this.password1.set(password1);
        notifyPropertyChanged(BR.password1);
    }

    @Bindable
    public String getPassword2() {
        return password2.get();
    }

    public void setPassword2(String password2) {
        this.password2.set(password2);
        notifyPropertyChanged(BR.password2);
    }

    @Override
    public void destroy() {

        EventBus.getDefault().unregister(this);
    }
}
