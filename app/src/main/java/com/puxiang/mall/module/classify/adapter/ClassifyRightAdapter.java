package com.puxiang.mall.module.classify.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.utils.AutoUtils;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class ClassifyRightAdapter extends BaseQuickAdapter<RxProduct,ClassifyRightAdapter.DataBindingHolder> {

    public ClassifyRightAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(DataBindingHolder helper, RxProduct item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.item, item);
        binding.executePendingBindings();
        helper.getBinding().getRoot().setTag(item);
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding);
        AutoUtils.auto(view);
        return view;
    }

    public static class DataBindingHolder extends BaseViewHolder {

        public DataBindingHolder(View view) {
            super(view);
        }

        public ViewDataBinding getBinding() {
            return (ViewDataBinding) itemView.getTag(R.id.BaseQuickAdapter_databinding_support);
        }
    }
}
