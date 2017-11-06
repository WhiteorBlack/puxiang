package com.puxiang.mall.module.post.adapter;

import com.puxiang.mall.BR;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxReply;

public class CommentDetailQuickAdapter extends EasyBindQuickAdapter<RxReply> {

    public CommentDetailQuickAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxReply item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
    }
}
