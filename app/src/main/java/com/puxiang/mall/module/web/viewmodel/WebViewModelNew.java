package com.puxiang.mall.module.web.viewmodel;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orhanobut.logger.Logger;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.base.ErrorShow;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxIntegralProduct;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.model.data.RxSing;
import com.puxiang.mall.module.emotion.viewmodel.adapter.ShareBottomDialog;
import com.puxiang.mall.module.im.model.IMRequest;
import com.puxiang.mall.module.im.model.IMUserInfoProvider;
import com.puxiang.mall.module.post.model.ShareInfo;
import com.puxiang.mall.module.web.view.WebActivity;
import com.puxiang.mall.module.web.view.WebViewNew;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;
import com.puxiang.mall.utils.location.LocationUtil;
import com.puxiang.mall.widget.dialog.DefaultDialog;
import com.puxiang.mall.widget.dialog.MapDialog;
import com.puxiang.mall.widget.dialog.OnDialogExecuteListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.util.Map;

import io.reactivex.Observable;
import io.rong.imkit.RongIM;

import static com.puxiang.mall.utils.WebUtil.verifyUrlSuffixed;

public class WebViewModelNew implements ViewModel {
    private final LoadingWindow loadingWindow;
    private DefaultDialog dialog;
    public ObservableField<String> loadUrl = new ObservableField<>();
    public ObservableBoolean isShowIcon = new ObservableBoolean(false);
    public ObservableBoolean isShowText = new ObservableBoolean(false);
    public ObservableField<String> toolBarText = new ObservableField<>();
    public ObservableField<String> toolBarTitle = new ObservableField<>();
    public ObservableBoolean isError = new ObservableBoolean(false);
    public ObservableInt webProgress = new ObservableInt(-1);
    public String url = "";
    private String TAG = "WebViewModel";
    private WebViewNew activity;
    private boolean initOK = false;
    private ShareBottomDialog shareDialog;
    private MapDialog mapDialog;

    public WebViewModelNew(WebViewNew activity) {
        EventBus.getDefault().register(this);
        this.activity = activity;
        loadingWindow = new LoadingWindow(activity);
        initDialog();
//        initData();
        isError.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(android.databinding.Observable sender, int propertyId) {
                boolean b = ((ObservableBoolean) sender).get();
                if (!b) {
                    activity.hidNoneView();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.RELOAD || i == Event.RELOAD_WEB) {
            WebUtil.syncCookie(url);
            if (loadUrl.get().equals(url)) {
                loadUrl.notifyChange();
            } else {
                loadUrl.set(url);
            }
            activity.setUrl(url);
        } else if (i == Event.WEB_BACK) {
            activity.goBack();
        } else if (i == Event.KILL_WEB) {
            activity.finish();
        } else if (i == Event.GO_MALL) {
            activity.finish();
        } else if (i == Event.GO_HOME) {
            activity.finish();
        }
    }

    public void initData() {
        Map<String, String> extraMap = ActivityUtil.getExtraMap(activity);
        String url;
        if (extraMap != null) {
            url = extraMap.get(WebUtil.URL);
        } else {
            url = activity.getIntent().getStringExtra(WebUtil.URL);
        }
        WebUtil.syncCookie(url);
        activity.setUrl(verifyUrlSuffixed(url));
    }

    private void initDialog() {
        dialog = new DefaultDialog(activity, "是否删除该地址？", new OnDialogExecuteListener() {
            @Override
            public void execute() {
                delete();
            }

            @Override
            public void cancel() {

            }
        });
    }

    public void deleteAddress() {
        if (dialog != null)
            dialog.show();
    }

    private void delete() {
        String addressId = StringUtil.getUrlValue(loadUrl.get(), "addressId=");
        ApiWrapper.getInstance().deleteAddress(addressId)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {
                        activity.goBack();
                    }
                });
    }


    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
        if (loadingWindow != null) loadingWindow.dismiss();
    }

    private boolean isLoading = false;

    public WebViewClient getWebViewClient(ErrorShow errorShow) {
        return new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!isLoading) {
                    isLoading = true;
                    // 设置是否阻塞图片加载
                    view.getSettings().setBlockNetworkImage(true);

                    isError.set(false);
                }
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isLoading = false;
                if (view.getSettings().getLoadsImagesAutomatically()) {
                    view.getSettings().setLoadsImagesAutomatically(true);
                }
