package com.puxiang.mall.module.personal.adapter;

import android.databinding.ViewDataBinding;

import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxReplyPostComment;
public class ReplyAdapter extends EasyBindQuickAdapter<RxReplyPostComment> {

    private boolean isOwner;

    public ReplyAdapter(int layoutResId) {
        super(layoutResId);
    }

    public void setOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxReplyPostComment item) {
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(BR.isOwner, isOwner);
        holder.addOnClickListener(R.id.tv_comment1)
                .addOnClickListener(R.id.tv_comment2)
                .addOnClickListener(R.id.iv_del_icon)
                .addOnClickListener(R.id.tv_more_comment)
                .addOnClickListener(R.id.tv_title)
                .addOnClickListener(R.id.sdv_user_pic)
                .addOnClickListener(R.id.tv_time)
                .addOnClickListener(R.id.tv_user_name);
    }
}
