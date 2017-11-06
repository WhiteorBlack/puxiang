package com.puxiang.mall.module.welcome.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.DraweeViewUtils;
import com.puxiang.mall.utils.FileUtil;
import com.puxiang.mall.utils.ScreenUtil;
import com.puxiang.mall.utils.StringUtil;

import java.io.File;

public class SplashViewModel extends BaseObservable implements ViewModel {

    private Activity activity;
    public ObservableField<String> uriStr = new ObservableField<>();
    public ObservableBoolean isLoading = new ObservableBoolean();
    public ObservableBoolean isFirst=new ObservableBoolean(true);

    public SplashViewModel(Activity activity) {
        this.activity = activity;
        getUri();
    }

    public void setIsFirst(boolean isFirst){
        this.isFirst.set(isFirst);
    }


    private void getUri() {
        String path = MyApplication.getContext().getExternalFilesDir(null) + File.separator + "myFirst.png";
        if (!FileUtil.isFileExists(path)) {
            path = DraweeViewUtils.getResUri(R.mipmap.ic_launcher);
        } else {
            path = "file://" + path;
        }
        uriStr.set(path);
    }

    public void jumpNextPage() {
        MyApplication.mCache.getAsString(CacheKey.VERSION_CODE, versionCode -> {
            int code = ActivityUtil.getVersionCode(MyApplication.getContext());
            if (!StringUtil.isEmpty(versionCode)) {
                if (code > Integer.valueOf(versionCode)) {
                    MyApplication.isFirst = true;
                }
            } else {
                putDeviceInfo(code);
                MyApplication.isFirst = true;
            }
            MyApplication.mCache.put(CacheKey.VERSION_CODE, ActivityUtil.getVersionCode(MyApplication.getContext()) +
                    "");
            ActivityUtil.startMainActivity(activity);
            activity.finish();
        });

    }

    private void putDeviceInfo(int code) {
        ApiWrapper.getInstance()
                .uploadDeviceInfo("android", code + "", ScreenUtil.getUniquePsuedoID())
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {
                    }
                });
    }


    public WebViewClient getWebViewClient() {
        return new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);


            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override //网页加载 禁止在浏览器打开在本应用打开
            public boolean shouldOverrideUrlLoading(WebView web, String url) {
                return true;
            }
        };
    }

    @Override
    public void destroy() {
    }
}
