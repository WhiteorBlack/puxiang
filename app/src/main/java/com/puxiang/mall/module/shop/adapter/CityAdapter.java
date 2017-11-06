package com.puxiang.mall.module.shop.adapter;

import android.view.View;

import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxCity;
import com.puxiang.mall.utils.AutoUtils;

/**
 * Created by zhaoyong bai on 2017/10/24.
 */

public class CityAdapter extends EasyBindQuickAdapter<RxCity> {
    public CityAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxCity item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
//        AutoUtils.auto(holder.getBinding().getRoot());
    }

}
