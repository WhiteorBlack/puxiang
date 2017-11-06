package com.puxiang.mall.module.personal.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.utils.AutoUtils;

import java.util.List;

public class CollectPostAdapter extends BaseMultiItemQuickAdapter<RxPostInfo, BaseViewHolder> {

    private boolean isOwner;

    public CollectPostAdapter(List<RxPostInfo> data) {
        super(data);
        addItemType(RxPostInfo.POST_PLATE, R.layout.item_personal_post);
        addItemType(RxPostInfo.POST_OFFICIAL, R.layout.item_personal_collect);
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        View view = mLayoutInflater.inflate(layoutResId, parent, false);
        AutoUtils.auto(view);
        return view;
    }

    public void setOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        int layoutResId = viewType == RxPostInfo.POST_PLATE ? R.layout.item_personal_post : R
                .layout.item_personal_collect;
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent,
                false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    protected void convert(BaseViewHolder holder, RxPostInfo bean) {
        ViewDataBinding binding = ((BindingViewHolder) holder).getBinding();
        binding.setVariable(BR.item, bean);
        binding.setVariable(BR.isOwner, isOwner);
        binding.executePendingBindings();
        holder.addOnClickListener(R.id.tv_from)
                .addOnClickListener(R.id.iv_del_icon)
                .addOnClickListener(R.id.tv_time);
        if (holder.getItemViewType() == RxPostInfo.POST_PLATE) {
            holder.addOnClickListener(R.id.iv_like_icon)
                    .addOnClickListener(R.id.tv_user_name)
                    .addOnClickListener(R.id.sdv_user_pic);
        }
    }
}
