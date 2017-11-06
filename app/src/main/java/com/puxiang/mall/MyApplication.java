package com.puxiang.mall;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.module.my.model.ObMessageState;
import com.puxiang.mall.mvvm.base.ApplicationLike;
import com.puxiang.mall.network.ApiService;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.service.InitializeService;
import com.puxiang.mall.utils.ACache;
import com.puxiang.mall.utils.StringUtil;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.bugly.beta.Beta;
import com.umeng.analytics.MobclickAgent;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class MyApplication extends ApplicationLike {
    private Application application;

    public MyApplication(Application application) {
        super(application);
    }

    public static String INFO = "";
    public static int heightPixels;
    public static int widthPixels;
    public static String TOKEN = "";
    public static String USER_ID = "";
    public static String RONG_TOKEN = "";
    public static ACache mCache;
    private static Context context;

    public static String info = "";
    public static int i = 1;

    private static final String XF_APPID = "58b69a7e";
    private static final String BUGLY_APPID = "7af15a29f3";
    public static boolean isFirst = false;
    public static boolean isInit = false;
    public static boolean isHotFix = false;

    public static ObservableBoolean isLoginOB = new ObservableBoolean();


    public static ObMessageState messageState = new ObMessageState(); //暂时把框架拉起来，其他先不考虑

    @Override
    public void onCreate() {
        super.onCreate();
        application = getApplication();
        isLoginOB.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean b = ((ObservableBoolean) sender).get();
                if (!b) {
                    messageState.clearMessage();
                }
            }
        });
        Bugly.init(application, BUGLY_APPID, false);
        init();
        InitializeService.start(MyApplication.getContext(), InitializeService.ACTION_INIT_WHEN_APP_CREATE);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
//        MultiDex.install(base);

        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
//        Beta.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    private void init() {
        initNative();
        initThirdParty();
        // initExceptionHandler();
    }

    private void initExceptionHandler() {
        if (Config.isIsOnline()) {
            Thread.setDefaultUncaughtExceptionHandler(new MyUnCaughtExceptionHandler());
        }
    }

    private void initNative() {
        context = application.getApplicationContext();
        mCache = ACache.get(application);
        initCacheData();
        initPixels();
    }

    private void initThirdParty() {
        initFresco();
//        initLeakCanary();
    }


    //获取屏幕宽高像素
    private void initPixels() {
        widthPixels = application.getResources().getDisplayMetrics().widthPixels;
        heightPixels = application.getResources().getDisplayMetrics().heightPixels;
    }

    private void initLeakCanary() {
        if (!Config.isIsOnline()) {
            if (LeakCanary.isInAnalyzerProcess(application)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(application);
        }
    }

    public static boolean isLogin() {
        return !StringUtil.isEmpty(MyApplication.TOKEN);
    }

    private class MyUnCaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            Logger.e("UncaughtException", ex.getMessage());
            MobclickAgent.reportError(getContext(), ex);
            // do some work here
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    public static Context getContext() {
        return context;
    }


    /**
     * 初始化本地缓存策略
     */
    private void initCacheData() {
        //  MD5_Utils.test();
        String userId = mCache.getAsString(CacheKey.USER_ID);
        String token = mCache.getAsString(CacheKey.TOKEN);
        String info = mCache.getAsString(CacheKey.INFO);
        String rongToken = mCache.getAsString(CacheKey.RONG_TOKEN);
        // Log.e(TAG, "onCreate: " + userId + "----" + token);
        if (!StringUtil.isEmpty(userId)) {
            USER_ID = userId;
        } else {
            USER_ID = "";
        }
        if (!StringUtil.isEmpty(token)) {
            TOKEN = token;
            isLoginOB.set(true);
        } else {
            TOKEN = "";
        }
        if (!StringUtil.isEmpty(token)) {
            INFO = info;
        } else {
            INFO = "";
        }

        if (TextUtils.isEmpty(rongToken)) {
            RONG_TOKEN = "";
        } else {
            RONG_TOKEN = rongToken;
        }
    }


    /**
     * 图片框架Fresco 初始化配置
     */
    private void initFresco() {
        ProgressiveJpegConfig pjpegConfig = new SimpleProgressiveJpegConfig();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(application)
                .setDownsampleEnabled(true)
                .setProgressiveJpegConfig(pjpegConfig)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build();
        Fresco.initialize(application, config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Fresco.shutDown();
        isInit = false;
    }
}
