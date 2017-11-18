package com.puxiang.mall.module.my.adapter;

import com.puxiang.mall.BR;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxMyItem;

import java.util.List;

/**
 * Created by zhaoyong bai on 2017/11/14.
 */

public class MyItemAdapter extends EasyBindQuickAdapter<RxMyItem> {
    public MyItemAdapter(int layoutResId, List<RxMyItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxMyItem item) {
        holder.getBinding().setVariable(BR.item,item);
        holder.getBinding().executePendingBindings();
    }
}
