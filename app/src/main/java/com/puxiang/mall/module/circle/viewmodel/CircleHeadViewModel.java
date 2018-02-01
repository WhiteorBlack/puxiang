package com.puxiang.mall.module.circle.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.puxiang.mall.BR;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.config.ListType;
import com.puxiang.mall.model.data.RxAds;
import com.puxiang.mall.module.circle.adapter.CircleNavigateAdapter;
import com.puxiang.mall.module.circle.view.CircleFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.DraweeViewUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import io.reactivex.Observable;

public class CircleHeadViewModel extends BaseObservable implements ViewModel, BGABanner
        .Delegate<View, RxAds>, BGABanner.Adapter {

    private final Activity activity;
    private final CircleNavigateAdapter navigateAdapter;
    private final CircleFragment fragment;
    private List<RxAds> bannerList = new ArrayList<>();

    /**
     * 这里的类目模块没有设计暂时隐藏不用
     * @param fragment
     * @param navigateAdapter
     */
    public CircleHeadViewModel(CircleFragment fragment, CircleNavigateAdapter navigateAdapter) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
        this.navigateAdapter = navigateAdapter;
//        getCache();
        getBannerData(URLs.COMMUNITY_CAROUSEL);
//        getNavigateData();
    }

    /**
     * 获取缓存
     */
    private void getCache() {
        try {
            boolean isFirst = fragment.getArguments().getBoolean("first", false);
            if (isFirst) {
//                getHomeNavigateCache();
                getHomeBannerCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 首页Banner 缓存
     */
    private void getHomeBannerCache() {
        MyApplication.mCache.getAsListBean(CacheKey.CIRCLE_BANNER, RxAds[].class, bannerData -> {
            if (!bannerData.isEmpty()) {
                bannerList.addAll(bannerData);
                notifyPropertyChanged(BR.banner);
            }
        });
    }

    /**
     * 获取 首页导航栏 缓存
     */
    private void getHomeNavigateCache() {
        MyApplication.mCache.getAsListBean(CacheKey.HOME_NAVIGATE, RxAds[].class, list -> {
            if (!list.isEmpty()) {
                navigateAdapter.setNewData(list);
            }
        });
    }

    /**
     * 请求网络 获取Banner 数据
     */
    public void getBannerData(String type) {
        ApiWrapper.getInstance()
                .getAds(type)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxAds>>() {
                    @Override
                    public void onSuccess(List<RxAds> list) {
//                        MyApplication.mCache.put(CacheKey.CIRCLE_BANNER, list);
                        bannerList.clear();
                        bannerList.addAll(list);
                        notifyPropertyChanged(BR.banner);
                    }
                });
    }


    @Bindable
    public List<RxAds> getBanner() {
        return this.bannerList;
    }

    /**
     * 获取导航栏数据
     */
    private void getNavigateData() {

        Observable.zip(ApiWrapper.getInstance().getAds(URLs.INDEX_NAVIGATE)
                , ApiWrapper.getInstance().getShuohuInfo(), (adsList, shuohuInfo) -> {
                    if (shuohuInfo != null) {
                        RxAds rxAds = new RxAds();
                        rxAds.setShuohuInfo(shuohuInfo);
                        rxAds.setPicUrl(shuohuInfo.getLinkPic());
                        rxAds.setAdName(shuohuInfo.getLinkName());
                        rxAds.setContentType(ListType.LINK_AI);
                        adsList.add(rxAds);
                    }
                    return adsList;
                })
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxAds>>() {
                    @Override
                    public void onSuccess(List<RxAds> adsList) {
//                        MyApplication.mCache.put(CacheKey.HOME_NAVIGATE, adsList);
                        navigateAdapter.setNewData(adsList);
                    }
                });
    }

    /**
     * Banner视图填充
     */
    @Override
    public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
        DraweeViewUtils.getInstance().loadImg(((SimpleDraweeView) view),
                ((RxAds) model).getPicUrl(), 600, (int) (600/1.68));
    }

    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                try {
                    RxAds bean = navigateAdapter.getData().get(i);
                    ActivityUtil.startLink(activity, bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void destroy() {
    }

    /**
     * banner点击响应
     */
    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, RxAds model, int position) {
        try {
            ActivityUtil.startLink(activity, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
