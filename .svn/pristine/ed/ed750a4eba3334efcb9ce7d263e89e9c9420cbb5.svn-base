package com.puxiang.mall.module.circle.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.model.data.RxChannel;
import com.puxiang.mall.model.data.RxMyUserInfo;
import com.puxiang.mall.module.circle.view.CirclePageFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.StringUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class CirclePageViewModel extends BaseObservable implements ViewModel {

    private final CirclePageFragment.HomePageAdapter adapter;
    private final CirclePageFragment fragment;
    public ObservableField<String> keyword = new ObservableField<>("");
    public ObservableBoolean isInitData = new ObservableBoolean(true);
    public ObservableBoolean isInit = new ObservableBoolean(true);
    public ObservableField<RxMyUserInfo> userInfo = new ObservableField<>();

    /**
     * 接收更新请求
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RxMyUserInfo myUserInfo) {
        if (myUserInfo != null) {
            userInfo.set(myUserInfo);
        }
    }

    public CirclePageViewModel(final CirclePageFragment fragment, final CirclePageFragment.HomePageAdapter adapter) {
        EventBus.getDefault().register(this);
        this.fragment = fragment;
        this.adapter = adapter;
        getCache();
        //  getChannels();
        //   getSearchText();
        initData();
    }

    private void initData() {
        List<RxChannel> channelList = new ArrayList<>();

        RxChannel homeChannel = new RxChannel();
        homeChannel.setContentType(RxChannel.CONTENT_TYPE_HOME);
        homeChannel.setTypeId(12);
        homeChannel.setChannelName("推荐");

        RxChannel plateChannel = new RxChannel();
        plateChannel.setContentType(RxChannel.CONTENT_TYPE_PLATE);
        plateChannel.setChannelName("圈子");
        channelList.add(homeChannel);
        channelList.add(plateChannel);
        setChannelsData(channelList);
    }

    /**
     * 获取缓存数据
     */
    private void getCache() {
        // getKeywordCache();
        getUserInfoCache();
        //   getChannelsCache();
    }


    /**
     * 获取缓存的用户信息
     */
    private void getUserInfoCache() {
        if (!StringUtil.isEmpty(MyApplication.TOKEN)) {
            MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class, myUserInfo -> {
                if (myUserInfo != null) {
                    userInfo.set(myUserInfo);
                }
                MyApplication.isLoginOB.set(true);
            });
        } else {
            MyApplication.isLoginOB.set(false);
        }
    }

    /**
     * 获取频道缓存。
     */
    private void getChannelsCache() {
        List<RxChannel> rxChannels = MyApplication.mCache.getAsListBean(CacheKey.CHANNELS_HOME, RxChannel[].class);
        adapter.setNewData(rxChannels);
    }

//    /**
//     * 获取搜索栏关键字缓存
//     */
//    private void getKeywordCache() {
//        MyApplication.mCache.getAsString(CacheKey.TEXT_SEARCH, s -> keyword.set(s));
//    }
//
//    /**
//     * 获取搜索栏内容
//     */
//    private void getSearchText() {
//        ApiWrapper.getInstance()
//                .getAds(URLs.TEXT_SEARCH)
//                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
//                .subscribe(new NetworkSubscriber<List<RxAds>>() {
//                    @Override
//                    public void onSuccess(final List<RxAds> bean) {
//                        setSearchText(bean);
//                    }
//                });
//    }
//
//    /**
//     * 设置搜索栏关键字
//     */
//    private void setSearchText(List<RxAds> list) {
//        if (list.size() > 0) {
//            String searchText = list.get(0).getText();
//            String text = searchText.trim();
//            keyword.set(text);
//            MyApplication.mCache.put(CacheKey.TEXT_SEARCH, text);
//        }
//    }

    /**
     * 获取频道
     */
    public void getChannels() {
        ApiWrapper.getInstance()
                .getChannels()
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxChannel>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        if (adapter.getCount() < 1) {
                            isInitData.set(false);
                        }
                    }

                    @Override
                    public void onSuccess(List<RxChannel> bean) {
                        isInitData.set(true);
                        setChannelsData(bean);
                    }
                });
    }

    /**
     * 设置频道数据源
     *
     * @param channels 频道数据源
     */
    private void setChannelsData(List<RxChannel> channels) {
        //  MyApplication.mCache.put(CacheKey.CHANNELS_HOME, channels);
        adapter.setNewData(channels);
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
