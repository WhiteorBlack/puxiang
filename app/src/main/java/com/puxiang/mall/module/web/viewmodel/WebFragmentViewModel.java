package com.puxiang.mall.module.web.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxIntegralProduct;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.module.emotion.viewmodel.adapter.ShareBottomDialog;
import com.puxiang.mall.module.post.model.ShareInfo;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.WebUtil;
import com.puxiang.mall.utils.location.LocationUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observable;

import static com.puxiang.mall.utils.WebUtil.verifyUrlSuffixed;

public class WebFragmentViewModel implements ViewModel {
    private final LoadingWindow loadingWindow;
    private final BaseBindFragment fragment;
    public ObservableField<String> loadUrl = new ObservableField<>();
    private String TAG = "WebFragmentViewModel";
    public ObservableBoolean isError = new ObservableBoolean(false);
    public ObservableBoolean init = new ObservableBoolean(false);
    private BaseBindActivity activity;
    private ShareBottomDialog shareDialog;
    private String url = "";
    private boolean isLoading;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.RELOAD || i == Event.RELOAD_WEB) {
            WebUtil.syncCookie(url);
            if (loadUrl.get().equals(url)) {
                loadUrl.notifyChange();
            } else {
                loadUrl.set(url);
            }
        }
    }


    public WebFragmentViewModel(BaseBindFragment fragment) {
        EventBus.getDefault().register(this);
        this.fragment = fragment;
        this.activity = ((BaseBindActivity) fragment.getActivity());
        loadingWindow = new LoadingWindow(activity);
        isError.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(android.databinding.Observable sender, int propertyId) {
                boolean b = ((ObservableBoolean) sender).get();
                Log.e(TAG, "onPropertyChanged: isError" + b);
                if (!b) activity.hidNoneView();
            }
        });
    }

    public void initData() {
        String url = fragment.getArguments().getString(WebUtil.URL);
        if (!StringUtil.isEmpty(url)) {
            if (url.contains(URLs.HTML_GAMING_HALL_KEY)) {
                Location location = LocationUtil.getLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Log.e(TAG, "initData: " + latitude + "  longitude: " + longitude);
                    url = String.format(url, latitude, longitude);
                }
            }
            Log.e(TAG, "initData: " + url);
            WebUtil.syncCookie(url);
            loadUrl.set(verifyUrlSuffixed(url));
        }
    }


    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
        if (loadingWindow != null) {
            loadingWindow.dismiss();
        }
    }

    public WebViewClient getWebViewClient() {
        return new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!isLoading) {
                    isLoading = true;
                    // 设置是否阻塞图片加载
                    view.getSettings().setBlockNetworkImage(true);
                    Log.e(TAG, "onPageStarted: " + url);
                    WebFragmentViewModel.this.url = url;
                    isError.set(false);
                }
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                super.onPageFinished(view, url);
                isLoading = false;
                if (view.getSettings().getLoadsImagesAutomatically()) {
                    view.getSettings().setLoadsImagesAutomatically(true);
                }
                loadingWindow.hidWindow();
                fragment.refreshOK();
                // 设置是否阻塞图片加载
                view.getSettings().setBlockNetworkImage(false);
                Log.e(TAG, "onPageFinished: " + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e(TAG, "onReceivedError: " + 1);
                isError.set(true);
                //    errorShow.showNoneView("当前网络不可用~", v -> loadUrl.notifyChange());
            }

