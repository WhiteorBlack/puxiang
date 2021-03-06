package com.puxiang.mall.module.my.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.v4.app.FragmentActivity;

import com.orhanobut.logger.Logger;
import com.puxiang.mall.BR;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.model.data.AppVersionJSON;
import com.puxiang.mall.model.data.RxAds;
import com.puxiang.mall.model.data.RxMessageState;
import com.puxiang.mall.model.data.RxMyItem;
import com.puxiang.mall.model.data.RxMyUserInfo;
import com.puxiang.mall.model.data.RxOrderState;
import com.puxiang.mall.module.im.model.IMRequest;
import com.puxiang.mall.module.im.model.IMUserInfoProvider;
import com.puxiang.mall.module.im.model.MessageCount;
import com.puxiang.mall.module.im.viewmodel.ImListViewModel;
import com.puxiang.mall.module.my.view.MyFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.StringUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

public class MyViewModel extends BaseObservable implements ViewModel {


    private MyFragment fragment;
    private FragmentActivity activity;
    public ObservableField<RxMyUserInfo> userBean = new ObservableField<>();
    public ObservableField<String> bgUrl = new ObservableField<>();
    private ObservableBoolean isBarVis = new ObservableBoolean(false);
    private ObservableBoolean isSellerVis = new ObservableBoolean(false);
    private ObservableInt barAlpha = new ObservableInt(255);
    private ObservableInt topAlpha = new ObservableInt(255);
    public String introduce = "";
    public String versionName = "";
    private boolean isInitIM = false;

    public MyViewModel(MyFragment fragment) {
        this.fragment = fragment;
        EventBus.getDefault().register(this);
        activity = fragment.getActivity();
        init();
    }

    private void init() {
        getCacheData();
        //TODO: 即使通讯连接
        if (!StringUtil.isEmpty(MyApplication.TOKEN)) {
            // 设置接收 push 消息的监听器。
            if (!isInitIM) {
                RongIM.setUserInfoProvider(IMUserInfoProvider.getInstance(), true);
                isInitIM = true;
            }
            IMRequest.IMConnect();
            MyApplication.isLoginOB.set(true);
        }
    }

    /**
     * 初始化订单模块数据
     *
     * @return
     */
    public List<RxMyItem> getOrderList() {
        List<RxMyItem> items = new ArrayList<>();
        items.add(new RxMyItem("待支付", R.mipmap.my_ordering, 0));
        items.add(new RxMyItem("待发货", R.mipmap.my_wait_post, 1));
        items.add(new RxMyItem("待收货", R.mipmap.my_receving, 2));
        items.add(new RxMyItem("待评价", R.mipmap.my_goods_comment, 3));
        items.add(new RxMyItem("退货", R.mipmap.my_reback, 4));
        return items;
    }

    /**
     * 初始化我是买家模块数据
     *
     * @return
     */
    public List<RxMyItem> getBuyList() {
        List<RxMyItem> items = new ArrayList<>();

        items.add(new RxMyItem("我的收藏", R.mipmap.my_coll, 0));
        items.add(new RxMyItem("收货地址", R.mipmap.my_map, 1));
        items.add(new RxMyItem("优惠券", R.mipmap.my_coupon, 12));
        if (!MyApplication.isLogin() || !MyApplication.messageState.getIsSeller()) {
            items.add(new RxMyItem("我要开店", R.mipmap.my_new_shop, 9));
        }
//        items.add(new RxMyItem("我要开店", R.mipmap.my_new_shop, 9));
        items.add(new RxMyItem("评论我的", R.mipmap.my_comment, 4));
        items.add(new RxMyItem("赞我的", R.mipmap.my_zan, 5));
//        items.add(new RxMyItem("我的积分", R.mipmap.my_integ, 6));
        items.add(new RxMyItem("积分商城", R.mipmap.my_integra_shop, 7));
        items.add(new RxMyItem("我的社区", R.mipmap.my_circle, 2));
        items.add(new RxMyItem("我的发帖", R.mipmap.my_post, 3));
        items.add(new RxMyItem("绑定经销商", R.mipmap.my_bind_dealer, 15));
        items.add(new RxMyItem("采购", R.mipmap.my_saler_buy, 16));
//        items.add(new RxMyItem("任务中心", R.mipmap.my_mession_center_gray, 8));
//        items.add(new RxMyItem("我的消息", R.mipmap.my_message, 13));
//        items.add(new RxMyItem("设置", R.mipmap.my_setting, 14));
        return items;
    }

    /**
     * 初始化 我是卖家的模块数据
     *
     * @return
     */
    public List<RxMyItem> getSaleList() {
        List<RxMyItem> items = new ArrayList<>();
        items.add(new RxMyItem("我的店铺", R.mipmap.my_new_shop, 0));
        items.add(new RxMyItem("店铺管理", R.mipmap.my_man_shop, 1));
        items.add(new RxMyItem("我的订单", R.mipmap.my_buy_order, 2));
        items.add(new RxMyItem("商品管理", R.mipmap.my_goods_man, 3));
        items.add(new RxMyItem("运费管理", R.mipmap.my_dealer_fee, 4));
//        items.add(new RxMyItem("绑定经销商", R.mipmap.my_bind_dealer, 5));
//        items.add(new RxMyItem("采购", R.mipmap.my_saler_buy, 7));
        return items;
    }

