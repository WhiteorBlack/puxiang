package com.puxiang.mall.module.dealer.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.model.data.RxDealerCheck;
import com.puxiang.mall.mvvm.base.ViewModel;

/**
 * Created by zhaoyong bai on 2018/6/3.
 */
public class StatueViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private ObservableField<RxDealerCheck> rxDealer = new ObservableField<>();

    public StatueViewModel(BaseBindActivity activity) {
        this.activity = activity;
        initData();
    }

    private void initData() {
        Intent intent=activity.getIntent();
        String data=intent.getStringExtra("data");
        if (!TextUtils.isEmpty(data)){
            RxDealerCheck rxDealerCheck=new Gson().fromJson(data,RxDealerCheck.class);
            setRxDealer(rxDealerCheck);
        }
    }

    @Bindable
    public RxDealerCheck getRxDealer() {
        return rxDealer.get();
    }

    public void setRxDealer(RxDealerCheck rxDealer) {
        this.rxDealer.set(rxDealer);
        notifyPropertyChanged(BR.rxDealer);
    }

    @Override
    public void destroy() {
    }
}
