package com.puxiang.mall.module.mall.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.v4.app.Fragment;

import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxAds;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

/**
 * Created by zhaoyong bai on 2017/10/12.
 */

public class MallPicAdds extends BaseObservable implements ViewModel {
    private Activity activity;
    private BaseBindFragment fragment;
    private ObservableField<String> onePic = new ObservableField<>("");
    private ObservableField<String> twoPic = new ObservableField<>("");
    private ObservableField<String> threePic = new ObservableField<>("");
    private ObservableField<String> fourPic = new ObservableField<>("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508132349181&di=5f6a4df15726c661fd3bda1a339e13a0&imgtype=0&src=http%3A%2F%2Fpic2.52pk.com%2Ffiles%2F130422%2F2203644_092722_1_lit.jpg");


    public MallPicAdds(BaseBindFragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
    }

    public void getRecommend() {
        ApiWrapper.getInstance()
                .getAds(URLs.INDEX_REM)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxAds>>() {
            @Override
            public void onSuccess(List<RxAds> data) {
                if (data != null && data.size() > 3) {
                    onePic.set(data.get(0).getPicUrl());
                    twoPic.set(data.get(1).getPicUrl());
                    threePic.set(data.get(2).getPicUrl());
                    fourPic.set("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508132349181&di=5f6a4df15726c661fd3bda1a339e13a0&imgtype=0&src=http%3A%2F%2Fpic2.52pk.com%2Ffiles%2F130422%2F2203644_092722_1_lit.jpg");
                    notifyPropertyChanged(BR.picModel);
                }
            }
        });
    }

    @Bindable
    public String getOnePic() {
        return onePic.get();
    }

    public void setOnePic(ObservableField<String> onePic) {
        this.onePic = onePic;
    }

    @Bindable
    public String getTwoPic() {
        return twoPic.get();
    }

    public void setTwoPic(ObservableField<String> twoPic) {
        this.twoPic = twoPic;
    }

    @Bindable
    public String getThreePic() {
        return threePic.get();
    }

    public void setThreePic(ObservableField<String> threePic) {
        this.threePic = threePic;
    }

    @Bindable
    public String getFourPic() {
        return fourPic.get();
    }

    public void setFourPic(ObservableField<String> fourPic) {
        this.fourPic = fourPic;
    }

    public void destroy() {

    }
}
