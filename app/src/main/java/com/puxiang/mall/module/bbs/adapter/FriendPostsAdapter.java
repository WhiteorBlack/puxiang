package com.puxiang.mall.module.bbs.adapter;

import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.module.bbs.viewmodel.BbsRequest;

public class FriendPostsAdapter extends EasyBindQuickAdapter<RxPostInfo> {


    public FriendPostsAdapter(int layoutResId, BbsRequest.RefreshListener refreshListener) {
        super(layoutResId, refreshListener);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxPostInfo item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
        holder.addOnClickListener(R.id.tv_from)
                .addOnClickListener(R.id.tv_from_hint)
                .addOnClickListener(R.id.tv_time)
                .addOnClickListener(R.id.tv_user_name)
                .addOnClickListener(R.id.sdv_bbs_pic)
                .addOnClickListener(R.id.iv_like_icon);
    }
}
