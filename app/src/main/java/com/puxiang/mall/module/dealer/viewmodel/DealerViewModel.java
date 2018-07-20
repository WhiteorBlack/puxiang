package com.puxiang.mall.module.dealer.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.model.data.RxArea;
import com.puxiang.mall.model.data.RxCity;
import com.puxiang.mall.model.data.RxDealer;
import com.puxiang.mall.model.data.RxLocation;
import com.puxiang.mall.model.data.RxShopList;
import com.puxiang.mall.module.dealer.adapter.DealerAdapter;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/3.
 */
public class DealerViewModel extends BaseObservable implements ViewModel {
    private ObservableField<String> currentCity = new ObservableField<>("郑州");
    private BaseBindActivity activity;
    private boolean isUpdate = false;  //只有第一次进入获取定位数据，之后更新定位只更新当前位置，不跟新商铺
    private int pageNo = 1;
    private String areaCode;
    private DealerAdapter adapter;
    private LoadingWindow loadingWindow;

    public DealerViewModel(BaseBindActivity activity) {
        this.activity = activity;
        loadingWindow=new LoadingWindow(activity);
        EventBus.getDefault().register(this);
        loadingWindow.delayedShowWindow();
    }

    public DealerViewModel(BaseBindActivity activity, DealerAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public void getCurrentLocation(double lat, double lng) {
        if (isUpdate){
            return;
        }
        ApiWrapper.getInstance()
                .getLocation(lat, lng)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxLocation>() {
                    @Override
                    public void onSuccess(RxLocation data) {
                        loadingWindow.hidWindow();
                        postCode(data.getCode());
                        if (isUpdate) {
                            setCurrentCity(data.getCity());
                            return;
                        }
                        isUpdate = true;
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RxArea rxCity) {
        if (rxCity != null) {
            setCurrentCity(rxCity.getName());
        }
    }

    private void postCode(String code) {
        EventBus.getDefault().post(code);
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
