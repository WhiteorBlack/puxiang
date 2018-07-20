package com.puxiang.mall.module.classify.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.MallData;
import com.puxiang.mall.model.data.RxClassfy;
import com.puxiang.mall.model.data.RxClassfyRight;
import com.puxiang.mall.model.data.RxClassfyRightItem;
import com.puxiang.mall.module.mall.adapter.SectionAdapterNew;
import com.puxiang.mall.utils.AutoUtils;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/4/2.
 */
public class ClassfyRightSectionAdapter extends BaseSectionQuickAdapter<RxClassfyRightItem, SectionAdapterNew.MovieViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public ClassfyRightSectionAdapter(int layoutResId, int sectionHeadResId, List<RxClassfyRightItem> data) {
        super(layoutResId, sectionHeadResId, data);
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

    @Override
    protected void convertHead(SectionAdapterNew.MovieViewHolder helper, RxClassfyRightItem item) {
        helper.getBinding().setVariable(BR.title, item.header);
        helper.getBinding().executePendingBindings();
    }

    @Override
    protected void convert(SectionAdapterNew.MovieViewHolder helper, RxClassfyRightItem item) {
        helper.getBinding().setVariable(BR.item, item);
        helper.getBinding().executePendingBindings();
        helper.getBinding().getRoot().setTag(item);
    }
}