    /**
     * 初始化 未成为经销商数据
     *
     * @return
     */
    public List<RxMyItem> getNoDealer() {
        List<RxMyItem> items = new ArrayList<>();
        items.add(new RxMyItem("成为经销商", R.mipmap.my_dealer, 0));
//        items.add(new RxMyItem("我要进货", R.mipmap.my_buy_man_gray, 1));
        return items;
    }

    /**
     * 初始化 成为经销商数据
     *
     * @return
     */
    public List<RxMyItem> getDealer() {
        List<RxMyItem> items = new ArrayList<>();
        items.add(new RxMyItem("信息管理", R.mipmap.my_dealer_manager, 4));
        items.add(new RxMyItem("我要进货", R.mipmap.my_buy_man, 2));
        items.add(new RxMyItem("我的订单", R.mipmap.my_buy_order, 3));
        return items;
    }

    /**
     * 初始化 系统中心
     *
     * @return
     */
    public List<RxMyItem> getSystem() {
        List<RxMyItem> items = new ArrayList<>();
        items.add(new RxMyItem("我的消息", R.mipmap.my_message, 0));
        items.add(new RxMyItem("设置", R.mipmap.my_setting, 1));
        items.add(new RxMyItem("客服中心", R.mipmap.icon_my_service, 3));
//        items.add(new RxMyItem("关于我们", R.mipmap.icon_my_about_us, 2));
        return items;
    }

    /**
     * 获取本地缓存数据
     */
    private void getCacheData() {
        //TODO: 获取用户信息
        MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class
                , userBean::set);
        //TODO: 获取App版本信息
        MyApplication.mCache.getAsJSONBean(CacheKey.VERSION, AppVersionJSON.class, appVersionJSON -> {
            AppVersionJSON.ReturnObjectBean bean = appVersionJSON.getReturnObject();
            int versionCode = bean.getVersionCode();
            boolean isNewestVersion = ActivityUtil.getVersionCode(activity) >= versionCode;
            MyApplication.messageState.setNewestVersion(isNewestVersion);
            introduce = bean.getIntroduce();
            versionName = bean.getVersionName();
        });

        //TODO: 获取背景图Url
        MyApplication.mCache.getAsString(CacheKey.MY_BG_URL, bgUrlStr -> bgUrl.set(bgUrlStr));
    }

    //接收消息推送
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(MessageCount messageCount) {
        if (messageCount != null && !ImListViewModel.isInitOK) {
            int count = messageCount.getCount();
            MyApplication.messageState.setMyMessage(count);
            MyApplication.messageState.notifyMessageChanged();
        }
    }

    //接收更新请求
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(RxMyUserInfo myUserInfo) {
        if (myUserInfo != null) {
            userBean.set(myUserInfo);
            if (!isInitIM) {
                RongIM.setUserInfoProvider(IMUserInfoProvider.getInstance(), true);
                isInitIM = true;
            }
        }
    }

    public void getBgUrl() {
        ApiWrapper.getInstance()
                .getAds(URLs.MY_BACKGROUND)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxAds>>() {
                    @Override
                    public void onSuccess(List<RxAds> bean) {
                        String bgUrlStr = bean.get(0).getPicUrl();
                        if (!StringUtil.isEmpty(bgUrlStr)) {
//                            MyApplication.mCache.put(CacheKey.MY_BG_URL, bgUrlStr);
                            bgUrl.set(bgUrlStr);
                        }
                    }
                });
    }

    public void getMessageState() {
        ApiWrapper.getInstance()
                .getMessageState()
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxMessageState>() {
                    @Override
                    public void onSuccess(RxMessageState bean) {
                        setMessageState(bean);
                    }
                });

    }

    private void setMessageState(RxMessageState message) {
        MyApplication.messageState.setData(message);
    }

    public void getOrderState() {
        ApiWrapper.getInstance()
                .getOrderStatusNum()
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxOrderState>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(RxOrderState data) {
                        fragment.updateOrderState(data);
                    }
                });
    }

    /**
     * 查阅消息
     *
     * @param messageCode 消息类型
     */
    public void setMessageReadTime(String messageCode) {
        ApiWrapper.getInstance()
                .setMessageReadTime(messageCode)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {
                    }
                });
    }

    @Bindable
    public boolean getIsSellerVis() {
        return isSellerVis.get();
    }

    public void setIsSellerVis(boolean isSellerVis) {
        this.isSellerVis.set(isSellerVis);
        notifyPropertyChanged(BR.isSellerVis);
    }

    @Bindable
    public boolean getIsBarVis() {
        return isBarVis.get();
    }

    public void setIsBarVis(boolean isBarVis) {
        this.isBarVis.set(isBarVis);
        notifyPropertyChanged(BR.isBarVis);
    }

    @Bindable
    public int getBarAlpha() {
        return barAlpha.get();
    }

    public void setBarAlpha(int barAlpha) {
        this.barAlpha.set(barAlpha);
        notifyPropertyChanged(BR.barAlpha);
    }

    @Bindable
    public int getTopAlpha() {
        return topAlpha.get();
    }

    public void setTopAlpha(int topAlpha) {
        this.topAlpha.set(topAlpha);
        notifyPropertyChanged(BR.topAlpha);
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

}
