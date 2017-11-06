package com.puxiang.mall.module.personal.adapter;

import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxPlate;

public class UserPlatesAdapter extends EasyBindQuickAdapter<RxPlate> {
    public UserPlatesAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxPlate item) {
        holder.addOnClickListener(R.id.tv_attention);
    }
}
