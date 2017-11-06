package com.puxiang.mall.model.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

/**
 * Created by zhaoyong bai on 2017/10/17.
 */

public class RxLocation extends BaseObservable{
    private String city;
    private String address;
    private String cityCode;
    private String lat;
    private String lng;

    public String getCode() {
        return cityCode;
    }

    public void setCode(String code) {
        this.cityCode = code;
    }

    private ObservableBoolean isVisiable = new ObservableBoolean(false);

    public void setIsVisiable(boolean isVisiable) {
        this.isVisiable.set(isVisiable);
    }

    @Bindable
    public boolean getIsVisiable() {
        return isVisiable.get();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
