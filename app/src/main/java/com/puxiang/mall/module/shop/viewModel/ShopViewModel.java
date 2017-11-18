package com.puxiang.mall.module.shop.viewModel;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxArea;
import com.puxiang.mall.model.data.RxCity;
import com.puxiang.mall.model.data.RxLocation;
import com.puxiang.mall.model.data.RxShop;
import com.puxiang.mall.model.data.RxShopList;
import com.puxiang.mall.module.shop.adapter.ShopListAdapter;
import com.puxiang.mall.module.shop.view.ShopListActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.puxiang.mall.utils.StringUtil.getString;

/**
 * Created by zhaoyong bai on 2017/10/12.
 */

public class ShopViewModel extends BaseObservable implements ViewModel {

    private BaseBindFragment fragment;
    private ShopListActivity activity;
    public ObservableField<String> searchText = new ObservableField<>("请输入商户名、地点");
    public ObservableField<String> cityText = new ObservableField<>("全城");
    public ObservableField<String> descText = new ObservableField<>("智能排序");
    private ObservableBoolean isTopVisible = new ObservableBoolean(false);
    private ObservableField<String> currentCity = new ObservableField<>("郑州市");
    private ObservableField<String> currentStreet = new ObservableField<>("");
    private int pageNo = 1;
    private ShopListAdapter adapter;
    private String keyword = "", areaCode = "", orderBy = "defalut", areaName = "";
    private ObservableBoolean isInitData = new ObservableBoolean(false);
    private boolean isUpdate = false;  //只有第一次进入获取定位数据，之后更新定位只更新当前位置，不跟新商铺

    private LoadingWindow loadingWindow;

    public ShopViewModel(BaseBindFragment fragment, ShopListAdapter adapter) {
        this.fragment = fragment;
        this.adapter = adapter;
        EventBus.getDefault().register(this);
        loadingWindow = new LoadingWindow(fragment.getActivity());
        loadingWindow.delayedShowWindow();
    }


    public ShopViewModel(ShopListActivity fragment, ShopListAdapter adapter) {
        this.activity = fragment;
        this.adapter = adapter;
        EventBus.getDefault().register(this);
        loadingWindow = new LoadingWindow(activity);
        loadingWindow.delayedShowWindow();
    }


    @Bindable
    public String getCityText() {
        return cityText.get();
    }

    public void setCityText(String cityText) {
        this.cityText.set(cityText);
        notifyPropertyChanged(BR.cityText);
    }

    @Bindable
    public String getCurrentStreet() {
        return currentStreet.get();
    }

    public void setCurrentStreet(String currentStreet) {
        this.currentStreet.set(currentStreet);
        notifyPropertyChanged(BR.currentStreet);
    }

    @Bindable
    public String getDescText() {
        return descText.get();
    }

    public void setDescText(String cityText) {
        this.descText.set(cityText);
        notifyPropertyChanged(BR.descText);
    }

    public boolean getIsTopVisible() {
        return isTopVisible.get();
    }

    public void setIsTopVisible(boolean isTopVisible) {
        this.isTopVisible.set(isTopVisible);
    }

    @Bindable
    public String getSearchText() {
        return searchText.get();
    }

    public void setSearchText(String searchText) {
        this.searchText.set(searchText);
    }

    public void loadMore() {
        pageNo++;
        getShopList(pageNo, keyword, areaCode, orderBy);
    }

