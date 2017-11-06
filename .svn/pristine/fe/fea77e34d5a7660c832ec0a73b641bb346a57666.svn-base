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
    private ShopSelectListener shopSelectListener;

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

    public void setShopSelectListener(ShopSelectListener shopSelectListener) {
        this.shopSelectListener = shopSelectListener;
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
        helper.getView(R.id.sdv_item_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopSelectListener.onGoodsClickListener(item.getCartProduct().getProductId());
            }
        });
        helper.getView(R.id.tv_item_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopSelectListener.onGoodsClickListener(item.getCartProduct().getProductId());
            }
        });
        helper.getView(R.id.iv_item_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopSelectListener.onGoodsSelectListener(item.getShopId(), item.getCartProduct().getProductId(), helper.getLayoutPosition(), !item.getCartProduct().isSelect());
            }
        });
        helper.getView(R.id.tv_item_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopSelectListener.onAddClickListener(helper.getLayoutPosition());
            }
        });
        helper.getView(R.id.tv_item_reduce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopSelectListener.onReduceClickListener(helper.getLayoutPosition());
            }
        });

        helper.getView(R.id.iv_item_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopSelectListener.onDeleteClickListener(helper.getLayoutPosition());
            }
        });
    }


    @Override
    protected void convertHead(MovieViewHolder helper, ShopCartData item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.shopCart, item);
        binding.executePendingBindings();
        helper.getBinding().getRoot().setTag(item);
        helper.getView(R.id.cb_shop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopSelectListener != null) {
                    shopSelectListener.onShopSelectListener(item.getShopId(), !item.isSelected());
                }
            }
        });
        helper.getView(R.id.tv_shop_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopSelectListener != null) {
                    shopSelectListener.onShopClickListener(item.getShopId());
                }
            }
        });
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
