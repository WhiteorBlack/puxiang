package com.puxiang.mall.module.shop.viewModel;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.model.data.RxDescShop;
import com.puxiang.mall.module.shop.adapter.DescListAdapter;
import com.puxiang.mall.module.shop.dialog.DescDialog;
import com.puxiang.mall.mvvm.base.ViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2017/10/17.
 */

public class DescViewModel implements ViewModel {
    private DescListAdapter adapterDesc;
    private List<RxDescShop> descShopList = new ArrayList<>();
    private DescDialog dialog;

    public DescViewModel(DescListAdapter adapter, DescDialog dialog) {
        this.adapterDesc = adapter;
        this.dialog = dialog;
    }

    public void getDescData() {
        if (descShopList != null && descShopList.size() > 0) {
            return;
        }
        RxDescShop rxDescShop = new RxDescShop();
        rxDescShop.setDescName("智能排序");
        rxDescShop.setIsSelect(true);
        rxDescShop.setDescCode("defalut");
        descShopList.add(rxDescShop);

        RxDescShop rxDescShop1 = new RxDescShop();
        rxDescShop1.setDescName("离我最近");
        rxDescShop1.setIsSelect(false);
        rxDescShop.setDescCode("nearby");
        descShopList.add(rxDescShop1);

        RxDescShop rxDescShop2 = new RxDescShop();
        rxDescShop2.setDescName("好评优先");
        rxDescShop2.setIsSelect(false);
        rxDescShop.setDescCode("comment");
        descShopList.add(rxDescShop2);

        RxDescShop rxDescShop3 = new RxDescShop();
        rxDescShop3.setDescName("人气最高");
        rxDescShop3.setIsSelect(false);
        rxDescShop.setDescCode("like");
        descShopList.add(rxDescShop3);
        adapterDesc.setNewData(descShopList);
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < descShopList.size(); i++) {
                    descShopList.get(i).setIsSelect(i == position);
                }
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putString("content", descShopList.get(position).getDescName());
                bundle.putString("code",descShopList.get(position).getDescCode());
                EventBus.getDefault().post(bundle);
                adapterDesc.notifyDataSetChanged();
                dialog.dismiss();
            }
        };
    }

    @Override
    public void destroy() {

    }
}
