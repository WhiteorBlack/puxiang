package com.puxiang.mall.module.classify.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.BR;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxAds;
import com.puxiang.mall.model.data.RxCatalog;
import com.puxiang.mall.model.data.RxClassfy;
import com.puxiang.mall.model.data.RxClassfyRight;
import com.puxiang.mall.model.data.RxClassfyRightItem;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.module.classify.adapter.ClassfyRightSectionAdapter;
import com.puxiang.mall.module.classify.adapter.ClassifyRightAdapter;
import com.puxiang.mall.module.classify.adapter.ProductClassifyLeftAdapter;
import com.puxiang.mall.module.classify.view.ClassifyFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;
import com.puxiang.mall.widget.verticaltablayout.VTabLayout;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Edit by zhaoyongbai on 2017/09/06
 */

public class ClassifyViewModel extends BaseObservable implements ViewModel {
    private final ClassifyFragment activity;
    private final ClassfyRightSectionAdapter rightAdapter;
    private List<RxClassfy> catalogList = new ArrayList<>();
    private Map<String, List<RxClassfyRightItem>> sa = new HashMap<>();
    private Map<String, RxAds> topImage = new HashMap<>();
    private boolean isBottom = false;
    private ObservableField<String> topUrl = new ObservableField<>();
    private RxAds rxAds;

    @Bindable
    public boolean isBottom() {
        return isBottom;
    }

    public void setBottom(boolean bottom) {
        isBottom = bottom;
    }

    public ClassifyViewModel(ClassifyFragment activity, ClassfyRightSectionAdapter rightAdapter) {
        this.activity = activity;
        this.rightAdapter = rightAdapter;
        getLeftData();
    }

    @BindingAdapter("catalogData")
    public static void setCatalogData(VTabLayout vtl, List<RxClassfy> list) {
        if (!list.isEmpty()) {
            vtl.setTabAdapter(new ProductClassifyLeftAdapter(list));
        }
    }

    @Bindable
    public List<RxClassfy> getCatalogList() {
        return this.catalogList;
    }


    public void setRightData(int pos) {
        rxAds = topImage.get(sa.get(catalogList.get(pos).getAdChannelCode()));
        if (rxAds == null) {
            getBannerData(catalogList.get(pos).getAdChannelCode());
        } else {
            setTopUrl(rxAds.picUrl);
        }
        rightAdapter.setNewData(sa.get(catalogList.get(pos).getId()));
    }

    /**
     * 获取目录数据
     */
    private void getLeftData() {
        ApiWrapper.getInstance()
                .getClassfy()
                .compose(activity.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxClassfy>>() {
                    @Override
                    public void onSuccess(List<RxClassfy> bean) {
                        catalogList.addAll(bean);
                        notifyPropertyChanged(BR.catalogList);
                        if (bean != null && bean.size() > 0) {
                            dealData(bean);
                            getBannerData(bean.get(0).getAdChannelCode());
                        }
                    }
                });
    }

    private void dealData(List<RxClassfy> bean) {
        for (int i = 0; i < bean.size(); i++) {
            List<RxClassfyRight> rxClassfyRight = bean.get(i).getChildren();
            List<RxClassfyRightItem> rxClassfyRightItems = new ArrayList<>();

            if (rxClassfyRight != null && rxClassfyRight.size() > 0) {
                for (int j = 0; j < rxClassfyRight.size(); j++) {
                    RxClassfyRight classfyRight = rxClassfyRight.get(j);
                    if (classfyRight.getChildren() != null && classfyRight.getChildren().size() > 0) {
                        RxClassfyRightItem rightItem = new RxClassfyRightItem(true, classfyRight.getName());
                        rxClassfyRightItems.add(rightItem);
                        for (int k = 0; k < classfyRight.getChildren().size(); k++) {
                            RxClassfyRightItem itemP = classfyRight.getChildren().get(k);
                            RxClassfyRightItem item = new RxClassfyRightItem(false, "");
                            item.setIconUrl(itemP.getIconUrl());
                            item.setId(itemP.getId());
                            item.setLevel(itemP.getLevel());
                            item.setName(itemP.getName());
                            item.setParentId(itemP.getParentId());
                            item.setLinkContent(itemP.getLinkContent());
                            item.setLinkType(itemP.getLinkType());
                            rxClassfyRightItems.add(item);
                        }
                    } else {
                        RxClassfyRightItem item = new RxClassfyRightItem(false, "");
                        item.setIconUrl(classfyRight.getIconUrl());
                        item.setId(classfyRight.getId());
                        item.setLevel(classfyRight.getLevel());
                        item.setName(classfyRight.getName());
                        item.setLinkContent(classfyRight.getLinkContent());
                        item.setLinkType(classfyRight.getLinkType());
                        item.setParentId(classfyRight.getParentId());
                        rxClassfyRightItems.add(item);
                    }
                }
            }
            sa.put(bean.get(i).getId(), rxClassfyRightItems);
        }
        setRightData(0);
    }

    /**
     * 获取轮播图数据
     */
    private void getBannerData(String channelCode) {
        ApiWrapper.getInstance()
                .getAds(channelCode)
                .compose(activity.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxAds>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(List<RxAds> bean) {
                        if (bean != null && bean.size() > 0) {
                            rxAds = bean.get(0);
                            topImage.put(channelCode, bean.get(0));
                            setTopUrl(bean.get(0).picUrl);
                        }
                    }
                });
    }

    public void topClick(View view) {
        if (rxAds == null) {
            return;
        }
        ActivityUtil.startLink(activity.getActivity(), rxAds);
    }


    public OnItemClickListener clickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                RxClassfyRightItem item = (RxClassfyRightItem) view.getTag();
                if (item==null){
                    return;
                }
                ActivityUtil.startClassfyLink(activity.getActivity(), item);
            }
        };
    }

    @Bindable
    public String getTopUrl() {
        return topUrl.get();
    }

    public void setTopUrl(String topUrl) {
        this.topUrl.set(topUrl);
        notifyPropertyChanged(BR.topUrl);
    }

    @Override
    public void destroy() {
        sa.clear();
    }
}
