package com.puxiang.mall.module.mall.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.BR;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.model.data.MallData;
import com.puxiang.mall.model.data.RxAds;
import com.puxiang.mall.model.data.RxMallInfo;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.module.mall.adapter.SectionAdapterNew;
import com.puxiang.mall.module.mall.view.MallFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.WebUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;


public class MallViewModel extends BaseObservable implements ViewModel {

    private final Activity activity;
    private final SectionAdapterNew adapter;
    private final MallFragment fragment;
    public ObservableField<String> total = new ObservableField<>("0");
    public ObservableBoolean isInitData = new ObservableBoolean(false);
    public ObservableBoolean isVisible = new ObservableBoolean(false);
    public boolean isTop = false;
    public ObservableField<String> searchText = new ObservableField<>("");
    private int floowerCount = 0;
    private List<MallData> mallList = new ArrayList<>();

    public ObservableBoolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible.set(isVisible);
        this.isTop = isVisible;
    }

    public MallViewModel(MallFragment fragment, SectionAdapterNew adapter) {
        this.activity = fragment.getActivity();
        this.fragment = fragment;
        this.adapter = adapter;
    }

    public void getCache() {
        MyApplication.mCache.getAsListBean(CacheKey.MALL_DATA, RxProduct[].class, productList -> {
            if (!productList.isEmpty()) {

            }
        });
    }


    /**
     * 获取购物车数量
     */
    public void getCartNum() {
        if (!StringUtil.isEmpty(MyApplication.TOKEN)) {
            ApiWrapper.getInstance().getCartNum()
                    .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new NetworkSubscriber<Integer>() {
                        @Override
                        public void onSuccess(Integer num) {
//                            fragment.setBadge(num > 99 ? "99+" : String.valueOf(num));
                        }
                    });
        }
    }

    /**
     * 获取推荐商品列表
     */
    public void getRecommendProducts(String recommendType) {
        ApiWrapper.getInstance()
                .getRecommendProducts(recommendType)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxMallInfo>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        fragment.refreshFail();
                    }

                    @Override
                    public void onSuccess(RxMallInfo bean) {
                        if (TextUtils.equals(recommendType, "recommendFloor1")) {
                            floowerCount = 0;
//                            isInitData.set(false);
                            getRecommendProducts("recommendFloor2");

                        }
                        if (TextUtils.equals(recommendType, "recommendFloor2")) {

                            getRecommendProducts("recommendFloor3");
                        }
                        if (TextUtils.equals(recommendType, "recommendFloor3")) {

                            getRecommendProducts("recommendFloor4");
                        }

                        if (TextUtils.equals(recommendType, "recommendFloor4")) {
                            isInitData.set(true);
                            fragment.refreshOK();
                        }
                        setData(bean);
                    }
                });
    }


    /**
     * 设置数据推荐列表
     */
    public void setData(RxMallInfo bean) {
        floowerCount++;
        List<RxProduct> beanList = bean.getData();
        if (beanList == null) return;
        if (floowerCount == 1) {
            mallList.clear();
        }
        MallData mallData = new MallData(true, "");
        mallData.isTitle = true;
        mallData.floower = floowerCount;
        mallData.desc = bean.getDesc();
        mallList.add(mallData);
        for (int j = 0; j < beanList.size(); j++) {
            MallData mall = new MallData(false);
            mall.floower = floowerCount;
            mall.setBen(beanList.get(j));
            mallList.add(mall);
        }

        if (floowerCount == 4) {
            floowerCount=0;
            adapter.setNewData(mallList);
        }
//        if (floowerCount == 1) {
//
//        } else {
//            adapter.notifyDataSetChanged();
//        }
    }


    /**
     * 获取搜索数据
     */
    public void getSearchTextData() {
        ApiWrapper.getInstance()
                .getAds(URLs.TEXT_SEARCH)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxAds>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(List<RxAds> bean) {
                        MyApplication.mCache.put(CacheKey.TEXT_SEARCH, bean);
                        setSerachText(bean);
                    }
                });
    }

    private void setSerachText(List<RxAds> bean) {
        if (bean == null && bean.size() == 0 && TextUtils.isEmpty(bean.get(0).getText())) {
            searchText.set(activity.getString(R.string.home_search_tips));
        } else {
            searchText.set(bean.get(0).getText());
        }
    }

    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MallData mallData = (MallData) view.getTag();
                if (mallData.isTitle) {
                    ActivityUtil.startSearchActivity(activity, "");
                } else {
                    WebUtil.jumpGoodsWeb(activity, mallData.getBean().getProductId());
                }
            }
        };
    }


    public RecyclerView.OnItemTouchListener itemBottomClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MallData mallData = (MallData) view.getTag();
                WebUtil.jumpGoodsWeb(activity, mallData.getBean().getProductId());
            }
        };
    }

    @Override
    public void destroy() {
    }
}
