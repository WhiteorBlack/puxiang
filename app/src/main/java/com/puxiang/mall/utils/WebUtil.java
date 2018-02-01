package com.puxiang.mall.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.module.login.view.LoginActivity;
import com.puxiang.mall.module.web.view.WebActivity;
import com.puxiang.mall.module.web.view.WebWithSearchActivity;
import com.puxiang.mall.module.web.viewmodel.WebViewModel;
import com.puxiang.mall.module.web.viewmodel.WebWithSearchViewModel;
import com.puxiang.mall.network.URLs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebUtil {

    public final static String TYPE = "TYPE";
    public final static String TYPE_POST = "TYPE_POST";
    public final static String TYPE_PLATE = "TYPE_PLATE";
    public final static String URL = "url";
    public final static String POST_ID = "POST_ID";

    /**
     * init WebView Settings
     */
    public static void initWebViewSettings(WebView webView, WebViewClient webViewClient) {
        webSettings(webView);
        setWebChrome(webView);
        setWebClient(webView, webViewClient);
    }

    /**
     * init WebView Settings
     */
    public static void initWebViewSettings(WebView webView, WebViewClient webViewClient, WebViewModel viewModel) {
        webSettings(webView);
        setWebChrome(webView, viewModel);
        setWebClient(webView, webViewClient);
    }

    /**
     * init WebView Settings
     */
    public static void initWebViewSettings(WebView webView, WebViewClient webViewClient, WebWithSearchViewModel viewModel) {
        webSettings(webView);
        setWebChrome(webView, viewModel);
        setWebClient(webView, webViewClient);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private static void webSettings(WebView webView) {
        WebSettings settings = webView.getSettings();
        // >= 19(SDK4.4)启动硬件加速，否则启动软件加速
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        settings.setLoadsImagesAutomatically(true); //支持自动加载图片
        settings.setUseWideViewPort(true); //将图片调整到适合WebView的大小
        settings.setLoadWithOverviewMode(true); //自适应屏幕
        // 设置可以访问文件
        CookieManager.getInstance().setAcceptCookie(true);

        settings.setAllowFileAccess(true);
        webView.getSettings().setSupportMultipleWindows(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);
       /* web.addJavascriptInterface(new JavaScriptinterface(WebActivity.this),
                "android");*/
        settings.setUserAgentString("Android");
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setSaveFormData(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        settings.setDatabaseEnabled(true);
        settings.setSupportZoom(true); //支持缩放
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); // 取消滚动条白边效果
        webView.requestFocus();
       /* web.addJavascriptInterface(new JavaScriptinterface(WebActivity.this),
                "android");*/

    }

    private static void setWebClient(WebView webView, WebViewClient webViewClient) {
        webView.setWebViewClient(webViewClient);
    }

    private static void setWebChrome(WebView webView) {
        WebChromeClient chromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }
        };
        webView.setWebChromeClient(chromeClient);
    }

    private static void setWebChrome(WebView webView, WebViewModel viewModel) {
        WebChromeClient chromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress>0&&newProgress<100) {
                    viewModel.webProgress.set(newProgress);
                } else {
                    viewModel.webProgress.set(-1);
                }
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }
        };
        webView.setWebChromeClient(chromeClient);
    }

    private static void setWebChrome(WebView webView, WebWithSearchViewModel viewModel) {
        WebChromeClient chromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress>0&&newProgress<100) {
                    viewModel.webProgress.set(newProgress);
                } else {
                    viewModel.webProgress.set(-1);
                }
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }
        };
        webView.setWebChromeClient(chromeClient);
    }

    /**
     * 跳转到商品详情
     *
     * @param context
     * @param id
     */
    public static void jumpGoodsWeb(Context context, String id) {
        Intent intent = new Intent(context, WebActivity.class);
        String url = URLs.HTML_GOODS + "&productId=" + id;
        intent.putExtra(URL, url);
        intent.putExtra("productId", id);
        context.startActivity(intent);
    }

    /**
     * 跳转到进货商品详情
     *
     * @param context
     * @param id
     */
    public static void jumpGoodsWeb(Context context, String id, String type) {
        Intent intent = new Intent(context, WebActivity.class);
        String url = URLs.HTML_GOODS + "&productId=" + id + "&type=" + type;
        intent.putExtra(URL, url);
        intent.putExtra("productId", id);
        context.startActivity(intent);
    }

    /**
     * 跳转到店铺详情
     *
     * @param context
     * @param id
     */
    public static void jumpShopWeb(Context context, String id) {
        Intent intent = new Intent(context, WebActivity.class);
        String url = URLs.HTML_SHOP + "&shopId=" + id;
        intent.putExtra(URL, url);
        intent.putExtra("shopId", id);
        context.startActivity(intent);
    }

    public static void jumpMyWeb(String url, Context context) {

        Intent intent;
        if (StringUtil.isEmpty(MyApplication.TOKEN)) {
            intent = new Intent(context, LoginActivity.class);
        } else {
            intent = new Intent(context, WebActivity.class);
            intent.putExtra(URL, url);
        }
        context.startActivity(intent);
    }

    public static void jumpWeb(String url, Context context) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(URL, verifyUrlSuffixed(url));
        context.startActivity(intent);
    }

    public static void jumpWebWithSearch(String url, Context context) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent(context, WebWithSearchActivity.class);
        intent.putExtra(URL, verifyUrlSuffixed(url));
        context.startActivity(intent);
    }

    public static String verifyUrlSuffixed(String url) {
        if (!StringUtil.isEmpty(url)) {
            if (!url.contains("?")) {
                url = url + "?from=app";
            } else if (!url.contains("from")) {
                url = url + "&from=app";
            }
            if (!url.contains("userId") && !TextUtils.isEmpty(MyApplication.USER_ID)) {
                url += "&userId=" + MyApplication.USER_ID;
            }

            if (!url.contains("token") && !TextUtils.isEmpty(MyApplication.TOKEN)) {
                url += "&token=" + MyApplication.TOKEN;
            }
        }
        return url;
    }


