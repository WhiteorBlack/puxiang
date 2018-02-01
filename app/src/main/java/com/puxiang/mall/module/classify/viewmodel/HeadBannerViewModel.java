package com.puxiang.mall.module.classify.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxAds;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.widget.MyBanner;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by 123 on 2017/9/5.
 */

public class HeadBannerViewModel extends BaseObservable implements ViewModel, BGABanner.Delegate<View, RxAds>, BGABanner.Adapter {
    private Activity activity;
    private BaseBindActivity bindActivity;
    private BaseBindFragment fragment;
    public List<RxAds> bannerList = new ArrayList<>();
    private static String dataCode;

    public HeadBannerViewModel(BaseBindFragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
    }

    public HeadBannerViewModel(BaseBindActivity activity) {
        this.bindActivity = activity;
        this.activity = activity;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void fillBannerItem(BGABanner banner, View itemView, Object model, int position) {
        if (TextUtils.equals(dataCode, "classfy")) {
            ((SimpleDraweeView) itemView).setImageURI(AppUtil.getResUri(((RxAds) model).getResId()));
        } else {
            ((SimpleDraweeView) itemView).setImageURI(((RxAds) model).getPicUrl());
        }
    }

    @BindingAdapter("headBanner")
    public static void setBanner(MyBanner banner, List<RxAds> list) {
        List<String> tips = new ArrayList<>();
        if (TextUtils.equals(dataCode, "classfy")) {
            list.clear();
            RxAds rxAds = new RxAds();
            rxAds.setResId(R.mipmap.banner_classfy_one);
            list.add(rxAds);

            RxAds rxAds2 = new RxAds();
            rxAds2.setResId(R.mipmap.banner_classfy_two);
            list.add(rxAds2);
        } else {
            if (list == null || list.size() < 1) {
                return;
            }


            for (RxAds ads : list) {
                tips.add(ads.getText());
            }
        }


        banner.setData(R.layout.viewpager_img, list, tips);
    }

    /**
     * 获取轮播图数据
     */
    public void getBannerData(String channelCode) {
        dataCode = channelCode;
        ApiWrapper.getInstance()
                .getAds(channelCode)
                .compose(fragment == null ? bindActivity.bindUntilEvent(ActivityEvent.DESTROY) : fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxAds>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(List<RxAds> bean) {
                        bannerList.clear();
                        bannerList.addAll(bean);
                        notifyPropertyChanged(BR.headBanner);
                    }
                });
    }

    @Bindable
    public List<RxAds> getHeadBanner() {
        return bannerList;
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, RxAds model, int position) {
        try {
            ActivityUtil.startLink(activity, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
