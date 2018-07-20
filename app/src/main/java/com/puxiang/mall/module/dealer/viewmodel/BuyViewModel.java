package com.puxiang.mall.module.dealer.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxDealerCheck;
import com.puxiang.mall.module.dealer.adapter.MineAdapter;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/4.
 */
public class BuyViewModel implements ViewModel {
    private BaseBindActivity activity;
    private MineAdapter adapter;
    private int pageNo = 1;

    public BuyViewModel(BaseBindActivity activity, MineAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public void loadMore() {
        pageNo++;
        getDealerList(pageNo);
    }

    public void getDealerList(int pageNo) {
        ApiWrapper.getInstance()
                .getDealerMine(pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxDealerCheck>>() {
                    @Override
                    public void onSuccess(List<RxDealerCheck> data) {
                        adapter.setPagingData(data, pageNo);
                    }
                });

    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapterT, View view, int position) {
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adaptera, View view, int position) {
                super.onItemChildClick(adaptera, view, position);
                switch (view.getId()) {
                    case R.id.iv_goods_down:
                        ActivityUtil.startShopDetialActivity(activity, adapter.getData().get(position).getShopId());
                        break;
                }
            }
        };
    }

    @Override
    public void destroy() {

    }
}
