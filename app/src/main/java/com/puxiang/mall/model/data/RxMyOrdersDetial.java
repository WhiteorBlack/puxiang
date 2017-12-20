package com.puxiang.mall.model.data;

/**
 * Created by zhaoyong bai on 2017/12/14.
 */

public class RxMyOrdersDetial {
    private String orderDetailId;
    private String productId;
    private String productName;
    private String salePrice;
    private int buyQty;
    private int isComment;
    private double totalAmout;
    private String skuId;
    private String skuAttrShow;
    private String mainPictureUrl;
    private String afterState;

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public int getBuyQty() {
        return buyQty;
    }

    public void setBuyQty(int buyQty) {
        this.buyQty = buyQty;
    }

    public int getIsComment() {
        return isComment;
    }

    public void setIsComment(int isComment) {
        this.isComment = isComment;
    }

    public double getTotalAmout() {
        return totalAmout;
    }

    public void setTotalAmout(double totalAmout) {
        this.totalAmout = totalAmout;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuAttrShow() {
        return skuAttrShow;
    }

    public void setSkuAttrShow(String skuAttrShow) {
        this.skuAttrShow = skuAttrShow;
    }

    public String getMainPictureUrl() {
        return mainPictureUrl;
    }

    public void setMainPictureUrl(String mainPictureUrl) {
        this.mainPictureUrl = mainPictureUrl;
    }

    public String getAfterState() {
        return afterState;
    }

    public void setAfterState(String afterState) {
        this.afterState = afterState;
    }
}
