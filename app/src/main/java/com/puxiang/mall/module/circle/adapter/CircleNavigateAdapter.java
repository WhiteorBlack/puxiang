package com.puxiang.mall.module.circle.adapter;

import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxAds;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class CircleNavigateAdapter extends EasyBindQuickAdapter<RxAds> {
    public CircleNavigateAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxAds item) {
    }
}
