package com.puxiang.mall.module.plate.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxPlate;
import com.puxiang.mall.utils.AutoUtils;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class PlateRightAdapter extends BaseQuickAdapter<RxPlate, PlateRightAdapter.PlateHolder> {
    public PlateRightAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(PlateHolder helper, RxPlate item) {
        helper.getBinding().setVariable(BR.item,item);
        helper.getBinding().executePendingBindings();
        helper.getBinding().getRoot().setTag(item);
    }

    @Override
    protected View getItemView(@LayoutRes int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view=binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_databinding_support,binding);
        AutoUtils.auto(view);
        return view;
    }

    public static class PlateHolder extends BaseViewHolder {

        public PlateHolder(View view) {
            super(view);
        }

        public ViewDataBinding getBinding() {
            return (ViewDataBinding) itemView.getTag(R.id.BaseQuickAdapter_databinding_support);
        }
    }
}
