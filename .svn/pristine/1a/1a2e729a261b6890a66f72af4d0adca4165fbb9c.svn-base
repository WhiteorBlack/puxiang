package com.puxiang.mall.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.puxiang.mall.BR;
import com.puxiang.mall.module.bbs.viewmodel.BbsRequest;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.utils.AutoUtils;

import java.util.List;

/**
 * Desc : 用于简化Adapter 使用Databinding 后的处理；
 */
public abstract class EasyBindQuickAdapter<T> extends BaseQuickAdapter<T, BindingViewHolder> {

    public EasyBindQuickAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public EasyBindQuickAdapter(List<T> data) {
        super(data);
    }

    public EasyBindQuickAdapter(int layoutResId) {
        super(layoutResId);
    }

    public EasyBindQuickAdapter(int layoutResId, BbsRequest.RefreshListener refreshListener) {
        super(layoutResId);
        this.refreshListener = refreshListener;
    }

    private BbsRequest.RefreshListener refreshListener;

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        View view = mLayoutInflater.inflate(layoutResId, parent, false);
        //自动适配view
        AutoUtils.auto(view);
        return view;
    }

    /**
     * 使用 DataBinding.inflate itemView
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    protected BindingViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, mLayoutResId, parent, false);
        return new BindingViewHolder<>(binding);
    }

    /**
     * 对ItemView 进行viewModel 的绑定
     *
     * @param holder
     * @param item
     */
    @Override
    protected void convert(BindingViewHolder holder, T item) {
        easyConvert(holder, item);
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(BR.item, item);
        binding.executePendingBindings();
    }

    protected abstract void easyConvert(BindingViewHolder holder, T item);

    public void setRefreshListener(BbsRequest.RefreshListener listener) {
        this.refreshListener = listener;
    }

    /**
     * 添加分页数据
     *
     * @param list   数据源
     * @param pageNo 页码
     */
    public void setPagingData(List<T> list, int pageNo) {
        if (pageNo == 1 && refreshListener != null) {
            refreshListener.refreshOK();
        }
        if (list == null) return;
        if (pageNo == 1) {
            setNewData(list);
        } else {
            addData(list);
        }
        if (list.size() < URLs.PAGE_SIZE) {
            loadMoreEnd();
        } else {
            loadMoreComplete();
        }
    }

    /**
     * 加载更多失败
     */
    @Override
    public void loadMoreFail() {
        if (refreshListener != null) {
            refreshListener.refreshFail();
        }
        super.loadMoreFail();
    }
}
