package com.puxiang.mall.module.shop.adapter;

import android.graphics.Paint;
import android.widget.TextView;

import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxProduct;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/4/25.
 */
public class GoodsAdapter extends EasyBindQuickAdapter<RxProduct> {
    public GoodsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxProduct item) {
        ((TextView) holder.getBinding().getRoot().findViewById(R.id.tv_item_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.addOnClickListener(R.id.iv_goods_down)
                .addOnClickListener(R.id.iv_item_box)
                .addOnClickListener(R.id.iv_goods_up);
    }
}