//                loadingWindow.hidWindow();
                // 设置是否阻塞图片加载
                view.getSettings().setBlockNetworkImage(false);
                WebViewModelNew.this.url = url;

                if (url.contains(URLs.HTML_GHALL_DETAIL_KEY)) {
                    try {
                        url = URLDecoder.decode(url, "UTF-8");
                        url = URLDecoder.decode(url, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String title = StringUtil.getUrlValue(url, "name=");
                    toolBarTitle.set(title);
                } else {
                    toolBarTitle.set(view.getTitle());
                }
                if (url.contains(URLs.HTML_MY_GOOD_ADDRESS_KEY)) {
                    showText("新增");
                } else if (url.contains(URLs.HTML_EXCHANGE_PAGE_KEY)) {
                    isShowIcon.set(false);
                    isShowText.set(false);
//                    showText("积分规则");
                } else if (url.contains(URLs.HTML_MY_GOOD_ADDRESS_MODIFY_KEY)) {
                    showText("删除");
                } else if (StringUtil.isContains(url, URLs.HTML_GAMING_HALL_KEY, URLs.HTML_GHALL_INFO_KEY,
                        URLs.HTML_GHALL_DETAIL_KEY, URLs.HTML_GOODS_KEY, URLs.HTML_EXCHANGE_DETAIL_KEY)) {
                    isShowIcon.set(true);
                    isShowText.set(false);
                } else if (StringUtil.isContains(url, URLs.HTML_ACTIVITY_KEY)) {
                    isShowIcon.set(true);
                    isShowText.set(false);
                } else {
                    isShowIcon.set(false);
                    isShowText.set(false);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                isError.set(true);
                errorShow.showNoneView("当前网络不可用~", v -> loadUrl.notifyChange());
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                isError.set(true);
                errorShow.showNoneView("当前网络不可用~", v -> loadUrl.notifyChange());
            }

            @Override //网页加载 禁止在浏览器打开在本应用打开
            public boolean shouldOverrideUrlLoading(WebView web, String url) {
                Logger.e("webNew--"+url);
                if (!url.contains("http://")) {
                    if (url.contains("tel:")) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                    return true;
                }
                url = WebUtil.verifyUrlSuffixed(url);
                if (url.contains(URLs.HTML_LOTTERY_NEW_KEY)) {
                    getShareInfo();
                } else if (url.contains("shop_detail.html")) {
                    //跳转到商家详情
                    try {
                        String shopId = url.substring(url.indexOf("=") + 1, url.indexOf("&"));
                        ActivityUtil.startShopDetialActivity(activity, shopId);
                    } catch (Exception e) {

                    }

                } else if (url.contains(URLs.HTML_LOGIN_KEY)) {
                    ActivityUtil.startLoginActivity(activity);
                } else if (url.contains(URLs.HTML_PAY_KEY)) {
                    String orderId = StringUtil.getUrlValue(url, "orderIds=");
                    ActivityUtil.startPayActivity(activity, orderId);
                    activity.finish();
                } else if (url.contains(URLs.CREATE_TOPIC_KEY)) {
                    if (StringUtil.isEmpty(MyApplication.TOKEN)) {
                        ActivityUtil.startLoginActivity(activity);
                    } else {
                        url = getDecoderUrl(url);
                        String plateId = StringUtil.getUrlValue(url, "plateId=");
                        String plateName = StringUtil.getUrlValue(url, "plateName=");
                        String detailId = url.substring(url.indexOf("=") + 1, url.indexOf("&"));
                        ActivityUtil.startPublishActivity(activity, plateId, detailId, plateName);
                    }
                } else if (url.contains(URLs.HTML_REFUND_KEY)) {
                    String orderDetailId = StringUtil.getUrlValue(url, "orderDetailId=");
                    String productId = StringUtil.getUrlValue(url, "productId=");
                    String tabIndex = StringUtil.getUrlValue(url, "tabIndex=");
                    ActivityUtil.startRefundActivity(activity, orderDetailId, productId, tabIndex);
                } else if (url.contains("shopping_car.html")) {
                    ActivityUtil.startShoppingActivity(activity);
                } else if (url.contains(URLs.HTML_POST_KEY)
                        || url.contains(URLs.HTML_VIDEO_KEY)) {
                    String postId = StringUtil.getUrlValue(url, "postId=");
                    EventBus.getDefault().post(Event.KILL_POST);
                    ActivityUtil.startPostDetailActivity(activity, postId);
                } else if (url.contains(URLs.HTML_MORE_COMMENT_KEY)) {
                    String commentId = StringUtil.getUrlValue(url, "commentId=");
                    ActivityUtil.startCommentActivity(activity, commentId);
                } else if (url.contains("talkToId=")) {
                    String id = StringUtil.getUrlValue(url, "talkToId=");
                    if (RongIM.getInstance() != null) {
                        IMRequest.getUserInfo(id);
                        WeakReference<Activity> wr = new WeakReference<Activity>(activity);
                        RongIM.getInstance().startPrivateChat(wr.get(), id, IMUserInfoProvider.NULL);
                    }
                } else if (url.contains(URLs.HTML_PLATE_KEY)) {
                    String plateId = StringUtil.getUrlValue(url, "plateId=");
                    ActivityUtil.startPlatePostActivity(activity, plateId);
                } else if (url.contains(URLs.HTML_INDEX_KEY)) {
                    if (MyApplication.isLogin()) {
                    } else {
                        ActivityUtil.startLoginActivity(activity);
                    }
//                }
//                else if (url.contains(URLs.HTML_INTEGRAL_KEY)) {
//                    ActivityUtil.startIntegralActivity(activity);
                } else if (url.contains(URLs.HTML_REGISTER_KEY)) {
                    ActivityUtil.startRegisterActivity(activity);
                } else if (url.contains(URLs.HTML_HOT_PLATES_KEY)) {
                    EventBus.getDefault().post(Event.GO_PLATES);
                } else if (url.contains(URLs.HTML_USER_INFO)) {
                    if (!MyApplication.isLogin()) {
                        ActivityUtil.startLoginActivity(activity);
                    } else {
                        ActivityUtil.startInfoActivity(activity);
                    }
                } else if (url.contains(URLs.HTML_USER_CENTER_KEY)) {
                    String userId = StringUtil.getUrlValue(url, "userId=");
                    ActivityUtil.startPersonalActivity(activity, userId);
                } else if (url.contains(URLs.HTML_OPEN_BDMAP_KEY)) {
                    if (mapDialog == null) {
                        url = getDecoderUrl(url);
                        String lat = StringUtil.getUrlValue(url, "lat=");
                        String lng = StringUtil.getUrlValue(url, "lng=");
                        String name = StringUtil.getUrlValue(url, "name=");
                        mapDialog = new MapDialog(activity, lat, lng, name);
                        mapDialog.show();
                    } else {
                        mapDialog.show();
                    }
                } else if (url.contains(URLs.HTML_SING_KEY)) {
                    checkCard();
                } else if (url.contains(URLs.HTML_REFUND_ADDRESS)) {
                    //跳转增加退货邮件信息
                    String reId = StringUtil.getUrlValue(url, "reID=");
                    ActivityUtil.startRefundAddressActivity(activity, reId);
                } else if (url.contains(URLs.HTML_SECKILL_PAGE_KEY)) {

                } else {
//                    loadUrl.set(url);
                    activity.setUrl(url);
                }
                return true;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

                // ------5.0以上手机执行------
                Uri uri = request.getUrl();
                String url = uri.toString();
                return shouldInterceptRequest(view, url);
            }


//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//                String newUrl = StringUtil.getNewUrl(url);
//
//                if (newUrl.equals(url)) {
//                    return super.shouldInterceptRequest(view, url);
//                }
//
//
//                Request request1 = new Request.Builder()
//                        .url(newUrl)
//                        .build();
//                Response response = null;
//                try {
//                    response = client.newCall(request1).execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                String type = Utils.getMimeType(newUrl);
//                return new WebResourceResponse(type, response.header("content-encoding", "utf-8"), response.body().byteStream());
//            }

        };
    }


    private String getDecoderUrl(String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    private void checkCard() {
        //检测GPS
        boolean isEnabled = LocationUtil.checkGPS(activity);
        if (isEnabled) {
            requestGPSPermission();
        }
    }

    private void checkLocation() {
        //获取地理位置信息
        Location location = LocationUtil.getLocation();
        double latitude;
        double longitude;
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            latitude = LocationUtil.LATITUDE;
            longitude = LocationUtil.LONGITUDE;
        }
        if (latitude == 0 && longitude == 0) {
            ToastUtil.toast("获取位置信息失败，添加的GPS权限后重试~");
        } else {
            String gid = StringUtil.getUrlValue(loadUrl.get(), "gid=");
            ApiWrapper.getInstance()
                    .checkUserInShop(gid, LocationUtil.LATITUDE, LocationUtil.LONGITUDE)
                    .subscribe(new NetworkSubscriber<RxSing>() {
                        @Override
                        public void onSuccess(RxSing data) {
                            if ("1".equals(data.getStatus())) {
                                ActivityUtil.startQRCodeActivityForResult(activity,
                                        ActivityUtil.ActivityRequestCode.QR_SING, gid, LocationUtil.LATITUDE,
                                        LocationUtil.LONGITUDE);
                            } else {
//                                ToastUtil.toast("你当前所在的位置不在电竞馆范围内，请移步到硕美科电竞馆。");
                            }
                        }
                    });
        }
    }

    /**
     * 请求文件读写权限。
     */
    private void requestGPSPermission() {
        new RxPermissions(activity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        checkLocation();
                    } else {
                        ToastUtil.toast("需要访问的GPS权限~");
                    }
                });
    }

    /**
     * 获取分享信息
     */
    public void getShareInfo() {
        String shareUrl = loadUrl.get().replaceAll("from=app", "from=html");
        if (shareUrl.contains(URLs.HTML_ACTIVITY_KEY)) {
            shareActivity(shareUrl);
        } else {

            loadingWindow.showWindow();
            if (shareUrl.contains(URLs.HTML_GOODS_KEY)) {
                shareProduct(shareUrl);
            } else if (shareUrl.contains(URLs.HTML_EXCHANGE_DETAIL_KEY)) {
                shareIntegralProduct(shareUrl);
            } else if (shareUrl.contains(URLs.HTML_LOTTERY_KEY)) {
                shareLottery(shareUrl);
            } else if (StringUtil.isContains(shareUrl, URLs.HTML_GAMING_HALL_KEY, URLs.HTML_GHALL_INFO_KEY, URLs
                    .HTML_GHALL_DETAIL_KEY)) {
                shareLink(shareUrl);
            }
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
                        ShareInfo shareInfo = new ShareInfo(shareUrl, "我在《蒲象商城》发现了 ", "", rawUrl, toolBarTitle.get());
                        share(shareInfo);
                    }
                });
    }

    private void shareActivity(String rawUrl) {
        ShareInfo shareInfo = new ShareInfo("", "我在《蒲象商城》发现了 ", "", rawUrl, toolBarTitle.get());
        share(shareInfo);
    }

    /**
     * 分享每日一转活动
     *
     * @param rawUrl
     */
    private void shareLottery(String rawUrl) {
        // String rawUrl = s.replaceAll("_share", "");
        ApiWrapper.getInstance().getShareUrl(rawUrl)
                .doOnTerminate(loadingWindow::hidWindow)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String shareUrl) {
                        ShareInfo shareInfo =
                                new ShareInfo(shareUrl, "福利多多送，来转一转试试运气吧~", "", rawUrl);
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
//        Observable.zip(getProduct(id), getShareUrl(rawUrl), (product, shareUrl) -> {
//            String title = "我在《蒲象商城》发现了 " + product.getName();
//            String picture = product.getPicture();
//            return new ShareInfo(shareUrl, title, picture, rawUrl);
//        })
//                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
//                .doOnTerminate(loadingWindow::hidWindow)
//                .subscribe(new NetworkSubscriber<ShareInfo>() {
//                    @Override
//                    public void onFail(RetrofitUtil.APIException e) {
//                        super.onFail(e);
//                    }
//
//                    @Override
//                    public void onSuccess(ShareInfo shareInfo) {
//                        share(shareInfo);
//                    }
//                });
        getProduct(id, rawUrl);
    }

    /**
     * 分享积分商品
     *
     * @param url
     */
    private void shareIntegralProduct(String url) {
        String id = StringUtil.getUrlValue(url, "pid=");
//        Observable.zip(getIntegralProduct(id), getShareUrl(url), (integralProduct, shareUrl) -> {
//            String title = "我在《蒲象商城》发现了 " + integralProduct.getProductIntroduce();
//            String imgUrl = integralProduct.getProductMainPicUrl();
//            return new ShareInfo(shareUrl, title, imgUrl, url);
//        })
//                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
//                .doOnTerminate(loadingWindow::hidWindow)
//                .subscribe(new NetworkSubscriber<ShareInfo>() {
//                    @Override
//                    public void onSuccess(ShareInfo shareInfo) {
//                        share(shareInfo);
//                    }
//                });

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

    private void getProduct(final String id, String url) {
        ApiWrapper.getInstance().getProduct(id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(loadingWindow::hidWindow)
                .subscribe(new NetworkSubscriber<RxProduct>() {
                    @Override
                    public void onSuccess(RxProduct data) {
                        String title = data.getProductName();
                        String picture = data.getMainPictureUrl();
                        share(new ShareInfo("", title, picture, url, "我在《蒲象商城》发现了 " + data.getIntroduce()));
                    }
                });
    }

    private Observable<RxIntegralProduct> getIntegralProduct(final String id) {
        return ApiWrapper.getInstance().getIntegralProduct(id);
    }

    private void showText(String text) {
        isShowText.set(true);
        isShowIcon.set(false);
        toolBarText.set(text);
    }
}
