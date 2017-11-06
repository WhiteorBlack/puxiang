package com.puxiang.mall.module.mall.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.puxiang.mall.BR;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.model.data.RxAds;
import com.puxiang.mall.module.mall.adapter.MallClassAdapter;
import com.puxiang.mall.module.mall.view.MallFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.widget.MyBanner;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by 123 on 2017/9/5.
 */

public class MallHeadViewModel extends BaseObservable implements ViewModel, BGABanner.Delegate<View, RxAds>, BGABanner.Adapter, BaseQuickAdapter.OnItemClickListener {
    private Activity activity;
    private MallFragment fragment;
    public List<RxAds> bannerList = new ArrayList<>();
    private List<RxAds> picList = new ArrayList<>();
    private List<RxAds> hotList = new ArrayList<>();
    private MallClassAdapter adapter;
    private ObservableField<String> hotLineOne = new ObservableField<>("");
    private ObservableField<String> hotLineTwo = new ObservableField<>("");
    private ObservableField<String> onePic = new ObservableField<>("");
    private ObservableField<String> twoPic = new ObservableField<>("");
    private ObservableField<String> threePic = new ObservableField<>("");
    private ObservableField<String> fourPic = new ObservableField<>("");


    public MallHeadViewModel(MallFragment fragment, MallClassAdapter adapter) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
        this.adapter = adapter;
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void fillBannerItem(BGABanner banner, View itemView, Object model, int position) {
        ((SimpleDraweeView) itemView).setImageURI(((RxAds) model).getPicUrl());
    }


    @BindingAdapter("banner")
    public static void setBanner(MyBanner banner, List<RxAds> list) {
        if (list == null || list.size() < 1) {
            return;
        }

        List<String> tips = new ArrayList<>();
        for (RxAds ads : list) {
            tips.add(ads.getText());
        }
        banner.setData(R.layout.viewpager_img, list, tips);
    }

    /**
     * 获取首页类别数据
     */
    public void getClassDdata() {
        ApiWrapper.getInstance().getAds(URLs.INDEX_NAVIGATE).
                compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxAds>>() {
                    @Override
                    public void onSuccess(List<RxAds> data) {
                        adapter.setNewData(data);
                    }
                });
    }

    /**
     * 获取轮播图数据
     */
    public void getBannerData() {
        ApiWrapper.getInstance()
                .getAds(URLs.MALL_APP_CAROUSEL)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxAds>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(List<RxAds> bean) {
                        MyApplication.mCache.put(CacheKey.MALL_BANNER, bean);
                        bannerList.clear();
                        bannerList.addAll(bean);
                        notifyPropertyChanged(BR.banner);
                    }
                });
    }

    /**
     * 获取首页蒲象头条
     */
    public void getHotLine() {
        ApiWrapper.getInstance().getAds(URLs.HOT_LINE).compose(fragment.bindUntilEvent(FragmentEvent.DESTROY)).subscribe(new NetworkSubscriber<List<RxAds>>() {
            @Override
            public void onSuccess(List<RxAds> data) {
                hotList.clear();
                hotList.addAll(data);
                if (data != null && data.size() > 1) {
                    hotLineOne.set(data.get(0).getText());
                    hotLineTwo.set(data.get(1).getText());
                }
            }
        });
    }

    @Bindable
    public ObservableField<String> getHotLineOne() {
        return hotLineOne;
    }

    public void setHotLineOne(ObservableField<String> hotLineOne) {
        this.hotLineOne = hotLineOne;
    }

    @Bindable
    public ObservableField<String> getHotLineTwo() {
        return hotLineTwo;
    }

    public void setHotLineTwo(ObservableField<String> hotLineTwo) {
        this.hotLineTwo = hotLineTwo;
    }


    @Bindable
    public List<RxAds> getBanner() {
        return bannerList;
    }


    public void getRecommend() {
        ApiWrapper.getInstance()
                .getAds(URLs.INDEX_REM)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxAds>>() {
                    @Override
                    public void onSuccess(List<RxAds> data) {
                        picList.clear();
                        picList.addAll(data);
                        if (data != null && data.size() > 3) {
                            onePic.set(data.get(0).getPicUrl());
                            twoPic.set(data.get(1).getPicUrl());
                            threePic.set(data.get(2).getPicUrl());
                            fourPic.set(data.get(3).getPicUrl());
                            notifyPropertyChanged(BR.onePic);
                            notifyPropertyChanged(BR.twoPic);
                            notifyPropertyChanged(BR.threePic);
                            notifyPropertyChanged(BR.fourPic);
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

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, RxAds model, int position) {
        try {
            ActivityUtil.startLink(activity, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        RxAds rxAds = (RxAds) view.getTag();
        if (TextUtils.isEmpty(rxAds.getLinkUrl()) && TextUtils.isEmpty(rxAds.getContentType())) {
            ToastUtil.toast("正在开发中，敬请期待...");
            return;
        }
        ActivityUtil.startLink(activity, rxAds);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sdv_activity:
                ActivityUtil.startLink(activity, picList.get(0));
                break;
            case R.id.sdv_top:
                ActivityUtil.startLink(activity, picList.get(1));
                break;
            case R.id.sdv_left:
                ActivityUtil.startLink(activity, picList.get(2));
                break;
            case R.id.sdv_right:
                ActivityUtil.startLink(activity, picList.get(3));
                break;
            case R.id.txt_hot_comment_one:
                ActivityUtil.startLink(activity, picList.get(0));
                break;
            case R.id.txt_hot_comment_two:
                ActivityUtil.startLink(activity, picList.get(1));
                break;
        }
    }

}
