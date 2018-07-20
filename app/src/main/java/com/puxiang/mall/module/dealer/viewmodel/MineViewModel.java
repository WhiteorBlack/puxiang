package com.puxiang.mall.module.dealer.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.R;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxDealer;
import com.puxiang.mall.model.data.RxDealerCheck;
import com.puxiang.mall.module.dealer.adapter.MineAdapter;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/3.
 */
public class MineViewModel implements ViewModel {
    private BaseBindFragment fragment;
    private MineAdapter adapter;
    private int pageNo = 1;

    public MineViewModel(BaseBindFragment fragment, MineAdapter adapter) {
        this.fragment = fragment;
        this.adapter = adapter;
    }

    public void getDealerList(int pageNo) {
        ApiWrapper.getInstance()
                .getDealerMine(pageNo)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxDealerCheck>>() {
                    @Override
                    public void onSuccess(List<RxDealerCheck> data) {
                        adapter.setNewData(data);
                    }
                });

    }

    private void cancelDealer(String ids) {
        ApiWrapper.getInstance().cancelDealer(ids)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                        dealData();
                    }
                });
    }

    public void selectAll() {
        List<RxDealerCheck> rxDealerChecks = adapter.getData();
        if (rxDealerChecks != null && rxDealerChecks.size() > 0) {

            for (int i = 0; i < rxDealerChecks.size(); i++) {
                rxDealerChecks.get(i).setSelected(true);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void link() {
        String ids = "";
        List<RxDealerCheck> rxDealerChecks = adapter.getData();
        if (rxDealerChecks != null && rxDealerChecks.size() > 0) {
            for (int i = 0; i < rxDealerChecks.size(); i++) {
                if (rxDealerChecks.get(i).isSelected()) {
                    ids = ids + "," + rxDealerChecks.get(i).getDealerId();
                }
            }
        }
        cancelDealer(ids);
    }

    public void cancelAll() {
        List<RxDealerCheck> rxDealerChecks = adapter.getData();
        if (rxDealerChecks != null && rxDealerChecks.size() > 0) {
            for (int i = 0; i < rxDealerChecks.size(); i++) {
                rxDealerChecks.get(i).setSelected(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void dealData() {
        List<RxDealerCheck> rxDealerChecks = adapter.getData();
        if (rxDealerChecks != null && rxDealerChecks.size() > 0) {

            for (int i = rxDealerChecks.size() - 1; i >= 0; i--) {
                if (rxDealerChecks.get(i).isSelected()) {
                    rxDealerChecks.remove(i);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }


    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapterT, View view, int position) {
                super.onItemChildClick(adapterT, view, position);
                adapter.getData().get(position).setSelected(true);
                switch (view.getId()) {
                    case R.id.iv_goods_down:
                        //取关
                        cancelDealer(adapter.getData().get(position).getDealerId());
                        break;
                }
            }
        };
    }


    @Override
    public void destroy() {

    }
}
