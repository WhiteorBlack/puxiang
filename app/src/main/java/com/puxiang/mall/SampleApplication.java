package com.puxiang.mall;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.puxiang.mall.model.data.RongMessage;
import com.puxiang.mall.mvvm.base.ApplicationLike;
import com.tencent.tinker.loader.TinkerRuntimeException;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.lang.reflect.Constructor;

import io.rong.imkit.RongIM;
import io.rong.imkit.utils.SystemUtils;
import io.rong.imkit.widget.provider.RealTimeLocationMessageProvider;
import io.rong.imlib.ipc.RongExceptionHandler;

/**
 * Desc : 热更新Tinker接入，需要定义Application
 * version : v1.0
 */

public class SampleApplication extends Application {
    private ApplicationLike applicationLike;


    @Override
    public void onCreate() {
        super.onCreate();
        applicationLike.onCreate();
        RongIM.init(this);
        initRongIM();
    }

    private void initRongIM() {
        /**
         * 注意：
         *
         * IMKit SDK调用第一步 初始化
         *
         * context上下文
         *
         * 只有两个进程需要初始化，主进程和 push 进程
         */
//        RongIM.setServerInfo("nav.cn.ronghub.com", "up.qbox.me");
        RongIM.init(this,"cpj2xarlc19nn");
//        Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));

//        try {
//            RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
//            RongIM.registerMessageType(RongMessage.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.ensureDelegate();
    }

    private synchronized void ensureDelegate() {
        if (this.applicationLike == null) {
            this.applicationLike = this.createDelegate();
        }

    }

    private ApplicationLike createDelegate() {
        try {
            Class var1 = Class.forName("com.puxiang.mall.MyApplication", false, this.getClassLoader());
            Constructor var2 = var1.getConstructor(new Class[]{Application.class});
            return (ApplicationLike) var2.newInstance(new Object[]{this});
        } catch (Throwable var3) {
            throw new TinkerRuntimeException("createDelegate failed", var3);
        }
    }

    public void onTerminate() {
        super.onTerminate();
        if(this.applicationLike != null) {
            this.applicationLike.onTerminate();
        }

    }

    public void onLowMemory() {
        super.onLowMemory();
        if(this.applicationLike != null) {
            this.applicationLike.onLowMemory();
        }

    }

    @TargetApi(14)
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(this.applicationLike != null) {
            this.applicationLike.onTrimMemory(level);
        }

    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(this.applicationLike != null) {
            this.applicationLike.onConfigurationChanged(newConfig);
        }

    }

    public Resources getResources() {
        Resources var1 = super.getResources();
        return this.applicationLike != null?this.applicationLike.getResources(var1):var1;
    }

    public ClassLoader getClassLoader() {
        ClassLoader var1 = super.getClassLoader();
        return this.applicationLike != null?this.applicationLike.getClassLoader(var1):var1;
    }

    public AssetManager getAssets() {
        AssetManager var1 = super.getAssets();
        return this.applicationLike != null?this.applicationLike.getAssets(var1):var1;
    }

    public Object getSystemService(String name) {
        Object var2 = super.getSystemService(name);
        return this.applicationLike != null?this.applicationLike.getSystemService(name, var2):var2;
    }

    public Context getBaseContext() {
        Context var1 = super.getBaseContext();
        return this.applicationLike != null?this.applicationLike.getBaseContext(var1):var1;
    }

}