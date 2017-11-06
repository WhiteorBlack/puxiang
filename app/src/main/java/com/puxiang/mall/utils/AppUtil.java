package com.puxiang.mall.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;


/**
 */

public class AppUtil {


    public static Drawable getDrawable(int resId) {
        return resId == 0 ? null : ContextCompat.getDrawable(MyApplication.getContext(), resId);
    }

    public static int getColor(int resId) {
        return ContextCompat.getColor(MyApplication.getContext(), resId == 0 ? R.color.transparent : resId);
    }

    public static int getColor(String colorString) {
        return getColor(colorString, R.color.transparent);
    }

    public static int getColor(String colorString, int defaultColorRes) {
        return StringUtil.isEmpty(colorString) || !colorString.startsWith("#") ?
                getColor(defaultColorRes == 0 ? R.color.transparent : defaultColorRes) : Color.parseColor(colorString);
    }

    public static float getDimension(int resId) {
        return MyApplication.getContext().getResources().getDimension(resId);
    }

    public static float getDimensionPixelSize(int resId) {
        return MyApplication.getContext().getResources().getDimensionPixelSize(resId);
    }



    public static boolean isDebugMode() {
        boolean debugAble = false;
        ApplicationInfo appInfo = getApplicationInfo();
        if (appInfo != null) {
            debugAble = (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) > 0;
        }
        return debugAble;
    }

    public static Bundle getMetadata() {
        ApplicationInfo appInfo = getApplicationInfo();
        if (appInfo != null) {
            return appInfo.metaData;
        }
        return null;
    }

    public static ApplicationInfo getApplicationInfo() {
        ApplicationInfo appInfo = null;
        try {
            appInfo = MyApplication.getContext().getPackageManager().getApplicationInfo(MyApplication.getContext().getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("get ApplicationInfo error!");
        }
        return appInfo;
    }

    public static void showKeyBoard(View view) {
        view.requestFocus();
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        //延迟500，为了更好的加载activity
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager mgr = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(view, 0);
            }
        }, 500);
    }

    public static void showSoftInput(View view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);
    }

    public static void hideKeyBoard(View view) {
        view.clearFocus();
        InputMethodManager mgr = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //弱引用的Handler,防止内存泄露
    public static class UnLeakHandler extends Handler {
        private final WeakReference<Context> context;
        public UnLeakHandler(Context context) {
            this.context = new WeakReference<>(context);
        }
    }

    //精确到小数点后几位,不四舍五入,直接废弃后面的值
    public static double getDecimalValue(double value, int count) {
        double newCount = Math.pow(10, count);
        return (int) (value * (int) newCount) / newCount;
    }
}
