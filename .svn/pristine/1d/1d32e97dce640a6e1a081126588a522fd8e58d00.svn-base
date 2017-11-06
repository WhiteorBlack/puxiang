package com.puxiang.mall.module.integral.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.model.data.RxIntegral;
import com.puxiang.mall.model.data.RxTasks;
import com.puxiang.mall.module.integral.viewmodel.model.IntegralData;
import com.puxiang.mall.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

public class IntegralAdapter extends BaseSectionQuickAdapter<IntegralData, BindingViewHolder> {

    public IntegralAdapter(int layoutResId, int sectionHeadResId, List<IntegralData> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        View view = mLayoutInflater.inflate(layoutResId, parent, false);
        AutoUtils.auto(view);
        return view;
    }

    @Override
    protected BindingViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = viewType == SECTION_HEADER_VIEW ? DataBindingUtil.inflate
                (mLayoutInflater, mSectionHeadResId, parent, false) : DataBindingUtil.inflate
                (mLayoutInflater, mLayoutResId, parent, false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    protected void convertHead(BindingViewHolder helper, IntegralData item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.item, item);
        binding.executePendingBindings();
    }

    @Override
    protected void convert(BindingViewHolder holder, IntegralData integralData) {
        RxTasks bean = integralData.t;
        holder.getBinding().setVariable(BR.item, bean);
        holder.getBinding().executePendingBindings();
        holder.addOnClickListener(R.id.iv);
    }

    public void setData(List<RxIntegral> list) {
        List<IntegralData> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            RxIntegral rxIntegral = list.get(i);
            IntegralData integralData = new IntegralData(true, "", rxIntegral);
            if (i == 0) {
                integralData.setFirst(true);
            }
            data.add(integralData);
            for (RxTasks tasks : rxIntegral.getTasks()) {
                IntegralData integral = new IntegralData(tasks);
                data.add(integral);
            }
            rxIntegral.getTasks().clear();
        }
        setNewData(data);
    }
}