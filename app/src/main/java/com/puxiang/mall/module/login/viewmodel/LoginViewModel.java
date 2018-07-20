package com.puxiang.mall.module.login.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.HttpResult;
import com.puxiang.mall.model.data.RxAuthorizeUserInfo;
import com.puxiang.mall.model.data.RxMyUserInfo;
import com.puxiang.mall.module.main.view.MainActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.MD5_Utils;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import static com.puxiang.mall.utils.StringUtil.getString;


public class LoginViewModel extends BaseObservable implements ViewModel {
    private final BaseBindActivity activity;
    private BaseBindFragment fragment;
    private final LoadingWindow loadingWindow;
    public ObservableField<String> account = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>();
    public ObservableBoolean isLogging = new ObservableBoolean();
    public ObservableBoolean checkSelection = new ObservableBoolean();
    public UMShareAPI mShareAPI;

    public LoginViewModel(BaseBindFragment fragment) {
        EventBus.getDefault().register(this);
        this.fragment = fragment;
        activity = (BaseBindActivity) fragment.getActivity();
        loadingWindow = new LoadingWindow(activity);
    }

    /**
     * 接收事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.KILL_LOGIN) {
            activity.onBackPressed();
        } else if (i == Event.HID_WINDOW) {
            loadingWindow.hidWindow();
        } else if (i == Event.KILL_LOGIN_DELAYED) {
            MainActivity.handler.postDelayed(activity::onBackPressed, 500);
        }
    }

    public void setAccount(String account) {
        this.account.set(account);
        notifyPropertyChanged(BR.account);
    }

    @Bindable
    public String getAccount() {
        return this.account.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getPassword() {
        return this.password.get();
    }

    /**
     * 微信授权回调
     *
     * @param resp
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SendAuth.Resp resp) {
        int errCode = resp.errCode;
        if (errCode == BaseResp.ErrCode.ERR_OK) {
            String code = resp.code;
            wechatAuthLogin(code);
        } else {
            ToastUtil.toast("授权失败！");
            loadingWindow.hidWindow();
        }
    }

    /**
     * 微信授权登录
     *
     * @param code
     */
    private void wechatAuthLogin(String code) {
        ApiWrapper.getInstance().wechatAuthorize(code)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .doOnTerminate(loadingWindow::hidWindow)
                .subscribe(new NetworkSubscriber<RxAuthorizeUserInfo>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(RxAuthorizeUserInfo bean) {
                        saveInfo(bean);
                        activity.onBackPressed();
                    }
                });
    }

    /**
     * 发送登录请求
     */
    public void login() {
        checkLogin();
    }

    /**
     * 登录信息合法性校验
     */
    private void checkLogin() {
        final String accountStr = account.get();
        final String passwordStr = password.get();
        boolean accountsEmpty = StringUtil.isEmpty(accountStr);
        boolean passwordEmpty = StringUtil.isEmpty(passwordStr);
        if (accountsEmpty || passwordEmpty) {
            ToastUtil.toast(getString(R.string.verify_null_tip));
        } else {
            isLogging.set(true);
            startLogin(accountStr, passwordStr);
        }
    }

    /**
     * 延时0.5秒，提升用户登录体验
     *
     * @param accountStr  帐号
     * @param passwordStr 密码
     */
    private void startLogin(String accountStr, String passwordStr) {
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> loginRequest(accountStr, passwordStr));
    }

    /**
     * 登录请求
     *
     * @param account  帐号
     * @param password 密码
     */
    private void loginRequest(final String account, final String password) {
        loadingWindow.showWindow();
        ApiWrapper.getInstance()
                .login(account, password)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> {
                    isLogging.set(false);
                    loadingWindow.hidWindow();
                })
                .subscribe(new NetworkSubscriber<HttpResult<RxMyUserInfo>>() {
                    @Override
                    public void onSuccess(HttpResult<RxMyUserInfo> bean) {
                        if (bean.isSuccess()) {
                            MyApplication.mCache.saveAccount(account, password, bean);
                            getRongToken(bean.getReturnObject().getUserId(), bean.getToken());
                            activity.onBackPressed();
                            notifyRefresh();
                        }
                        String errorMessage = bean.getErrorMessage();
                        if (!StringUtil.isEmpty(errorMessage)) {
                            ToastUtil.toast(errorMessage);
                        }
                    }
                });
    }


    private void getRongToken(String userId, String token) {
        ApiWrapper.getInstance()
                .getRongToken(userId, token)
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                        MyApplication.mCache.put(CacheKey.RONG_TOKEN, data);
                        connect(data);
                    }
                });
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在  init 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(String token) {

        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {

            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e("rongClient---"+errorCode.getMessage());
            }
        });
    }


    /**
     * 获取本地缓存数据
     */
    private void getCacheData() {
        MyApplication.mCache.getAsString(CacheKey.ACCOUNTS, accountsStr -> {
            if (!StringUtil.isEmpty(accountsStr)) {
                account.set(accountsStr);
                checkSelection.set(true);

            }
        });

        MyApplication.mCache.getAsString(CacheKey.PASSWORD_MD5, passwordMd5 -> {
            String passwordStr = MD5_Utils.JM(passwordMd5);
            if (!StringUtil.isEmpty(passwordStr)) {
                password.set(passwordStr);
            }
        });
    }


    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
        loadingWindow.dismiss();
    }

    /**
     * 微信授权
     */
    public void authQQ() {
        if (mShareAPI == null) {
            mShareAPI = UMShareAPI.get(activity);
        }
        if (mShareAPI.isInstall(activity, SHARE_MEDIA.QQ)) {
            loadingWindow.showWindow();
            mShareAPI.getPlatformInfo(activity, SHARE_MEDIA.QQ, umAuthListener);
        } else {
            ToastUtil.toast("请安装QQ客户端");
        }
    }

    /**
     * 微博授权
     */
    public void authWeibo() {
        if (mShareAPI == null) {
            mShareAPI = UMShareAPI.get(activity);
        }
        if (mShareAPI.isInstall(activity, SHARE_MEDIA.SINA)) {
            loadingWindow.showWindow();
            mShareAPI.getPlatformInfo(activity, SHARE_MEDIA.SINA, umAuthListener);
        } else {
            ToastUtil.toast("请安装新浪微博客户端");
        }
    }

    /**
     * 微信授权
     */
    public void authWechat() {
        if (mShareAPI == null) {
            mShareAPI = UMShareAPI.get(activity);
        }
        if (mShareAPI.isInstall(activity, SHARE_MEDIA.WEIXIN)) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            IWXAPI api = WXAPIFactory.createWXAPI(activity, Config.WX_APP_ID);
            api.sendReq(req);
            loadingWindow.showWindow();
        } else {
            ToastUtil.toast("请安装微信客户端");
        }
    }

    /**
     * 授权回调
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (platform == SHARE_MEDIA.QQ) {
                qqAuthLogin(data);
            } else if (platform == SHARE_MEDIA.SINA) {
                weiboAuthLogin(data);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            loadingWindow.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            loadingWindow.dismiss();
            ToastUtil.toast("已取消登录");
        }
    };

    /**
     * 微博授权登录
     *
     * @param data 授权信息
     */
    private void weiboAuthLogin(Map<String, String> data) {
        ApiWrapper.getInstance().weiboAuthorize(data)
                .doOnTerminate(loadingWindow::hidWindow)
                .subscribe(new NetworkSubscriber<RxAuthorizeUserInfo>() {
                    @Override
                    public void onSuccess(RxAuthorizeUserInfo bean) {
                        saveInfo(bean);
                        activity.onBackPressed();
                    }
                });
    }

    /**
     * qq授权登录请求
     *
     * @param data 授权信息
     */
    private void qqAuthLogin(Map<String, String> data) {
        ApiWrapper.getInstance().qqAuthorize(data)
                .doOnTerminate(loadingWindow::hidWindow)
                .subscribe(new NetworkSubscriber<RxAuthorizeUserInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(RxAuthorizeUserInfo bean) {
                        saveInfo(bean);
                        activity.onBackPressed();
                    }
                });
    }

    /**
     * 保存用户信息
     *
     * @param rxAuthorizeUserInfo
     */
    private void saveInfo(RxAuthorizeUserInfo rxAuthorizeUserInfo) {
        if (rxAuthorizeUserInfo.getState() == 0) {
            ActivityUtil.startBindingMobileActivity(activity);
        } else {
            MyApplication.mCache.saveInfo(rxAuthorizeUserInfo.getUserInfo(), rxAuthorizeUserInfo.getToken());
            notifyRefresh();
        }
    }

    public void onClick(View view) {
        fragment.closeInput();
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_forget:
                ActivityUtil.startForgetActivity(activity);
                break;
            case R.id.iv_wechat:
                authWechat();
                break;
            case R.id.iv_qq:
                authQQ();
                break;
            case R.id.iv_weibo:
//                authWeibo();
                break;
            case R.id.iv_account_clear:
                setAccount("");
                break;
            case R.id.iv_pwd_clear:
                setPassword("");
                break;
        }
    }

    public TextWatcher accountWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            setAccount(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            setPassword(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void notifyRefresh() {
        EventBus.getDefault().post(Event.LOGIN_REFRESH);
    }
}
