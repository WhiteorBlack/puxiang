package com.puxiang.mall;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.model.data.RongMessage;
import com.puxiang.mall.mvvm.base.ApplicationLike;
import com.puxiang.mall.service.InitializeService;
import com.tencent.tinker.loader.TinkerRuntimeException;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.lang.reflect.Constructor;

import io.rong.imkit.RongIM;
import io.rong.imkit.utils.SystemUtils;
import io.rong.imkit.widget.provider.RealTimeLocationMessageProvider;
import io.rong.imlib.ipc.RongExceptionHandler;



public class SampleApplication extends Application {
    private ApplicationLike applicationLike;


    @Override
    public void onCreate() {
        super.onCreate();
        applicationLike.onCreate();
//        InitializeService.start(this, InitializeService.ACTION_INIT_WHEN_APP_CREATE);
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
            throw new RuntimeException("createDelegate failed", var3);
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