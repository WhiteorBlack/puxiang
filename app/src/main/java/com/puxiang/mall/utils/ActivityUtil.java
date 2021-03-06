package com.puxiang.mall.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.puxiang.mall.model.data.RxClassfyRightItem;
import com.puxiang.mall.module.bbs.view.FriendActivity;
import com.puxiang.mall.module.bbs.view.PublishActivity;
import com.puxiang.mall.module.bbs.view.SelectPlateActivity;
import com.puxiang.mall.module.classify.view.ClassifyFragment;
import com.puxiang.mall.module.dealer.view.DealerBuyActivity;
import com.puxiang.mall.module.dealer.view.DealerListActivity;
import com.puxiang.mall.module.dealer.view.DealerStatueActivity;
import com.puxiang.mall.module.from.view.FromActivity;
import com.puxiang.mall.module.integral.view.IntegralActivity;
import com.puxiang.mall.module.login.view.BindingMobileActivity;
import com.puxiang.mall.module.login.view.LoginActivity;
import com.puxiang.mall.module.login.view.RegisterFragment;
import com.puxiang.mall.module.main.view.MainActivity;
import com.puxiang.mall.module.my.view.AddEditAddressActivity;
import com.puxiang.mall.module.my.view.CollectionActivity;
import com.puxiang.mall.module.my.view.CouponActivity;
import com.puxiang.mall.module.my.view.PostAddress;
import com.puxiang.mall.module.my.view.ShowHeadPicActivity;
import com.puxiang.mall.module.pay.view.PayActivity;
import com.puxiang.mall.module.pay.view.PayResultActivity;
import com.puxiang.mall.module.personal.view.AttentionUsersActivity;
import com.puxiang.mall.module.personal.view.FansActivity;
import com.puxiang.mall.module.personal.view.PersonalActivity;
import com.puxiang.mall.module.personal.view.UserPlatesActivity;
import com.puxiang.mall.module.plate.view.PlatePostActivityNew;
import com.puxiang.mall.module.post.view.CommentActivity;
import com.puxiang.mall.module.post.view.PostDetailActivity;
import com.puxiang.mall.module.refund.view.RefundActivity;
import com.puxiang.mall.module.refund.view.ReturnAddressActivity;
import com.puxiang.mall.module.scan.view.QRCodeActivity;
import com.puxiang.mall.module.search.view.SearchActivity;
import com.puxiang.mall.module.search.view.SearchBBsListActivity;
import com.puxiang.mall.module.search.view.SearchListActivity;
import com.puxiang.mall.module.seller.view.ApplyDealerActivity;
import com.puxiang.mall.module.seller.view.DealerManager;
import com.puxiang.mall.module.seller.view.SellerNotifyActivity;
import com.puxiang.mall.module.shop.view.BuyGoodsList;
import com.puxiang.mall.module.shop.view.GoodsManager;
import com.puxiang.mall.module.shop.view.SelectCityActivity;
import com.puxiang.mall.module.shop.view.ShopDetial;
import com.puxiang.mall.module.shop.view.ShopListActivity;
import com.puxiang.mall.module.shoppingcart.view.ShoppCartActivity;
import com.puxiang.mall.module.userinfo.view.ChangeMobileActivity;
import com.puxiang.mall.module.userinfo.view.EmailActivity;
import com.puxiang.mall.module.userinfo.view.ForgetActivity;
import com.puxiang.mall.module.userinfo.view.InfoActivity;
import com.puxiang.mall.module.userinfo.view.NameActivity;
import com.puxiang.mall.module.userinfo.view.NickActivity;
import com.puxiang.mall.module.userinfo.view.SettingActivity;
import com.puxiang.mall.module.userinfo.view.SexActivity;
import com.puxiang.mall.module.userinfo.view.VerifyPhoneActivity;
import com.puxiang.mall.module.userinfo.viewmodel.ChangeMobileViewModel;
import com.puxiang.mall.module.welcome.view.GuideActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.config.ListType;
import com.puxiang.mall.model.data.RxAds;
import com.puxiang.mall.model.data.RxPost;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.module.login.view.LoginFragment;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class ActivityUtil {
    private static String TAG = "NETWORK_Exception";

    public static String getChannelName(Activity ctx) {
        if (ctx == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,
                // 因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx
                        .getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static void startLink(Activity activity, RxAds bean) {
        String type = bean.getContentType();
        if (StringUtil.isEmpty(type)) {
            type = ListType.LINK_HTML;
        }

        switch (type) {
            case ListType.LINK_INTEGRAL_TASK:
                activity.startActivity(new Intent(activity, IntegralActivity.class));
                break;

            case ListType.LINK_ACTIVITY:
                openChrome(activity, bean.getLinkUrl());
                break;

            case ListType.LINK_ESPORT:
                EventBus.getDefault().post(Event.GO_PLATES);
                startMainActivity(activity);
                break;
            case ListType.LINK_HTML:
            case ListType.LINK_H5:
            case ListType.LINK_PRODUCT:
            case ListType.LINK_INTEGRAL_MALL:
                String linkUrl = bean.getLinkUrl();
                if (linkUrl.contains("stock_list.htm")) {
                    /**
                     * 跳转到进货页面
                     * 如果是普通用户则跳转到开通经销商页面
                     */
                    if (MyApplication.isLogin()) {
                        startStockListActivity(activity);
                    } else {
                        startLoginActivity(activity);
                    }
                    return;
                }
                if (linkUrl.contains("shop_list.html")) {
                    //跳转精选商家
                    ActivityUtil.startBestShopActivity(activity);
                    return;
                }
                if (linkUrl.contains(URLs.HTML_SECKILL_PAGE_KEY)) {
                    //秒杀必须登录
                    WebUtil.jumpMyWeb(linkUrl, activity);
                    return;
                }
                WebUtil.jumpWeb(linkUrl, activity);
                break;
            case ListType.LINK_POST:
                String postId = bean.getContentId();
                startPostDetailActivity(activity, postId);
                break;
            case ListType.LINK_PLATE:
                String plateId = bean.getContentId();
                Logger.d("plateId", plateId);
                if (!StringUtil.isEmpty(plateId)) {
                    startPlatePostActivity(activity, plateId);
                }
                break;
            case ListType.LINK_PLATE_TYPE:
                ((MainActivity) activity).setCurrentItem(2);
                break;
            case ListType.LINK_CHANNEL:
                String typeId = bean.getContentId();
                String typeName = bean.getAdName();
                startFromActivity(activity, typeId, typeName);
                break;

            case ListType.LINK_AI:
                if (MyApplication.isLogin()) {
//                    startAiActivity(activity, bean.getShuohuInfo());
                } else {
                    startLoginActivity(activity);
                }
                break;
            case ListType.LINK_SEARCH:
                String contentId = bean.getContentId();
                if (!TextUtils.isEmpty(contentId)) {
                    startSearchList(activity, contentId);
                }
                break;
        }
    }

    public static void startSearchList(Activity activity, String content) {
        if (!TextUtils.isEmpty(content)) {
            String keyword = "";
            String catoryId = "";
            if (content.contains("keyword") && content.contains("categoryId")) {
                String[] searchContent = content.split("&");
                for (int i = 0; i < searchContent.length; i++) {
                    if (searchContent[i].contains("keyword")) {
                        keyword = searchContent[i].substring(searchContent[i].indexOf("=") + 1, searchContent[i].length());
                    } else {
                        catoryId = searchContent[i].substring(searchContent[i].indexOf("=") + 1, searchContent[i].length());
                    }
                }
            } else if (content.contains("keyword")) {
                keyword = content.substring(content.indexOf("=") + 1, content.length());
            } else {
                catoryId = content.substring(content.indexOf("=") + 1, content.length());
            }

            Intent intent = new Intent(activity, SearchListActivity.class);
            MobclickAgent.onEvent(activity, "keyword--->" + keyword);
            intent.putExtra("keyword", keyword);
            intent.putExtra("categoryId", catoryId);
            activity.startActivity(intent);
        }
    }

    public static void startLink(Activity activity, RxPost post) {
        String linkType = post.getLinkType();
        if (StringUtil.isEmpty(linkType)) {
            if (StringUtil.isEmpty(post.getLinkUrl())) {
                linkType = ListType.LINK_POST;
            } else {
                linkType = ListType.LINK_HTML;
            }
        }
        switch (linkType) {
            case ListType.LINK_POST:
                String postId = post.getId();
                startPostDetailActivity(activity, postId);
                break;
            case ListType.LINK_ACTIVITY:
                openChrome(activity, post.getLinkUrl());
                break;
            case ListType.LINK_ESPORT:
                EventBus.getDefault().post(Event.GO_PLATES);
                startMainActivity(activity);
                break;
            case ListType.LINK_HTML:
            case ListType.LINK_H5:
            case ListType.LINK_PRODUCT:
            case ListType.LINK_INTEGRAL_TASK:
            case ListType.LINK_INTEGRAL_MALL:
                String linkUrl = post.getLinkUrl();
                WebUtil.jumpWeb(linkUrl, activity);
                break;
            case ListType.LINK_PLATE:
                String plateId = post.getLinkId();
                startPlatePostActivity(activity, plateId);
                break;
            case ListType.LINK_PLATE_TYPE:
//                ((MainActivity) activity).setCurrentItem(1);
                break;
        }
    }

    public static void startPostFrom(Activity activity, RxPostInfo bean) {
        if (bean != null) {
            int postType = bean.getPost().getPostType();
            switch (postType) {
                case RxPostInfo.POST_PLATE:
                    String plateId = bean.getPlate().getId();
                    ActivityUtil.startPlatePostActivity(activity, plateId);
                    break;
                case RxPostInfo.POST_VIDEO:
                case RxPostInfo.POST_OFFICIAL:
                    int typeId = bean.getPost().getPostTypeId();
                    String typeName = bean.getPost().getPostTypeName();
                    ActivityUtil.startFromActivity(activity, typeId + "", typeName);
                    break;
                case RxPostInfo.POST_LINK:
                    break;
            }
        }
    }

    public static void openChrome(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    public static void error(Throwable e) {
        if (e instanceof RetrofitUtil.APIException) {
            RetrofitUtil.APIException exception = (RetrofitUtil.APIException) e;
            ToastUtil.toast(exception.getMessage());
            Logger.e(TAG, "error: APIException " + exception.getMessage());
        } else if (e instanceof SocketTimeoutException) {
            ToastUtil.toast("当前网络不可用");
            Logger.e(TAG, "error: SocketTimeoutException " + e.getMessage());
        } else if (e instanceof ConnectException) {
            ToastUtil.toast("当前网络不可用");
            Logger.e(TAG, "error: ConnectException " + e.getMessage());
        } else if (e instanceof JsonSyntaxException) {
            ToastUtil.toast("类型转换异常");
            Logger.e(TAG, "error: JsonSyntaxException 类型转换异常");
        } else {
            ToastUtil.toast(MyApplication.getContext().getString(R.string.no_network));
        }
        Log.e("ActivityUtil", String.valueOf(e.getMessage()));
        e.printStackTrace();
    }

    public interface ActivityRequestCode {
        int QR_SING = 100;
    }

    public static void startNickActivityForResult(Activity activity, String info, int i) {
        Intent intent = new Intent(activity, NickActivity.class);
        intent.putExtra("info", info);
        activity.startActivityForResult(intent, i);
    }

    //
    public static void startNameActivityForResult(Activity activity, String info, int i) {
        Intent intent = new Intent(activity, NameActivity.class);
        intent.putExtra("info", info);
        activity.startActivityForResult(intent, i);
    }

    public static void startSexActivityForResult(Activity activity, int info, int i) {
        Intent intent = new Intent(activity, SexActivity.class);
        intent.putExtra("info", info);
        activity.startActivityForResult(intent, i);
    }

    public static void startEmailActivityForResult(Activity activity, String info, int i) {
        Intent intent = new Intent(activity, EmailActivity.class);
        intent.putExtra("info", info);
        activity.startActivityForResult(intent, i);
    }

    public static void startSelectPlateActivityForResult(Activity activity, String plateId) {
        Intent intent = new Intent(activity, SelectPlateActivity.class);
        if (!StringUtil.isEmpty(plateId)) {
            intent.putExtra(Config.PLATE_ID, plateId);
        }
        activity.startActivityForResult(intent, 0);
    }

    //
    public static void startQRCodeActivityForResult(Activity activity, int code, String esportShopId, double
            latitude, double longitude) {
        Intent intent = new Intent(activity, QRCodeActivity.class);
        intent.putExtra("esportShopId", esportShopId);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        activity.startActivityForResult(intent, code);
    }

    public static void startGuideActivity(Activity activity) {
        activity.startActivity(new Intent(activity, GuideActivity.class));
    }


//
// -----------------------------------------------------------------------------------------------

    public static void startRegisterActivity(Activity activity) {
        Intent intent = new Intent(activity, RegisterFragment.class);
        activity.startActivity(intent);
    }

    public static void startForgetActivity(Activity activity) {
        Intent intent = new Intent(activity, ForgetActivity.class);
        activity.startActivity(intent);
    }

    //
    public static void startInfoActivity(Activity activity) {
        Intent intent = new Intent(activity, InfoActivity.class);
        activity.startActivity(intent);
    }

    //
    public static void startBindingMobileActivity(Activity activity) {
        Intent intent = new Intent(activity, BindingMobileActivity.class);
        activity.startActivity(intent);
    }

    public static void startLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 跳转登录注册页面
     *
     * @param activity
     * @param isLogin  标识登录或注册，if true viewpager显示登录
     */
    public static void startLoginActivity(Activity activity, boolean isLogin) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("isLogin", isLogin);
        activity.startActivity(intent);
    }


    public static void startShowHeadPicActivity(Activity activity, String url, String userId) {
        Intent intent = new Intent(activity, ShowHeadPicActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("userId", userId);
        activity.startActivity(intent);
    }

    public static void startPlatePostActivity(Activity activity, String plateId) {
        Intent intent = new Intent(activity, PlatePostActivityNew.class);
        intent.putExtra("plateId", plateId);
        activity.startActivity(intent);
    }

    public static void startPostDetailActivity(Activity activity, String postId) {
        if (TextUtils.isEmpty(postId)) {
            return;
        }
        Intent intent = new Intent(activity, PostDetailActivity.class);
        intent.putExtra("postId", postId);
        activity.startActivity(intent);
    }

    //
    public static void startPersonalActivity(Context context, String userId) {
        Intent personalIntent = new Intent(context, PersonalActivity.class);
        personalIntent.putExtra(Config.USER_ID, userId);
        context.startActivity(personalIntent);
    }

    //
//
    public static void startFromActivity(Activity activity, String typeId, String typeName) {
        Intent intent = new Intent(activity, FromActivity.class);
        intent.putExtra("typeId", typeId);
        intent.putExtra("typeName", typeName);
        activity.startActivity(intent);
    }

    //
    public static void startSearchBBsListActivity(FragmentActivity activity, String keyword) {
        Intent intent = new Intent(activity, SearchBBsListActivity.class);
        intent.putExtra(Config.KEYWORD, keyword);
        activity.startActivity(intent);
    }

    //
    public static void startCommentActivity(Activity activity, String commentId) {
        Intent intent = new Intent(activity, CommentActivity.class);
        intent.putExtra("commentId", commentId);
        activity.startActivity(intent);
    }

    //
    public static void startClassifyActivity(Activity activity) {
        Intent intent = new Intent(activity, ClassifyFragment.class);
        activity.startActivity(intent);
    }

    //
    public static void startSearchActivity(Context activity, String keyword) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtra("keyword", keyword);
        activity.startActivity(intent);
    }

    //
    public static void startSearchActivity(Context activity, String keyword, String id) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtra("keywordIndex", keyword);
        intent.putExtra("idIndex", id);
        activity.startActivity(intent);
    }

    //
    public static void startShoppingActivity(Activity activity) {
        Intent intent = new Intent(activity, ShoppCartActivity.class);
        activity.startActivity(intent);
    }

    //
    public static void startSettingActivity(Activity activity, boolean isNewestVersion, String
            introduce, String versionName) {
        if (!MyApplication.isLogin()) {
            startLoginActivity(activity);
            return;
        }
        Intent intent = new Intent(activity, SettingActivity.class);
        intent.putExtra("isNewestVersion", isNewestVersion);
        intent.putExtra("introduce", introduce);
        intent.putExtra("versionName", versionName);
        activity.startActivity(intent);
    }

    //
    public static void startUserPlatesActivity(Activity activity, String userId) {
        Intent intent = new Intent(activity, UserPlatesActivity.class);
        intent.putExtra("userId", userId);
        activity.startActivity(intent);
    }

    public static void startAttentionUsersActivity(Activity activity, String userId) {
        Intent intent = new Intent(activity, AttentionUsersActivity.class);
        intent.putExtra("userId", userId);
        activity.startActivity(intent);
    }

    public static void startFansActivity(Activity activity, String userId) {
        Intent intent = new Intent(activity, FansActivity.class);
        intent.putExtra("userId", userId);
        activity.startActivity(intent);
    }


    public static Map<String, String> getExtraMap(Activity activity) {
        String extra = activity.getIntent().getStringExtra("extraMap");
        if (!StringUtil.isEmpty(extra)) {
            try {
                Log.e(TAG, "extra: " + extra);
                Gson gson = new Gson();
                Map<String, String> extraMap = new HashMap<>();
                extraMap = gson.fromJson(extra, extraMap.getClass());

//                JSONObject object = new JSONObject(extra);
//                Map<String, String> extraMap = (Map<String, String>) object;
                Log.e(TAG, "extraMap: " + extraMap.toString());
                return extraMap;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void startPayActivity(Activity activity, String orderId) {
        Intent intent = new Intent(activity, PayActivity.class);
        intent.putExtra("orderId", orderId);
        activity.startActivity(intent);
    }

    //
    public static void startPublishActivity(Activity activity, String plateId, String detailId) {
        Intent intent = new Intent(activity, PublishActivity.class);
        intent.putExtra(Config.PLATE_ID, plateId);
        intent.putExtra(Config.DETAIL_ID, detailId);
        activity.startActivity(intent);
    }

    //
    public static void startRefundActivity(Activity activity, String orderDetailId, String
            productId, String tabIndex) {
        Intent intent = new Intent(activity, RefundActivity.class);
        intent.putExtra("orderDetailId", orderDetailId);
        intent.putExtra("productId", productId);
        intent.putExtra("tabIndex", tabIndex);
        activity.startActivity(intent);
    }

    /**
     * 朋友圈
     *
     * @param activity
     */
    public static void startFriendActivity(Activity activity) {
        Intent intent = new Intent(activity, FriendActivity.class);
        activity.startActivity(intent);
    }

    //
    public static void startPublishActivity(Activity activity) {
        Intent intent = new Intent(activity, PublishActivity.class);
        activity.startActivity(intent);
    }

    //
    public static void startPublishActivity(Activity activity, String plateId, String detailId,
                                            String plateName) {
        Intent intent = new Intent(activity, PublishActivity.class);
        intent.putExtra(Config.PLATE_ID, plateId);
        intent.putExtra(Config.DETAIL_ID, detailId);
        intent.putExtra(Config.PLATE_NAME, plateName);
        activity.startActivity(intent);
    }

    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    //
//    public static void startAiActivity(Activity activity, RxShuohuInfo shuohuInfo) {
//        Intent intent = new Intent(activity, AiActivity.class);
//        intent.putExtra("shuohuInfo", shuohuInfo);
//        activity.startActivity(intent);
//    }
//
    public static void startPayResultActivity(Activity activity, int errCode, String orderId, double totalPrices) {
        Intent intent = new Intent(activity, PayResultActivity.class);
        intent.putExtra("result", errCode);
        intent.putExtra("orderId", orderId);
        intent.putExtra("totalPrice", totalPrices);
        activity.startActivity(intent);
    }


    public static void startIntegralActivity(Activity activity) {
        Intent intent = new Intent(activity, IntegralActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 商家详情页面
     *
     * @param activity
     * @param shopId
     */
    public static void startShopDetialActivity(Activity activity, String shopId) {
        Intent intent = new Intent(activity, ShopDetial.class);
        intent.putExtra("shopId", shopId);
        activity.startActivity(intent);
    }


    /**
     * 进货页面
     *
     * @param activity
     */
    public static void startStockListActivity(Activity activity) {
        if (MyApplication.messageState.getIsDealer()) {
            Intent intent = new Intent(activity, BuyGoodsList.class);
            activity.startActivity(intent);
        } else {
            if (TextUtils.isEmpty(MyApplication.mCache.getAsString("dealer" + MyApplication.USER_ID))) {
                startSellerNotifyActivity(activity);
            } else {
                startApplySellerActivity(activity);
            }
        }
    }

    /**
     * 精选商家
     *
     * @param activity
     */
    public static void startBestShopActivity(Activity activity) {
        Intent intent = new Intent(activity, ShopListActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 精选商家
     *
     * @param activity
     */
    public static void startCityActivity(Activity activity, String areaName) {
        Intent intent = new Intent(activity, SelectCityActivity.class);
        intent.putExtra("city", areaName);
        activity.startActivity(intent);
    }

    /**
     * 收货地址
     *
     * @param activity
     */
    public static void startPostAddressActivity(Activity activity) {
        if (!MyApplication.isLogin()) {
            ActivityUtil.startLoginActivity(activity);
            return;
        }
        Intent intent = new Intent(activity, PostAddress.class);
        activity.startActivity(intent);
    }

    public static void startAddEditAddressActivity(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, AddEditAddressActivity.class);
        intent.putExtra("bundle", bundle);
        activity.startActivity(intent);
    }

    public static void startChangePhoneActivity(Activity activity, String ticket) {
        Intent intent = new Intent(activity, ChangeMobileActivity.class);
        intent.putExtra("ticket", ticket);
        activity.startActivity(intent);
    }

    public static void startVerifyPhoneActivity(Activity activity, String phone) {
        Intent intent = new Intent(activity, VerifyPhoneActivity.class);
        intent.putExtra("phone", phone);
        activity.startActivity(intent);
    }

    /**
     * 跳转 我的收藏页面
     *
     * @param activity
     */
    public static void startCollectionActivity(Activity activity) {
        Intent intent = new Intent(activity, CollectionActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 开通经销商提示页面
     *
     * @param activity
     */
    public static void startSellerNotifyActivity(Activity activity) {
        Intent intent = new Intent(activity, SellerNotifyActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 申请开通经销商
     *
     * @param activity
     */
    public static void startApplySellerActivity(Activity activity) {
        Intent intent = new Intent(activity, ApplyDealerActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 经销商信息管理
     *
     * @param activity
     */
    public static void startDealerManagerActivity(Activity activity) {
        Intent intent = new Intent(activity, DealerManager.class);
        activity.startActivity(intent);
    }

    /**
     * 我的优惠券
     *
     * @param activity
     */
    public static void startCouponActivity(Activity activity) {
        Intent intent = new Intent(activity, CouponActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 我的优惠券
     *
     * @param activity
     */
    public static void startRefundAddressActivity(Activity activity, String returnId) {
        Intent intent = new Intent(activity, ReturnAddressActivity.class);
        intent.putExtra("reId", returnId);
        activity.startActivity(intent);
    }

    /**
     * 商品管理页面
     *
     * @param activity
     */
    public static void startGoodsManager(Activity activity) {
        Intent intent = new Intent(activity, GoodsManager.class);
        activity.startActivity(intent);
    }

    /**
     * 商品管理页面
     *
     * @param activity
     */
    public static void startDealerList(Activity activity) {
        Intent intent = new Intent(activity, DealerListActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 采购页面
     *
     * @param activity
     */
    public static void startDealerBuy(Activity activity) {
        Intent intent = new Intent(activity, DealerBuyActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 经销商审核状态
     *
     * @param activity
     */
    public static void startDealerStatue(Activity activity,String data) {
        Intent intent = new Intent(activity, DealerStatueActivity.class);
        intent.putExtra("data",data);
        activity.startActivity(intent);
    }

    public static void startClassfyLink(Activity activity, RxClassfyRightItem bean) {
        String type = bean.getLinkType();
        if (StringUtil.isEmpty(type)) {
            type = ListType.LINK_HTML;
        }

        switch (type) {
            case ListType.LINK_INTEGRAL_TASK:
                activity.startActivity(new Intent(activity, IntegralActivity.class));
                break;

            case ListType.LINK_ACTIVITY:
                openChrome(activity, bean.getLinkContent());
                break;

            case ListType.LINK_ESPORT:
                EventBus.getDefault().post(Event.GO_PLATES);
                startMainActivity(activity);
                break;
            case ListType.LINK_HTML:
            case ListType.LINK_H5:
            case ListType.LINK_PRODUCT:
            case ListType.LINK_INTEGRAL_MALL:
                String linkUrl = bean.getLinkContent();
                if (linkUrl.contains("stock_list.htm")) {
                    /**
                     * 跳转到进货页面
                     * 如果是普通用户则跳转到开通经销商页面
                     */
                    if (MyApplication.isLogin()) {
                        startStockListActivity(activity);
                    } else {
                        startLoginActivity(activity);
                    }
                    return;
                }
                if (linkUrl.contains("shop_list.html")) {
                    //跳转精选商家
                    ActivityUtil.startBestShopActivity(activity);
                    return;
                }
                if (linkUrl.contains(URLs.HTML_SECKILL_PAGE_KEY)) {
                    //秒杀必须登录
                    WebUtil.jumpMyWeb(linkUrl, activity);
                    return;
                }
                WebUtil.jumpWeb(linkUrl, activity);
                break;
            case ListType.LINK_POST:
                String postId = bean.getLinkContent();
                startPostDetailActivity(activity, postId);
                break;
            case ListType.LINK_PLATE:
                String plateId = bean.getLinkContent();
                Logger.d("plateId", plateId);
                if (!StringUtil.isEmpty(plateId)) {
                    startPlatePostActivity(activity, plateId);
                }
                break;
            case ListType.LINK_PLATE_TYPE:
                ((MainActivity) activity).setCurrentItem(2);
                break;
            case ListType.LINK_CHANNEL:
                String typeId = bean.getLinkContent();
                String typeName = bean.getName();
                startFromActivity(activity, typeId, typeName);
                break;

            case ListType.LINK_AI:
                if (MyApplication.isLogin()) {
//                    startAiActivity(activity, bean.getShuohuInfo());
                } else {
                    startLoginActivity(activity);
                }
                break;
            case ListType.LINK_SEARCH:
                String contentId = bean.getLinkContent();
                if (!TextUtils.isEmpty(contentId)) {
                    startSearchList(activity, contentId);
                }
                break;
        }
    }

}
