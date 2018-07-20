package com.puxiang.mall.module.web.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.MiddlewareWebClientBase;
import com.just.agentweb.PermissionInterceptor;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityNewWebviewBinding;
import com.puxiang.mall.module.web.viewmodel.WebViewModelNew;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;

import cn.finalteam.galleryfinal.GalleryFinal;

/**
 * Created by zhaoyong bai on 2018/4/28.
 */
public class WebViewNew extends BaseBindActivity {
    private ActivityNewWebviewBinding binding;
    protected AgentWeb mAgentWeb;
    private WebViewModelNew viewModel;
    private String url;
    private BaseBindActivity activity;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void initBind() {
        activity = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_webview);
        viewModel = new WebViewModelNew(this);
        binding.setViewModel(viewModel);
        initAgentWeb();
    }

    private void initAgentWeb() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(binding.llParent, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(getResources().getColor(R.color.mall_activity), 1)
                .setWebChromeClient(mWebChromeClient)
                .setPermissionInterceptor(mPermissionInterceptor)
                .setWebViewClient(viewModel.getWebViewClient(this))
                .addJavascriptInterface("puxiang", new JsToAndroid())
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
                .setSecurityType(AgentWeb.SecurityType.DEFAULT_CHECK)
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .go(url);
//        initSetting(mAgentWeb.getAgentWebSettings().getWebSettings());
    }

    private void initSetting(WebSettings agentWebSettings) {
        WebUtil.getWebSettings(agentWebSettings);
    }


    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            return false;
        }
    };

    private IAgentWebSettings getSetting() {

        return new IAgentWebSettings() {
            @Override
            public IAgentWebSettings toSetting(WebView webView) {
                return null;
            }

            @Override
            public WebSettings getWebSettings() {
                return WebUtil.getWebSettings(mAgentWeb.getWebCreator().getWebView());
            }
        };
    }


    private String getUrl() {
        return url;
    }

    @SuppressLint("ResourceAsColor")
    public void setUrl(String url) {
        this.url = WebUtil.verifyUrlSuffixed(url);
        if (mAgentWeb != null) {
            mAgentWeb.getUrlLoader().loadUrl(url);
        } else {
            initAgentWeb();
        }

    }

    @Override
    public void initView() {
        viewModel.initData();
    }


    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    };

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (mAgentWeb.getWebCreator().getWebView().canGoBack()) {
                    goBack();
                } else {
                    onBackPressed();
                }
                break;
            case R.id.v_icon:
                viewModel.getShareInfo();
                break;
            case R.id.btn_text:
                if (viewModel.url.contains(URLs.HTML_MY_GOOD_ADDRESS_KEY)) {
                    if (viewModel.loadUrl.get().equals(URLs.HTML_MY_GOOD_ADDRESS_ADD)) {
                        viewModel.loadUrl.notifyChange();
                    } else {
                        viewModel.loadUrl.set(URLs.HTML_MY_GOOD_ADDRESS_ADD);
                    }
                } else if (viewModel.url.contains(URLs.HTML_MY_GOOD_ADDRESS_MODIFY_KEY)) {
                    viewModel.deleteAddress();
                } else if (viewModel.url.contains(URLs.HTML_EXCHANGE_PAGE_KEY)) {
                    if (viewModel.loadUrl.get().equals(URLs.HTML_EXCHANGE_RULE)) {
                        viewModel.loadUrl.notifyChange();
                    } else {
                        viewModel.loadUrl.set(URLs.HTML_EXCHANGE_RULE);
                    }
                }
        }
    }

    public void goBack() {
        String url = mAgentWeb.getWebCreator().getWebView().getUrl();
        if (url.contains(URLs.HTML_ORDER_DEALER_COMMIT) || url.contains(URLs.HTML_MY_ORDER) || url.contains(URLs.HTML_MY_RETURN_ORDER)) {
            onBackPressed();
        } else {
            mAgentWeb.back();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mAgentWeb.getWebLifeCycle().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAgentWeb.getWebLifeCycle().onResume();
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) {
            viewModel.destroy();
        }
        GalleryFinal.clearCallback();
        GalleryFinal.cleanCacheFile();
        mAgentWeb.clearWebCache();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

    private void setResult(String data) {
        Intent intent = new Intent();
        intent.putExtra("data", data);
        this.setResult(RESULT_OK, intent);
        finish();
    }

    private class JsToAndroid {

        @JavascriptInterface
        public void back() {
            finish();
        }

        /**
         * 刷新 native 数据
         *
         * @param data
         */
        @JavascriptInterface
        public void refresh(String data) {
            setResult(data);
        }
    }
}
