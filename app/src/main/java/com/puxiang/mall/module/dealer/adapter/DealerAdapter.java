package com.puxiang.mall.module.dealer.adapter;

import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxDealer;
import com.puxiang.mall.model.data.RxDealerCheck;

/**
 * Created by zhaoyong bai on 2018/6/3.
 */
public class DealerAdapter extends EasyBindQuickAdapter<RxDealerCheck> {
    public DealerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxDealerCheck item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
        holder.addOnClickListener(R.id.iv_goods_down)
                .addOnClickListener(R.id.iv_item_box)
                .addOnClickListener(R.id.iv_goods_up);
    }
}
