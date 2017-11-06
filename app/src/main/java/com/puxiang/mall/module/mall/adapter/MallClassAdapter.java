package com.puxiang.mall.module.mall.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.puxiang.mall.BR;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxAds;

import java.util.List;

/**
 * Created by 123 on 2017/9/5.
 */

public class MallClassAdapter extends EasyBindQuickAdapter<RxAds> {

    public MallClassAdapter(@LayoutRes int layoutResId, @Nullable List<RxAds> data) {
        super(layoutResId, data);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxAds item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
        holder.itemView.setTag(item);
    }

}
