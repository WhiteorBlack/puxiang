package com.puxiang.mall.module.classify.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.BR;
import com.puxiang.mall.model.data.RxCatalog;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.module.classify.adapter.ClassifyRightAdapter;
import com.puxiang.mall.module.classify.adapter.ProductClassifyLeftAdapter;
import com.puxiang.mall.module.classify.view.ClassifyFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.WebUtil;
import com.puxiang.mall.widget.verticaltablayout.VTabLayout;
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
    private final ClassifyRightAdapter rightAdapter;
    private List<RxCatalog> catalogList = new ArrayList<>();
    private Map<String, List<RxProduct>> sa = new HashMap<>();
    private boolean isBottom = false;

    @Bindable
    public boolean isBottom() {
        return isBottom;
    }

    public void setBottom(boolean bottom) {
        isBottom = bottom;
    }

    public ClassifyViewModel(ClassifyFragment activity, ClassifyRightAdapter rightAdapter) {
        this.activity = activity;
        this.rightAdapter = rightAdapter;
        getLeftData();
    }

    @BindingAdapter("catalogData")
    public static void setCatalogData(VTabLayout vtl, List<RxCatalog> list) {
        if (!list.isEmpty()) {
            vtl.setTabAdapter(new ProductClassifyLeftAdapter(list));
        }
    }

    @Bindable
    public List<RxCatalog> getCatalogList() {
        return this.catalogList;
    }

    /**
     * 详细数据
     *
     * @param i 目录位置
     */
    public void getRightData(final int i) {
        String catalogId = catalogList.get(i).getCatalogId();
        final List<RxProduct> list = sa.get(catalogId);
        if (list != null && list.size() != 0) {
            rightAdapter.setNewData(list);
            return;
        }
        ApiWrapper.getInstance()
                .searchProduct(catalogId)
                .compose(activity.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxList<RxProduct>>() {
                    @Override
                    public void onSuccess(RxList<RxProduct> bean) {
                        setRightData(bean);
                    }
                });

    }

    private void setRightData(RxList<RxProduct> rxProductRxList) {
        List<RxProduct> list = rxProductRxList.getList();
        rightAdapter.setNewData(list);
        if (list.size() > 0) {
            sa.put(list.get(0).getCatalogId(), list);
        }
    }

    /**
     * 获取目录数据
     */
    private void getLeftData() {
        ApiWrapper.getInstance()
                .getCatalogs()
                .subscribe(new NetworkSubscriber<List<RxCatalog>>() {
                    @Override
                    public void onSuccess(List<RxCatalog> bean) {
                        catalogList.addAll(bean);
                        notifyPropertyChanged(BR.catalogList);
                        getRightData(0);
                    }
                });
    }

    public OnItemClickListener clickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                RxProduct bean = rightAdapter.getData().get(i);
                String id = bean.getProductId();
                WebUtil.jumpGoodsWeb(activity.getContext(), id);
            }
        };
    }

    @Override
    public void destroy() {

    }
}
