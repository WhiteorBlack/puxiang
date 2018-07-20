package com.puxiang.mall.module.web.view;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityWebSearchBinding;
import com.puxiang.mall.module.main.view.MainActivity;
import com.puxiang.mall.module.scan.viewmodel.QRCodeViewModel;
import com.puxiang.mall.module.web.viewmodel.WebViewModel;
import com.puxiang.mall.module.web.viewmodel.WebWithSearchViewModel;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.KeyBoardUtils;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.WebUtil;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WebWithSearchActivity extends BaseBindActivity implements View.OnClickListener {

    private static final String TAG = "WebWithSearchActivity";
    private static final String APP_CACHE_DIRNAME = "/webcache";

    private ActivityWebSearchBinding binding;
    private WebWithSearchViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_search);
    }

    @Override
    public void initView() {
        WebView webView = binding.web;
        viewModel = new WebWithSearchViewModel(this);
        WebUtil.initWebViewSettings(webView, viewModel.getWebViewClient(this),viewModel);
        binding.setViewModel(viewModel);
        setBarHeight(binding.toolbar.ivBar);
        setWebChrome(webView,viewModel);
    }

    private void setWebChrome(WebView webView, WebWithSearchViewModel viewModel) {
        WebChromeClient chromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 0 && newProgress < 100) {
                    viewModel.webProgress.set(newProgress);
                } else {
                    viewModel.webProgress.set(-1);
                }
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                take();
                return true;
            }


            //<3.0

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                take();
            }

            //>3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                take();
            }

            //>4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                take();
            }
        };
        webView.setWebChromeClient(chromeClient);
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
                break;
            case R.id.iv_search_btn:
                String keyword = binding.toolbar.et.getText().toString();
                viewModel.search(keyword);
                KeyBoardUtils.closeKeybord(binding.toolbar.et, this);
                break;
        }
    }

    public void goBack() {

        if (binding.web.getUrl().contains(URLs.HTML_ORDER_DEALER_COMMIT)) {
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

    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调
    private Uri imageUri;

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

        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                if (result == null) {
                    mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage = null;
                } else {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }
            }
        }
    }

    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;
        if (resultCode == RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }
        return;
    }

    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);
        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }


}
