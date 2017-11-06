package com.puxiang.mall.module.shoppingcart.interfacer;

/**
 * Created by zhaoyong bai on 2017/9/13.
 */

public interface ShopSelectListener {
    /**
     * 店铺的选择，同时更改该店铺下的所有商品的选中状态和全选的状态
     *
     * @param shopId
     * @param isSelected
     */
    void onShopSelectListener(String shopId, boolean isSelected);

    /**
     * 商品选择，同时修改该商品所属店铺的选中状态和全选的状态
     *
     * @param shopId
     * @param goodsId
     * @param isSelected
     */
    void onGoodsSelectListener(String shopId, String goodsId, int pos, boolean isSelected);

    /**
     * 店铺点击事件，跳转到该店铺首页
     *
     * @param shopId
     */
    void onShopClickListener(String shopId);

    /**
     * 商品点击事件，跳转到该商品详情页面
     *
     * @param goodsId
     */
    void onGoodsClickListener(String goodsId);

    /**
     * 购物车增加数量
     *
     * @param pos
     */
    void onAddClickListener(int pos);

    /**
     * 购物车减少数量
     *
     * @param pos
     */
    void onReduceClickListener(int pos);

    /**
     * 删除操作
     *
     * @param pos
     */
    void onDeleteClickListener(int pos);
}
