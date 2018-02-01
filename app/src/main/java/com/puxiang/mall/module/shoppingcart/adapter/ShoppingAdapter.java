package com.puxiang.mall.module.shoppingcart.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.ShopCartData;
import com.puxiang.mall.module.shoppingcart.interfacer.ShopSelectListener;
import com.puxiang.mall.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;


public class ShoppingAdapter extends BaseSectionQuickAdapter<ShopCartData, ShoppingAdapter.MovieViewHolder> {
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    List<ShopCartData> mallList = new ArrayList<>();
    private int floowerCount = 0;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ShoppingAdapter(int layoutResId, int sectionHeadResId, List<ShopCartData> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    /**
     * 设置数据推荐列表
     */
    public void setData(List<ShopCartData> beanList) {

        setNewData(beanList);
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
    protected void convert(MovieViewHolder helper, ShopCartData item) {
        helper.getBinding().setVariable(BR.item, item);
        helper.getBinding().executePendingBindings();
        helper.getBinding().getRoot().setTag(item);
        helper.addOnClickListener(R.id.sdv_item_pic)
                .addOnClickListener(R.id.tv_item_name)
                .addOnClickListener(R.id.iv_item_box)
                .addOnClickListener(R.id.tv_item_add)
                .addOnClickListener(R.id.tv_item_reduce)
                .addOnClickListener(R.id.iv_item_close);
    }


    @Override
    protected void convertHead(MovieViewHolder helper, ShopCartData item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.shopCart, item);
        binding.executePendingBindings();
        helper.getBinding().getRoot().setTag(item);
        helper.addOnClickListener(R.id.cb_shop)
                .addOnClickListener(R.id.tv_shop_name);

    }


    public static class MovieViewHolder extends BaseViewHolder {

        public MovieViewHolder(View view) {
            super(view);
        }

        public ViewDataBinding getBinding() {
            return (ViewDataBinding) itemView.getTag(R.id.BaseQuickAdapter_databinding_support);
        }
    }

}
