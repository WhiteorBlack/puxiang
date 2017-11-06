package com.puxiang.mall.model.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import com.puxiang.mall.BR;

import java.util.List;

/**
 * Created by zhaoyong bai on 2017/10/25.
 * 商家列表页面，选择城市下拉一级列表
 */

public class RxCityArea extends BaseObservable{
    private String code;
    private String name;
    private List<RxArea> children;
    private ObservableBoolean isVisiable = new ObservableBoolean(false);

    public void setIsVisiable(boolean isVisiable) {
        this.isVisiable.set(isVisiable);
        notifyPropertyChanged(BR.isVisiable);
    }

    @Bindable
    public boolean getIsVisiable() {
        return isVisiable.get();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RxArea> getChildren() {
        return children;
    }

    public void setChildren(List<RxArea> children) {
        this.children = children;
    }
}