    public void getShopList(int pageNo, String keyword, String areaCode, String city) {
        if (pageNo == 1) {
            this.pageNo = 1;
            this.keyword = keyword;
            this.areaCode = areaCode;
        }

        ApiWrapper.getInstance()
                .getShopList(keyword, this.areaCode, this.pageNo, currentCity.get(), orderBy)
                .compose(fragment != null ? fragment.bindUntilEvent(FragmentEvent.DESTROY) : activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(loadingWindow::hidWindow)
                .subscribe(new NetworkSubscriber<RxShopList>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        if (fragment != null)
                            fragment.refreshFail();
                        else activity.refreshFail();
                    }


                    @Override
                    public void onSuccess(RxShopList data) {
                        if (fragment != null)
                            fragment.refreshOK();
                        else activity.refreshOK();
                        adapter.setPagingData(data.getList(), pageNo);
                        isInitData.set(true);
                        notifyPropertyChanged(BR.isInitData);
                    }
                });
    }

    public void getCurrentLocation(double lat, double lng) {
        ApiWrapper.getInstance()
                .getLocation(lat, lng)
                .compose(fragment != null ? fragment.bindUntilEvent(FragmentEvent.DESTROY) : activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxLocation>() {
                    @Override
                    public void onSuccess(RxLocation data) {
                        if (isUpdate) {
                            setCurrentStreet(data.getAddress());
                            return;
                        }
                        activity.updateCode(data.getCode());
                        isUpdate = true;
                        setCurrentStreet(data.getAddress());
                        areaName = data.getCity();
                        setCurrentCity(areaName);
                        RxCity rxCity = new RxCity();
                        rxCity.setSelected(true);
                        rxCity.setName(data.getCity());
                        rxCity.setCode(data.getCode());
                        dealLatestData(rxCity);
                        getShopList(1, "", "", data.getCity());
                    }
                });
    }

    /**
     * 把选择信息放到缓存中
     */
    private List<RxArea> areaList = new ArrayList<>();

    private void dealLatestData(RxCity rxCity) {

        MyApplication.mCache.getAsListBean(CacheKey.LATESTCITY, RxArea[].class, rxAreas -> {
            if (rxAreas != null) {
                areaList.addAll(rxAreas);
            }
            RxArea rxArea = new RxArea();
            rxArea.setIsVisiable(true);
            rxArea.setAreaName(rxCity.getName());
            rxArea.setAreaCode(rxCity.getCode());
            if (areaList.size() > 0) {
                for (int i = 0; i < areaList.size(); i++) {
                    areaList.get(i).setIsVisiable(false);
                    if (TextUtils.equals(rxCity.getName(), areaList.get(i).getName())) {
                        areaList.remove(i);
                        break;
                    }
                }
            }

            areaList.add(0, rxArea);

            if (areaList.size() > 3) {
                areaList = areaList.subList(0, 3);
            }
            MyApplication.mCache.put(CacheKey.LATESTCITY, areaList);
        });

    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                RxShop rxShop = (RxShop) adapter.getData().get(position);
                ActivityUtil.startShopDetialActivity(activity, rxShop.getShopId());
            }
        };
    }

    public void setIsInitData(boolean isInitData) {
        this.isInitData.set(isInitData);
    }

    @Bindable
    public boolean getIsInitData() {
        return isInitData.get();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Bundle area) {
        switch (area.getInt("type")) {
            case 0:
                if (!TextUtils.equals(getCityText(), area.getString("content")))
                    setCityText(area.getString("content"));
                areaCode = area.getString("code");
                getShopList(1, keyword, areaCode, orderBy);
                break;
            case 1:
                if (!TextUtils.equals(getDescText(), area.getString("content"))) {
                    setDescText(area.getString("content"));
                    orderBy = area.getString("code");
                    getShopList(1, keyword, areaCode, orderBy);
                }
                break;
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RxArea rxCity) {
        if (rxCity != null) {
            pageNo = 1;
            areaName = rxCity.getName();
            areaCode = rxCity.getAreaCode();
            keyword = "";
            orderBy = "default";
            this.setCurrentCity(areaName);
            activity.updateCode(areaCode);
            getShopList(pageNo, keyword, areaCode, areaName);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_location:
                ActivityUtil.startCityActivity(activity, getCurrentCity());
                break;
            case R.id.iv_back:
                activity.onBackPressed();
                break;
        }
    }

    public void setCurrentCity(String city) {
        this.currentCity.set(city);
        notifyPropertyChanged(BR.currentCity);
    }

    @Bindable
    public String getCurrentCity() {
        return currentCity.get();
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
