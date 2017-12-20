package com.puxiang.mall.module.my.adapter;

import com.puxiang.mall.BR;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxProduct;

import java.util.List;

/**
 * Created by zhaoyong bai on 2017/12/11.
 */

public class CollectionAdapter extends EasyBindQuickAdapter<RxProduct> {
    public CollectionAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxProduct item) {
        holder.getBinding().setVariable(BR.item,item);
        holder.getBinding().executePendingBindings();
    }
}
