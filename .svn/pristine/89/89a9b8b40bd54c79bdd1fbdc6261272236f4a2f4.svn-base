package com.puxiang.mall.model.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableInt;

import com.puxiang.mall.BR;

public class RxCartProduct extends BaseObservable {

    /**
     * status : 2
     * skuAttr : 颜色:黑色 套餐:官方标配
     * isNew : y
     * introduce : 重低音 头戴式 电脑震动耳机耳麦
     * cartId : 1150
     * isSpecialOffer : n
     * productId : 10330
     * picture : http://somicshop.oss-cn-shenzhen.aliyuncs.com//attached/image/2016-05-13/g910/left/3.jpg
     * title : 7.1声道电竞游戏耳机 CF/LOL双游戏模式 智能可调节震动 带呼吸LED灯效
     * price : 499
     * stock : 998
     * hit : 265
     * productNum : 1
     * name : G910 重低音 头戴式 电脑震动耳机耳麦
     * skuId : 12014
     * catalogId : 3964
     * salePrice : 349
     */
    private int status;
    private String skuAttr;
    private String isNew;
    private String introduce;
    private String cartId;
    private String isSpecialOffer;
    private String productId;
    private String picture;
    private String title;
    private int price;
    private int stock;
    private int hit;
    private int productNum;
    private String name;
    private int skuId;
    private String catalogId;
    private double salePrice;
    private boolean isSelect;
    private String productName;
    private String mainPictureUrl;
    private int buyQty;
    public ObservableInt buyQtyObser = new ObservableInt(1);

    public int getBuyQty() {
        return buyQty;
    }

    public void setBuyQty(int buyQty) {
        this.buyQty = buyQty;
        setBuyQtyObser(buyQty);
    }

    public void setBuyQtyObser(int buyQty){
        buyQtyObser.set(buyQty);
    }

    public String getMainPictureUrl() {
        return mainPictureUrl;
    }

    public void setMainPictureUrl(String mainPictureUrl) {
        this.mainPictureUrl = mainPictureUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Bindable
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
        notifyPropertyChanged(BR.select);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Bindable
    public String getSkuAttr() {
        return skuAttr;
    }

    public void setSkuAttr(String skuAttr) {
        this.skuAttr = skuAttr;
        notifyPropertyChanged(BR.skuAttr);
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getIsSpecialOffer() {
        return isSpecialOffer;
    }

    public void setIsSpecialOffer(String isSpecialOffer) {
        this.isSpecialOffer = isSpecialOffer;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Bindable
    public String getPicture() {
        return picture;
    }


    public void setPicture(String picture) {
        this.picture = picture;
        notifyPropertyChanged(BR.picture);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    @Bindable
    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
        notifyPropertyChanged(BR.productNum);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    @Bindable
    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
        notifyPropertyChanged(BR.salePrice);
    }

}
