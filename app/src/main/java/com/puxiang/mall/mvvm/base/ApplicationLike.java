package com.puxiang.mall.mvvm.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;

/**
 * Created by zhaoyong bai on 2017/11/3.
 */

public abstract class ApplicationLike implements ApplicationLifeCycle {
    private final Application application;
    private Intent tinkerResultIntent;
    private long applicationStartElapsedTime;
    private long applicationStartMillisTime;
    private int tinkerFlags;
    private boolean tinkerLoadVerifyFlag;


    public ApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                           long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        this.application = application;
        this.tinkerFlags = tinkerFlags;
        this.tinkerLoadVerifyFlag = tinkerLoadVerifyFlag;
        this.applicationStartElapsedTime = applicationStartElapsedTime;
        this.applicationStartMillisTime = applicationStartMillisTime;
        this.tinkerResultIntent = tinkerResultIntent;
    }

    public ApplicationLike(Application application) {
        this.application = application;
    }

    public Application getApplication() {
        return this.application;
    }


    public void onCreate() {
    }

    public void onLowMemory() {
    }

    public void onTrimMemory(int level) {
    }

    public void onTerminate() {
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public void onBaseContextAttached(Context base) {
    }

    public Resources getResources(Resources resources) {
        return resources;
    }

    public ClassLoader getClassLoader(ClassLoader classLoader) {
        return classLoader;
    }

    public AssetManager getAssets(AssetManager assetManager) {
        return assetManager;
    }

    public Object getSystemService(String name, Object service) {
        return service;
    }

    public Context getBaseContext(Context base) {
        return base;
    }
}
