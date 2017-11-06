package com.puxiang.mall.model.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import com.puxiang.mall.BR;

/**
 * Created by zhaoyong bai on 2017/10/17.
 * 城市下面的区域信息
 */

public class RxArea extends BaseObservable {
    private String name;
    private String code;
    private ObservableBoolean isVisiable=new ObservableBoolean(false);
    public void setIsVisiable(boolean isVisiable){
        this.isVisiable.set(isVisiable);
        notifyPropertyChanged(BR.isVisiable);
    }

    @Bindable
    public boolean getIsVisiable(){
        return isVisiable.get();
    }
    @Bindable
    public String getName() {
        return name;
    }

    public void setAreaName(String areaName) {
        this.name = areaName;
    }

    @Bindable
    public String getAreaCode() {
        return code;
    }

    public void setAreaCode(String areaCode) {
        this.code = areaCode;
    }
}
