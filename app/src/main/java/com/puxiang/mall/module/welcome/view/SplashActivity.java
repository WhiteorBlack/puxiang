package com.puxiang.mall.module.welcome.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.databinding.ActivitySplashBinding;
import com.puxiang.mall.module.welcome.viewmodel.SplashViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.SharedPreferences;
import com.puxiang.mall.utils.WebUtil;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.module.main.view.MainActivity;
import com.gyf.barlibrary.ImmersionBar;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class SplashActivity extends AppCompatActivity {

    /**
     * viewModel.
     */
    private SplashViewModel viewModel;
    private ActivitySplashBinding binding;
    private String ISFIRST = "isFirst";

    protected final void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout
                .activity_splash);
        viewModel = new SplashViewModel(this);
        viewModel.setIsFirst(SharedPreferences.getInstance().getBoolean(ISFIRST, true));
        WebUtil.initWebViewSettings(binding.webview, viewModel.getWebViewClient());
        binding.setViewModel(viewModel);

    }

    /**
     * 初始化融云Token并连接融云服务器
     */
    private void initRong() {
        if (MyApplication.isLogin() && !TextUtils.isEmpty(MyApplication.RONG_TOKEN)) {
            connect(MyApplication.RONG_TOKEN);
        } else {
            if (MyApplication.isLogin()) {
                getRongToken();
            }
        }
    }

    private void getRongToken() {
        ApiWrapper.getInstance()
                .getRongToken()
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                        MyApplication.mCache.put(CacheKey.RONG_TOKEN, data);
                        connect(data);
                    }
                });
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link #init(Context)} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token    从服务端获取的用户身份令牌（Token）。
     * @param callback 连接回调。
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
                Logger.e("rong","tokenIncorrect");
                getRongToken();
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Logger.e("rong", "--onSuccess" + userid);
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e("rong", "--onError" + errorCode);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        MainActivity.handler.postDelayed(() -> Intent(), Config.SPLASH_TIME);
        super.onCreate(savedInstanceState);
        initBind();
        setImmerStatue();
        initRong();
    }

    private void setImmerStatue() {
        ImmersionBar.with(this).transparentNavigationBar().init();
    }


    @Override
    protected final void onDestroy() {
        super.onDestroy();
        Fresco.getImagePipeline().evictFromMemoryCache(Uri.parse(viewModel.uriStr.get()));
    }


    @Override
    protected final void onStart() {
        super.onStart();

    }

    private void Intent() {

        if (SharedPreferences.getInstance().getBoolean(ISFIRST, true)) {
            ActivityUtil.startGuideActivity(this);
            SharedPreferences.getInstance().putBoolean(ISFIRST, false);
            finish();
        } else {
            viewModel.jumpNextPage();
        }
    }

    @Override
    public final boolean onKeyDown(final int keyCode, final KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }


}
