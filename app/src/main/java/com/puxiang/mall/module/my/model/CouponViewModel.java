package com.puxiang.mall.module.my.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.module.my.adapter.CouponAdapter;
import com.puxiang.mall.mvvm.base.ViewModel;

/**
 * Created by zhaoyong bai on 2017/12/25.
 */

public class CouponViewModel implements ViewModel {
    private BaseBindActivity activity;
    private CouponAdapter adapter;

    public CouponViewModel(BaseBindActivity activity, CouponAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener(){
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
            }
        };
    }

    @Override
    public void destroy() {

    }
}
