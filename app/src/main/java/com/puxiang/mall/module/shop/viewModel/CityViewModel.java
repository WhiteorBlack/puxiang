package com.puxiang.mall.module.shop.viewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.BR;
import com.puxiang.mall.model.data.RxArea;
import com.puxiang.mall.model.data.RxCityArea;
import com.puxiang.mall.model.data.RxLocation;
import com.puxiang.mall.module.shop.adapter.CityListAdapter;
import com.puxiang.mall.module.shop.adapter.CityListLeftAdapter;
import com.puxiang.mall.module.shop.dialog.CityDialog;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoyong bai on 2017/10/13.
 */

public class CityViewModel extends BaseObservable implements ViewModel {

    private List<RxCityArea> catalogList = new ArrayList<>();
    private CityListAdapter rightAdapter;
    private CityListLeftAdapter leftAdapter;
    private CityDialog dialog;
    private String areaCode = "";
    public ObservableBoolean isInitData = new ObservableBoolean(false);

    public CityViewModel(CityListAdapter adapter, CityListLeftAdapter leftAdapter, CityDialog dialog) {
        this.rightAdapter = adapter;
        this.leftAdapter = leftAdapter;
        this.dialog = dialog;
    }


    @Bindable
    public List<RxCityArea> getCatalogList() {
        return this.catalogList;
    }


    public void getLocation(String areaCode) {
        if (catalogList != null && catalogList.size() > 0 && TextUtils.equals(areaCode, this.areaCode)) {
            return;
        }
        if (TextUtils.isEmpty(areaCode)) {
            return;
        }
        this.areaCode = areaCode;
        ApiWrapper.getInstance()
                .getAllChildArea(areaCode)
                .subscribe(new NetworkSubscriber<List<RxCityArea>>() {
                    @Override
                    public void onSuccess(List<RxCityArea> data) {
                        catalogList.addAll(data);
                        leftAdapter.setNewData(catalogList);
                        setIsInitData(true);
                    }
                });

    }

    public RecyclerView.OnItemTouchListener areaListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                RxArea rxArea = (RxArea) adapter.getData().get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("type", 0);
                bundle.putString("content", rxArea.getName());
                bundle.putString("code", rxArea.getAreaCode());
                EventBus.getDefault().post(bundle);
                dialog.dismiss();
            }
        };
    }

    public RecyclerView.OnItemTouchListener cityListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<RxArea> areaList = catalogList.get(position).getChildren();
                if (areaList != null && areaList.size() > 0) {
                    for (int i = 0; i < catalogList.size(); i++) {
                        catalogList.get(i).setIsVisiable(position == i);
                    }
                    rightAdapter.setNewData(areaList);
                } else {
                    rightAdapter.setNewData(null);
                    RxCityArea rxArea = catalogList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 0);
                    bundle.putString("content", rxArea.getName());
                    bundle.putString("code", rxArea.getCode());
                    EventBus.getDefault().post(bundle);
                    dialog.dismiss();
                }
                leftAdapter.notifyDataSetChanged();
            }
        };
    }

    @Bindable
    public boolean getIsInitData() {
        return isInitData.get();
    }

    public void setIsInitData(boolean isInitData) {
        this.isInitData.set(isInitData);
        notifyPropertyChanged(BR.isInitData);
    }

    @Override
    public void destroy() {

    }
}
