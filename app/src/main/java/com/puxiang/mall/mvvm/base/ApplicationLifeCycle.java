package com.puxiang.mall.mvvm.base;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by zhaoyong bai on 2017/11/3.
 */

public interface ApplicationLifeCycle {
    void onCreate();

    void onLowMemory();

    void onTrimMemory(int var1);

    void onTerminate();

    void onConfigurationChanged(Configuration var1);

    void onBaseContextAttached(Context var1);
}
