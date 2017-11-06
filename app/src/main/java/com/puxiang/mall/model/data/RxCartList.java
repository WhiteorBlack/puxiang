package com.puxiang.mall.model.data;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

/**
 * Created by zhaoyong bai on 2017/9/12.
 */

public class RxCartList extends SectionEntity {
    private String shopName;
    private String shopId;
    private String logoUrl;
    private List<RxCartProduct> carts;
    private boolean isSelected=false;

    public RxCartList(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public List<RxCartProduct> getCartList() {
        return carts;
    }

    public void setCartList(List<RxCartProduct> cartList) {
        this.carts = cartList;
    }

}