//    public static void clearCookies(Context context) {
//        // Edge case: an illegal state exception is thrown if an instance of
//        // CookieSyncManager has not be created.  CookieSyncManager is normally
//        // created by a WebKit view, but this might happen if you start the
//        // app, restore saved state, and click logout before running a UI
//        // dialog in a WebView -- in which case the app crashes
//        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.removeSessionCookie();
//        cookieManager.removeAllCookie();
//    }

//    public static void removeCookie(Context context) {
//        CookieSyncManager.createInstance(context);
//        CookieSyncManager.getInstance().startSync();
//        CookieManager.getInstance().removeSessionCookie();
//        CookieManager.getInstance().removeAllCookie();
//        String cookie = CookieManager.getInstance().getCookie("http://wap.esomic.com/");
//        Log.e("222", "removeCookie: " + cookie );

    //    }
  /*  public static void jumpGoodsWeb(Context context, int id) {
        String url = URLs.HTML_GOODS;
        Intent intent = new Intent(context, WebActivity.class);
        url = url + "&productId=" + id;
        Log.e("url ", "jumpGoodsWeb: " + url);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void jumpMyWeb(String url, Context context) {

        Intent intent = null;
        if (MyApplication.TOKEN == null) {
            intent = new Intent(context, LoginActivity.class);
        } else {
            intent = new Intent(context, WebActivity.class);
        }
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
*/
    /*public static void synCookies(Context context, String url, String userInfo) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        String state = "$state={token:" + MyApplication.TOKEN + "}";
        Log.e("tag", "synCookies: " + state);
        cookieManager.setCookie(url, state);
        String info = "$info=" + userInfo;
        Log.e("tag", "synCookies: " + info);
        cookieManager.setCookie(url, info);
        CookieSyncManager.getInstance().sync();
    }*/

    public static void removeCookie() {
        CookieSyncManager.createInstance(MyApplication.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

//    /**
//     * Sync Cookie
//     */
//    public static void syncCookie(Context context, String url) {
//        try {
//            Log.d("Nat: webView.syncCookie.url", url);
//            CookieSyncManager.createInstance(context);
//
//            CookieManager cookieManager = CookieManager.getInstance();
//            cookieManager.setAcceptCookie(true);
//            cookieManager.removeSessionCookie();// 移除
//            cookieManager.removeAllCookie();
//            SystemClock.sleep(500);
//            String oldCookie = cookieManager.getCookie(url);
//            if (oldCookie != null) {
//                Log.d("Nat: webView.syncCookieOutter.oldCookie", oldCookie);
//            }
//
//
//            //String state = "state={token:" + MyApplication.TOKEN + "};Domain=.esomic.com" + ";Path=/";
//            String state = "state={\"token\":\"" + MyApplication.TOKEN + "\"}";
//            Log.e("tag", "synCookies: " + state);
//            String encode = URLEncoder.encode(MyApplication.INFO);
//            cookieManager.setCookie(url, "info=" + encode);
//            cookieManager.setCookie(url, state + ";Domain=.esomic.com;Path=/");
//            Log.e("tag", "synCookies: " + encode + ";Domain=.esomic.com;Path=/");
//            CookieSyncManager.getInstance().sync();
//
//            String newCookie = cookieManager.getCookie(url);
//            Log.e("newCookie", "newCookie: " + newCookie);
//            if (newCookie != null) {
//                Log.d("Nat: webView.syncCookie.newCookie", newCookie);
//            }
//        } catch (Exception e) {
//            Log.e("Nat: webView.syncCookie failed", e.toString());
//        }
//    }

    public static boolean syncCookie(String url) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            CookieSyncManager.createInstance(context);
//        }
        if (StringUtil.isEmpty(MyApplication.TOKEN)) {
            removeCookie();
            return false;
        }

        String state = "state={\"token\":\"" + MyApplication.TOKEN + "\"};Domain=" + Config.HTML_HOST + ";Path=/";
        String encode = "";
        try {
            encode = URLEncoder.encode(MyApplication.INFO, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String info = "info=" + encode + ";Domain=" + Config.HTML_HOST + ";Path=/";

        CookieSyncManager.createInstance(MyApplication.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, info);
        cookieManager.setCookie(url, state);
        //  cookieManager.setCookie(url, cookie);//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
        String newCookie = cookieManager.getCookie(url);
        Log.e("tag", "syncCookie: " + newCookie);
        return !StringUtil.isEmpty(newCookie);
    }

}
