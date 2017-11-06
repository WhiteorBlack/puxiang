package com.puxiang.mall.module.shop.adapter;

import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.model.data.RxShop;

/**
 * Created by zhaoyong bai on 2017/10/12.
 */

public class BuyListAdapter extends EasyBindQuickAdapter<RxProduct> {

    public BuyListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxProduct item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
        holder.getBinding().getRoot().setTag(item);

        holder.addOnClickListener(R.id.iv_reduce)
                .addOnClickListener(R.id.iv_add)
                .addOnClickListener(R.id.cb_select)
                .addOnClickListener(R.id.sdv_item_pic)
                .addOnClickListener(R.id.tv_goods_name)
                .addOnClickListener(R.id.tv_goods_info);
    }
}
