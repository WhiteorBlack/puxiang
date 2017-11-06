package com.puxiang.mall.module.shop.adapter;

import com.puxiang.mall.BR;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxShop;
import com.puxiang.mall.model.data.RxShopList;
import com.puxiang.mall.utils.AutoUtils;

/**
 * Created by zhaoyong bai on 2017/10/12.
 */

public class ShopListAdapter extends EasyBindQuickAdapter<RxShop> {

    public ShopListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxShop item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
        holder.getBinding().getRoot().setTag(item);
    }
}
