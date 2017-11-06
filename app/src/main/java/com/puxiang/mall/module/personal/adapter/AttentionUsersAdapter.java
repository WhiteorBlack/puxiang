package com.puxiang.mall.module.personal.adapter;

import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxAttentionUserData;

public class AttentionUsersAdapter extends EasyBindQuickAdapter<RxAttentionUserData> {
    public AttentionUsersAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxAttentionUserData item) {
        holder.addOnClickListener(R.id.tv_attention);
    }
}
