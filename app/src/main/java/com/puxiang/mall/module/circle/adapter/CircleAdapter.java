package com.puxiang.mall.module.circle.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.module.bbs.viewmodel.BbsRequest;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class CircleAdapter extends EasyBindQuickAdapter<RxPostInfo> {
    public CircleAdapter(int layoutResId, BbsRequest.RefreshListener listener) {
        super(layoutResId, listener);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxPostInfo item) {
        holder.addOnClickListener(R.id.tv_user_name)
                .addOnClickListener(R.id.sdv_user_pic)
                .addOnClickListener(R.id.iv_from_icon)
                .addOnClickListener(R.id.tv_from);
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding);
        return view;
    }

    @Override
    protected void convert(BindingViewHolder holder, RxPostInfo item) {
        super.convert(holder, item);
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(BR.item, item);
        binding.executePendingBindings();
        binding.getRoot().setTag(item);
    }
}
