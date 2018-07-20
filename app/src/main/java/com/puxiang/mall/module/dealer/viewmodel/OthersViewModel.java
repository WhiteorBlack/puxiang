package com.puxiang.mall.module.dealer.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.puxiang.mall.R;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxDealer;
import com.puxiang.mall.model.data.RxDealerCheck;
import com.puxiang.mall.module.dealer.adapter.MineAdapter;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/3.
 */
public class OthersViewModel implements ViewModel {
    private BaseBindFragment fragment;
    private MineAdapter adapter;

    public OthersViewModel(BaseBindFragment fragment, MineAdapter adapter) {
        this.fragment = fragment;
        this.adapter = adapter;
    }


    public void getDealerList(int pageNo) {
        ApiWrapper.getInstance()
                .getDealerNot(pageNo)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxDealerCheck>>() {
                    @Override
                    public void onSuccess(List<RxDealerCheck> data) {
                        adapter.setNewData(data);
                    }
                });

    }

    private void applyDealer(String ids) {
        ApiWrapper.getInstance().applyDealer(ids)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                        dealData();
                    }
                });
    }

    private void dealData() {
        List<RxDealerCheck> rxDealerChecks = adapter.getData();
        if (rxDealerChecks != null && rxDealerChecks.size() > 0) {

            for (int i = 0; i < rxDealerChecks.size(); i++) {
                if (rxDealerChecks.get(i).isSelected()) {
                    rxDealerChecks.get(i).setAuditStatus(1);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }


    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapterT, View view, int position) {
                if (adapter.getData().get(position).getAuditStatus()==3){
                    String data=new Gson().toJson(adapter.getData().get(position),RxDealerCheck.class);
                    ActivityUtil.startDealerStatue(fragment.getActivity(),data);
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapterT, View view, int position) {
                super.onItemChildClick(adapterT, view, position);
                adapter.getData().get(position).setSelected(true);
                switch (view.getId()) {
                    case R.id.iv_goods_down:
                        //重新申请
                        applyDealer(adapter.getData().get(position).getDealerId());
                        break;
                }
            }
        };
    }


    @Override
    public void destroy() {

    }
}
