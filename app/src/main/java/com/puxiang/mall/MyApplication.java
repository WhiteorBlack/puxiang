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

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.module.my.model.ObMessageState;
import com.puxiang.mall.mvvm.base.ApplicationLike;
import com.puxiang.mall.service.InitializeService;
import com.puxiang.mall.utils.ACache;
import com.puxiang.mall.utils.StringUtil;

public class MyApplication extends ApplicationLike {
    private Application application;

    public MyApplication(Application application, int tinkerFlags,
                         boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                         long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent);
    }

    public MyApplication(Application application) {
        super(application);
    }

    public static String INFO = "";
    public static int heightPixels;
    public static int widthPixels;
    public static String TOKEN = "";
    public static String USER_ID = "";
    public static String RONG_TOKEN = "";
    public static String SHOP_ID = "";
    public static ACache mCache;
    private static Context context;
    public static String info = "";

    public static boolean isFirst = false;
    public static boolean isInit = false;
    public static boolean isHotFix = false;
    public static CloudPushService pushService;

    public static ObservableBoolean isLoginOB = new ObservableBoolean();
    public static ObservableBoolean isRefreshing = new ObservableBoolean(false);
    public static ObMessageState messageState = new ObMessageState();

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
        init();
        InitializeService.start(this.getContext(), InitializeService.ACTION_INIT_WHEN_APP_CREATE);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    private void init() {
        initNative();
        initThirdParty();
    }


    private void initNative() {
        context = application.getApplicationContext();
        mCache = ACache.get(application);
        initCacheData();
        initPixels();
    }

    private void initThirdParty() {
        initFresco();
        initCloudChannel(getContext());
    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {

                pushService.bindAccount(USER_ID, new CommonCallback() {
                    @Override
                    public void onSuccess(String s) {
                    }

                    @Override
                    public void onFailed(String s, String s1) {

                    }
                });
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
            }
        });
        // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
        MiPushRegister.register(applicationContext, "", "");
// 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
        HuaWeiRegister.register(applicationContext);
    }

    //获取屏幕宽高像素
    private void initPixels() {
        widthPixels = application.getResources().getDisplayMetrics().widthPixels;
        heightPixels = application.getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean isLogin() {
        return !StringUtil.isEmpty(MyApplication.TOKEN);
    }


    public static Context getContext() {
        return context;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Fresco.getImagePipeline().clearMemoryCaches();
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
        SHOP_ID = mCache.getAsString(CacheKey.SHOP_ID);
        messageState.setIsDealer(mCache.getAsBoolean(CacheKey.ISDEALER));
        messageState.setIsMember(mCache.getAsBoolean(CacheKey.ISMEMBER));
        messageState.setIsSeller(mCache.getAsBoolean(CacheKey.ISSELLER));
    }


    /**
     * 图片框架Fresco 初始化配置
     */
    private void initFresco() {
        int MAX_MEM = 30 * ByteConstants.MB;
        ProgressiveJpegConfig pjpegConfig = new SimpleProgressiveJpegConfig();
        MemoryCacheParams params = new MemoryCacheParams(MAX_MEM, Integer.MAX_VALUE, MAX_MEM, Integer.MAX_VALUE, Integer.MAX_VALUE);
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return params;
            }
        };
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(application)
                .setDownsampleEnabled(true)
                .setProgressiveJpegConfig(pjpegConfig)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)
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