//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                Log.e(TAG, "onReceivedError: " + 2);
//                isError.set(true);
//                errorShow.showNoneView("当前网络不可用~", v -> loadUrl.notifyChange());
//            }

            @Override //网页加载 禁止在浏览器打开在本应用打开
            public boolean shouldOverrideUrlLoading(WebView web, String url) {
                if (url.contains(URLs.HTML_GAMING_HALL_KEY)) {
                    loadUrl.set(url + "&time=" + System.currentTimeMillis());
                } else {
                    WebUtil.jumpWeb(url, activity);
                }
                return true;
            }
        };
    }

    /**
     * 获取分享信息
     */
    public void getShareInfo() {
        String shareUrl = loadUrl.get().replaceAll("from=app", "from=html");
        loadingWindow.showWindow();
        if (shareUrl.contains(URLs.HTML_GOODS_KEY)) {
            shareProduct(shareUrl);
        } else if (shareUrl.contains(URLs.HTML_EXCHANGE_DETAIL_KEY)) {
            shareIntegralProduct(shareUrl);
        } else if (shareUrl.contains(URLs.HTML_LOTTERY_NEW_KEY)) {
            shareLottery(shareUrl);
        } else if (StringUtil.isContains(shareUrl, URLs.HTML_GAMING_HALL_KEY, URLs.HTML_GHALL_INFO_KEY, URLs
                .HTML_GHALL_DETAIL_KEY)) {
            shareLink(shareUrl);
        }
    }


    /**
     * 分享链接
     */
    private void shareLink(String rawUrl) {
        ApiWrapper.getInstance().getShareUrl(rawUrl)
                .doOnTerminate(loadingWindow::hidWindow)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String shareUrl) {
                        Log.e(TAG, "onSuccess: " + shareUrl);
                        ShareInfo shareInfo = new ShareInfo(shareUrl, "福利多多送，来转一转试试运气吧~", "", rawUrl);
                        share(shareInfo);
                    }
                });
    }

    /**
     * 分享每日一转活动
     *
     * @param s
     */
    private void shareLottery(String s) {
        String rawUrl = s.replaceAll("_share", "");
        ApiWrapper.getInstance().getShareUrl(rawUrl)
                .doOnTerminate(loadingWindow::hidWindow)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String shareUrl) {
                        Log.e(TAG, "onSuccess: " + shareUrl);
                        ShareInfo shareInfo = new ShareInfo(shareUrl, "福利多多送，来转一转试试运气吧~", "", rawUrl);
                        share(shareInfo);
                    }
                });
    }

    /**
     * 分享商品
     *
     * @param rawUrl
     */
    private void shareProduct(String rawUrl) {
        String id = StringUtil.getUrlValue(rawUrl, "productId=");
        Observable.zip(getProduct(id), getShareUrl(rawUrl), (product, shareUrl) -> {
            String title = "我在《硕虎娱乐》发现了 " + product.getName();
            String picture = product.getPicture();
            return new ShareInfo(shareUrl, title, picture, rawUrl);
        })
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(loadingWindow::hidWindow)
                .subscribe(new NetworkSubscriber<ShareInfo>() {
                    @Override
                    public void onSuccess(ShareInfo shareInfo) {
                        share(shareInfo);
                    }
                });
    }

    /**
     * 分享积分商品
     *
     * @param url
     */
    private void shareIntegralProduct(String url) {
        String id = StringUtil.getUrlValue(url, "pid=");
        Observable.zip(getIntegralProduct(id), getShareUrl(url), (integralProduct, shareUrl) -> {
            String title = "我在《硕虎娱乐》发现了 " + integralProduct.getProductIntroduce();
            String imgUrl = integralProduct.getProductMainPicUrl();
            return new ShareInfo(shareUrl, title, imgUrl, url);
        })
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(loadingWindow::hidWindow)
                .subscribe(new NetworkSubscriber<ShareInfo>() {
                    @Override
                    public void onSuccess(ShareInfo shareInfo) {
                        share(shareInfo);
                    }
                });
    }

    public void share(ShareInfo shareInfo) {
        if (shareInfo != null) {
            if (shareDialog == null) {
                shareDialog = new ShareBottomDialog(activity, shareInfo);
            } else {
                shareDialog.setShareInfo(shareInfo);
            }
            shareDialog.show();
        }
    }


    private Observable<String> getShareUrl(final String url) {
        return ApiWrapper.getInstance().getShareUrl(url);
    }

    private Observable<RxProduct> getProduct(final String id) {
        return ApiWrapper.getInstance().getProduct(id);
    }

    private Observable<RxIntegralProduct> getIntegralProduct(final String id) {
        return ApiWrapper.getInstance().getIntegralProduct(id);
    }

}
