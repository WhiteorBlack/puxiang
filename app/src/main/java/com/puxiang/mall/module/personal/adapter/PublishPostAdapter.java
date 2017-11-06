package com.puxiang.mall.module.personal.adapter;

import android.databinding.ViewDataBinding;

import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxPostInfo;

public class PublishPostAdapter extends EasyBindQuickAdapter<RxPostInfo> {

    private boolean isOwner;

    public PublishPostAdapter(int layoutResId) {
        super(layoutResId);
    }

    public void setOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxPostInfo item) {
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(BR.isOwner, isOwner);
        binding.setVariable(BR.item,item);
        holder.addOnClickListener(R.id.iv_del_icon)
                .addOnClickListener(R.id.tv_user_name)
                .addOnClickListener(R.id.tv_from)
                .addOnClickListener(R.id.iv_like_icon)
                .addOnClickListener(R.id.sdv_user_pic)
                .addOnClickListener(R.id.tv_time)
                .addOnClickListener(R.id.tv_user_name);
        binding.executePendingBindings();
    }
}
