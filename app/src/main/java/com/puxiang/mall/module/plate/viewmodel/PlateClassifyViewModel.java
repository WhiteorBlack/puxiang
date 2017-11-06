package com.puxiang.mall.module.plate.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.BR;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.model.data.RxPlate;
import com.puxiang.mall.model.data.RxPlateType;
import com.puxiang.mall.module.plate.adapter.PlateClassifyLeftAdapter;
import com.puxiang.mall.module.plate.adapter.PlateRightAdapter;
import com.puxiang.mall.module.plate.view.PlateVTabLayout;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class PlateClassifyViewModel extends BaseObservable implements ViewModel {
    private final Activity activity;
    private final PlateRightAdapter rightAdapter;
    private List<RxPlateType> catalogList = new ArrayList<>();
    // public ObservableField<RxMyUserInfo> userInfo = new ObservableField<>();

    public PlateClassifyViewModel(Activity activity, PlateRightAdapter rightAdapter) {
//        EventBus.getDefault().register(this);
        this.activity = activity;
        this.rightAdapter = rightAdapter;
        getCache();
        getLeftData();
    }

    private void getCache() {
        //  getUserInfoCache();
        getCatalogCache();
    }

//    private void getUserInfoCache() {
//        if (!StringUtil.isEmpty(MyApplication.TOKEN)) {
//            MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class, myUserInfo -> {
//                if (myUserInfo != null) {
//                    userInfo.set(myUserInfo);
//                }
//                isLoginOB.set(true);
//            });
//        } else {
//            isLoginOB.set(false);
//        }
//    }

    /**
     * 获取圈子目录缓存
     */
    private void getCatalogCache() {
        MyApplication.mCache.getAsListBean(CacheKey.PLATE_CLASSIFY, RxPlateType[].class, plateTypes -> {
            if (!plateTypes.isEmpty()) {
                catalogList.clear();
                catalogList.addAll(plateTypes);
                notifyPropertyChanged(BR.catalogList);
                getRightData(0);
            }
        });
    }

//    //接收更新请求
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(RxMyUserInfo myUserInfo) {
//        if (myUserInfo != null) {
//            userInfo.set(myUserInfo);
//        }
//    }

    @BindingAdapter("catalogData")
    public static void setCatalogData(PlateVTabLayout tabLayout, List<RxPlateType> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        try {
            RxPlateType rxPlateType = new RxPlateType();
            rxPlateType.setName("朋友圈");
            list.add(rxPlateType);
            PlateClassifyLeftAdapter tabAdapter = new PlateClassifyLeftAdapter(list);
            tabLayout.setTabAdapter(tabAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bindable
    public List<RxPlateType> getCatalogList() {
        return this.catalogList;
    }

    /**
     * 获取圈子目录
     */
    private void getLeftData() {
        ApiWrapper.getInstance().allPlates()
                .subscribe(new NetworkSubscriber<List<RxPlateType>>() {
                    @Override
                    public void onSuccess(List<RxPlateType> bean) {
                        saveCache(bean);
                        catalogList = bean;
                        notifyPropertyChanged(BR.catalogList);
                        getRightData(0);
                    }
                });
    }

    /**
     * 缓存圈子目录
     *
     * @param bean
     */
    private void saveCache(List<RxPlateType> bean) {
        MyApplication.mCache.put(CacheKey.PLATE_CLASSIFY, bean);
    }

    /**
     * 获取目录圈子列表
     */
    public void getRightData(final int i) {
        if (catalogList.size() < 1) {
            return;
        }
        if (i == catalogList.size() - 1) {
            if (StringUtil.isEmpty(MyApplication.TOKEN)) {
                ActivityUtil.startLoginActivity(activity);
            } else {
                ActivityUtil.startFriendActivity(activity);
            }
            return;
        }
        RxPlateType plateType = catalogList.get(i);
        List<RxPlate> plates = plateType.getPlates();
        rightAdapter.setNewData(plates);
    }

    public OnItemClickListener clickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String plateId = rightAdapter.getData().get(i).getId();
                ActivityUtil.startPlatePostActivity(activity, plateId);
            }
        };
    }

    @Override
    public void destroy() {
        //EventBus.getDefault().unregister(this);
    }
}
