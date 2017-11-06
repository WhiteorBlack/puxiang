package com.puxiang.mall.module.shop.adapter;

import com.puxiang.mall.BR;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxArea;
import com.puxiang.mall.model.data.RxCityArea;
import com.puxiang.mall.utils.AutoUtils;

import java.util.List;

/**
 * Created by zhaoyong bai on 2017/10/17.
 */

public class CityListAdapter extends EasyBindQuickAdapter<RxArea> {
    public CityListAdapter(int layoutResId, List<RxArea> data) {
        super(layoutResId, data);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxArea item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
//        AutoUtils.auto(holder.getBinding().getRoot());
    }
}
