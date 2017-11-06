package com.puxiang.mall.module.mall.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.puxiang.mall.model.data.RxAds;
import com.puxiang.mall.module.mall.adapter.MallClassAdapter;
import com.puxiang.mall.module.mall.view.MallFragment;
import com.puxiang.mall.mvvm.base.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/9/5.
 */

public class MallClassViewModel  extends BaseObservable implements ViewModel {
    private Activity activity;
    private MallFragment fragment;
    public List<RxAds> classList=new ArrayList<>();
    public static MallClassAdapter adapter;

    public MallClassViewModel(MallFragment fragment,MallClassAdapter adapter){
        this.fragment=fragment;
        this.activity=fragment.getActivity();
        this.adapter=adapter;
    }

    @Bindable
    public List<RxAds> getClassList() {
        return classList;
    }

    public void setClassList(List<RxAds> classList) {
        this.classList = classList;
    }

    @BindingAdapter("class")
    public static void setClass(RecyclerView recyclerView,List<RxAds> list){
        adapter.setNewData(list);
    }


    public void getClassData(){

    }

    @Override
    public void destroy() {

    }
}
