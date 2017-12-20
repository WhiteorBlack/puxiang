package com.puxiang.mall.module.web.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityWebBinding;
import com.puxiang.mall.module.main.view.MainActivity;
import com.puxiang.mall.module.scan.viewmodel.QRCodeViewModel;
import com.puxiang.mall.module.web.viewmodel.WebViewModel;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.WebUtil;
import com.umeng.socialize.UMShareAPI;

import java.io.File;

public class WebActivity extends BaseBindActivity {

    private static final String TAG = "WebActivity";
    private static final String APP_CACHE_DIRNAME = "/webcache";

    private ActivityWebBinding binding;
    private WebViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web);
    }

    @Override
    public void initView() {
        WebView webView = binding.web;
        viewModel = new WebViewModel(this);
        WebUtil.initWebViewSettings(webView, viewModel.getWebViewClient(this));
        binding.setViewModel(viewModel);
    }

    private boolean checkUrlValid() {
        Log.e(TAG, "loadUrl: " + viewModel.loadUrl.get());
        if (StringUtil.isEmpty(viewModel.loadUrl.get())) {
            View noneLayout = binding.vsNone.getViewStub().inflate();
            AutoUtils.auto(noneLayout);
            viewModel.toolBarTitle.set("出错啦！");
            ((TextView) noneLayout.findViewById(R.id.tv_none)).setText("网址错误！");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!MainActivity.isInit) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            super.onBackPressed();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (binding.web.canGoBack()) {
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

//    public void showNoneView() {
//        ViewGroup root = (ViewGroup) binding.getRoot();
//        View noneView = getLayoutInflater().inflate(R.layout.view_home_none, root, false);
//        AutoUtils.auto(noneView);
//        root.addView(noneView);
//        TextView tvNone = (TextView) noneView.findViewById(R.id.tv_none);
//        tvNone.setText("当前网络不可用");
//    }

    public void goBack() {
        String url=binding.web.getUrl();
        if (url.contains(URLs.HTML_ORDER_DEALER_COMMIT)||url.contains(URLs.HTML_MY_ORDER)||url.contains(URLs.HTML_MY_RETURN_ORDER)) {
            onBackPressed();
        } else {
            binding.web.goBack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //removeCookies(this);
        WebView webView = binding.web;
        if (webView != null) {
            webView.stopLoading();
            ViewGroup rootView = getRootView();
            if (rootView != null) {
                rootView.removeView(webView);
            }
            webView.removeAllViews();
            webView.destroy();
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {

        //清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME);
        Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");
        Log.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }


    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {
        Log.i(TAG, "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }
 /*   public class JavaScriptinterface {
        Context context;

        public JavaScriptinterface(Context c) {
            context = c;
        }

        */

    /**
     * 与js交互时用到的方法，在js里直接调用的
     *//*
        @JavascriptInterface
        public void like(String b) {
            Log.e(TAG, "like: " + b);
            if (b.equals("false")) {
                htmlLike.isLike = false;
            } else {
                htmlLike.isLike = true;
            }
            EventBus.getDefault().post(htmlLike);
        }

        @JavascriptInterface
        public void shareTitle(String title, String imgUrl) {
            if (!loadUrl.contains(URLs.HTML_MORE_COMMENT)) {
                Log.e(TAG, "shareTitle: " + title + "----" + imgUrl);
                String url = loadUrl.replaceAll("appType=android", "");
                shareInfo = new ShareInfo(url, title, imgUrl);
            }
        }

    }*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.web.canGoBack()) {
            goBack();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult:  requestCode:" + requestCode + " resultCode :" + resultCode);

        if (requestCode == ActivityUtil.ActivityRequestCode.QR_SING) {
            if (resultCode == QRCodeViewModel.RESULT_CODE_SING) {
                boolean isOK = data.getBooleanExtra(QRCodeViewModel.RESULT_FLAG, false);
                Log.e(TAG, "onActivityResult: " + isOK);
                if (isOK) {
                    viewModel.loadUrl.notifyChange();
                }
            }
        } else {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }
    }
}
