package com.puxiang.mall.module.shop.viewModel;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.text.TextUtils;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxAds;
import com.puxiang.mall.model.data.RxCity;
import com.puxiang.mall.module.shop.dialog.DescDialog;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.widget.MyBanner;
import com.puxiang.mall.module.shop.dialog.CityDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by zhaoyong bai on 2017/10/12.
 */

public class ShopHeadViewModel extends BaseObservable implements ViewModel, BGABanner.Delegate<View, RxAds>, BGABanner.Adapter<View, RxAds> {
    private BaseBindActivity activity;
    private BaseBindFragment fragment;
    private ObservableBoolean isCityOpen = new ObservableBoolean(false);
    private ObservableBoolean isOrderOpen = new ObservableBoolean(false);
    public List<RxAds> bannerList = new ArrayList<>();
    private CityDialog cityDialog;
    private DescDialog descDialog;
    private String areaCode = "410100";

    @Bindable
    public boolean getIsCityOpen() {
        return isCityOpen.get();
    }

    public void setIsCityOpen(boolean isCityOpen) {
        this.isCityOpen.set(isCityOpen);
        notifyPropertyChanged(BR.isCityOpen);
    }

    @Bindable
    public boolean getIsOrderOpen() {
        return isOrderOpen.get();
    }

    public void setIsOrderOpen(boolean isOrderOpen) {
        this.isOrderOpen.set(isOrderOpen);
        notifyPropertyChanged(BR.isOrderOpen);
    }

    public ShopHeadViewModel(BaseBindActivity activity) {
        this.activity = activity;
        EventBus.getDefault().register(this);
    }

    public ShopHeadViewModel(BaseBindFragment fragment) {
        this.fragment = fragment;
        activity = (BaseBindActivity) fragment.getActivity();
        EventBus.getDefault().register(this);
    }

    /**
     * 获取轮播图数据
     */
    public void getBannerData() {
        ApiWrapper.getInstance()
                .getAds(URLs.SHOP_LIST)
                .compose(fragment != null ? fragment.bindUntilEvent(FragmentEvent.DESTROY) : activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxAds>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(List<RxAds> bean) {
                        bannerList.clear();
                        bannerList.addAll(bean);
                        notifyPropertyChanged(BR.bannerList);
                    }
                });
    }

    @Bindable
    public List<RxAds> getBannerList() {
        return bannerList;
    }

    @BindingAdapter("bannerShop")
    public static void setBanner(MyBanner banner, List<RxAds> list) {
        if (list == null || list.size() < 1) {
            return;
        }

        List<String> tips = new ArrayList<>();
        for (RxAds ads : list) {
            tips.add(ads.getText());
        }
        banner.setData(R.layout.viewpager_img, list, tips);
    }

    public void setBannerList(List<RxAds> bannerList) {
        this.bannerList = bannerList;
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void fillBannerItem(BGABanner banner, View itemView, RxAds model, int position) {
        ((SimpleDraweeView) itemView).setImageURI(model.getPicUrl());
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, RxAds model, int position) {
        try {
            ActivityUtil.startLink(activity, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RxCity rxCity) {
        if (rxCity != null) {
            setAreaCode(rxCity.getCode());
        }
    }

    public void headClick(View v) {
        switch (v.getId()) {
            case R.id.iv_refresh:

                break;
            case R.id.tv_city:

                int[] viewPoint = new int[]{0, 0};
                v.getLocationInWindow(viewPoint);

                if (cityDialog == null) {
                    cityDialog = new CityDialog(activity, true);
                    cityDialog.setOnDismissListener(dialogInterface -> setIsCityOpen(false));
                    cityDialog.setAreaCode(areaCode);
                } else {
                    cityDialog.setAreaCode(areaCode);
                }

                cityDialog.resizeHeight(viewPoint[1] + 70);

                if (cityDialog.isShowing()) {
                    cityDialog.dismiss();
                    setIsCityOpen(false);
                } else {
                    setIsCityOpen(true);
                    cityDialog.showAtLocation(viewPoint[0], viewPoint[1] + 70);
                }
                break;
            case R.id.tv_order:
                int[] descPoint = new int[]{0, 0};
                v.getLocationInWindow(descPoint);

                if (descDialog == null) {
                    descDialog = new DescDialog(activity, true);
                    descDialog.setOnDismissListener(dialogInterface -> setIsOrderOpen(false));
                }

                descDialog.resizeHeight(descPoint[1] + 70);

                if (descDialog.isShowing()) {
                    descDialog.dismiss();
                    setIsOrderOpen(false);
                } else {
                    setIsOrderOpen(true);
                    descDialog.showAtLocation(descPoint[0], descPoint[1] + 70);
                }
                break;
        }
    }


}
