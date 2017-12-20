package com.puxiang.mall.module.my.viewmodel;

import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.module.my.adapter.CollectionAdapter;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;

/**
 * Created by zhaoyong bai on 2017/12/11.
 */

public class CollectionViewModel implements ViewModel {
    private BaseBindActivity activity;
    private CollectionAdapter adapter;
    private int pageSize = 10;
    private int pageNo = 1;
    private ObservableBoolean isInit = new ObservableBoolean(false);
    private ObservableBoolean hasMore = new ObservableBoolean(false);

    public CollectionViewModel(BaseBindActivity activity, CollectionAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public void getCollectProducts(int pageSize, int pageNo) {
        ApiWrapper.getInstance()
                .getCollectProducts(pageNo, pageSize)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxList<RxProduct>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(RxList<RxProduct> data) {
                        if (pageNo == 1 && data.getList().size() > 0) {
                            setIsInit(true);
                            if (data.getList().size() > 4) {
                                setHasMore(true);
                            }
                        }

                        adapter.setPagingData(data.getList(), pageNo);
                    }
                });
    }

    public boolean getIsInit() {
        return isInit.get();
    }

    public void setIsInit(boolean isInit) {
        this.isInit.set(isInit);
    }

    public boolean getHasMore() {
        return hasMore.get();
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore.set(hasMore);
    }

    public void loadMore() {
        pageNo++;
        getCollectProducts(pageSize, pageNo);
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        };
    }

    @Override
    public void destroy() {

    }
}
