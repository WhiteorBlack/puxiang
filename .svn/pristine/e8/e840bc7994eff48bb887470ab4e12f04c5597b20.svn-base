package com.puxiang.mall.model.data;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by zhaoyong bai on 2017/9/12.
 */

public class ShopCartData<T> extends SectionEntity {
    private String shopName;
    private String shopId;
    private String logoUrl;
    private  RxCartProduct cartProduct;
    private boolean isSelected=false;
    private boolean isHeader=false;
    public ShopCartData(boolean isHeader, String header) {
        super(isHeader, header);
        this.isHeader=isHeader;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public ShopCartData(T t){
        super(t);

    }
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public RxCartProduct getCartProduct() {
        return cartProduct;
    }

    public void setCartProduct(RxCartProduct cartProduct) {
        this.cartProduct = cartProduct;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;

    }
}
