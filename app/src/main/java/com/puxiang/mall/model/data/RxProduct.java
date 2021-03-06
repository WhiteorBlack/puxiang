package com.puxiang.mall.model.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.facebook.drawee.view.SimpleDraweeView;
import com.puxiang.mall.BR;

public class RxProduct extends BaseObservable {

    private String keywords;
    private int status;
    private String searchKey;
    private String isNew;
    private String isTimePromotion;
    private String introduce;
    private int score;
    private String isSpecialOffer;
    private int recommendType;
    private String productId;
    private String picture;
    private String activityId;
    private String unit;
    private String title;
    private int stock;
    private int type;
    private double price;
    private int hit;
    private String description;
    private String name;
    private String catalogId;
    private String images;
    private String giftId;
    private double salePrice;
    private String sellCount;
    private String mainPictureUrl;
    private double marketPrice;
    private String productName;
    private ObservableBoolean isSelected = new ObservableBoolean(false);
    public boolean manSelected = false;
    private ObservableInt buyCount = new ObservableInt(1);
    private int batchStartQty;
    private ObservableField<Double> batchTotalPrice = new ObservableField<>(0.0d);
    private String prices;
    private String shopId;
    private int isMixBatch;
    private double initTotalPrice;
    private String shopName;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Bindable
    public double getInitTotalPrice() {
        return initTotalPrice;
    }

    public void setInitTotalPrice(double initTotalPrice) {
        this.initTotalPrice = initTotalPrice;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Bindable
    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    @Bindable
    public double getBatchTotalPrice() {
        return batchTotalPrice.get();
    }

    public void setBatchTotalPrice(double batchTotalPrice) {
        this.batchTotalPrice.set(batchTotalPrice);
        notifyPropertyChanged(BR.batchTotalPrice);
    }

    @Bindable
    public int getBatchStartQty() {
        return batchStartQty;
    }

    public void setBatchStartQty(int batchStartQty) {
        this.batchStartQty = batchStartQty;
    }

    @Bindable
    public boolean isSelected() {
        return isSelected.get();
    }

    public void setSelected(boolean selected) {
        isSelected.set(selected);
        notifyPropertyChanged(BR.selected);
    }

    @Bindable
    public int getBuyCount() {
        return buyCount.get();
    }

    public void setBuyCount(int buyCount) {
        this.buyCount.set(buyCount);
    }

    @Bindable
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Bindable
    public String getMainPictureUrl() {
        return mainPictureUrl;
    }

    public void setMainPictureUrl(String mainPictureUrl) {
        this.mainPictureUrl = mainPictureUrl;
    }

    @Bindable
    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getIsTimePromotion() {
        return isTimePromotion;
    }

    public void setIsTimePromotion(String isTimePromotion) {
        this.isTimePromotion = isTimePromotion;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getIsSpecialOffer() {
        return isSpecialOffer;
    }

    public void setIsSpecialOffer(String isSpecialOffer) {
        this.isSpecialOffer = isSpecialOffer;
    }

    public int getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(int recommendType) {
        this.recommendType = recommendType;

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
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    @Bindable
    public String getSellCount() {
        return sellCount;
    }

    public void setSellCount(String sellCount) {
        this.sellCount = sellCount;
    }

    @BindingAdapter("url")
    public static void setUrl(SimpleDraweeView simpleDraweeView, String url) {
        simpleDraweeView.setImageURI(url);
    }

    @Override
    public String toString() {
        return "{" +
                "keywords='" + keywords + '\'' +
                ", status=" + status +
                ", searchKey='" + searchKey + '\'' +
                ", isNew='" + isNew + '\'' +
                ", isTimePromotion='" + isTimePromotion + '\'' +
                ", introduce='" + introduce + '\'' +
                ", score=" + score +
                ", isSpecialOffer='" + isSpecialOffer + '\'' +
                ", recommendType=" + recommendType +
                ", productId='" + productId + '\'' +
                ", picture='" + picture + '\'' +
                ", activityId='" + activityId + '\'' +
                ", unit='" + unit + '\'' +
                ", title='" + title + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                ", hit=" + hit +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", catalogId='" + catalogId + '\'' +
                ", images='" + images + '\'' +
                ", giftId='" + giftId + '\'' +
                ", salePrice=" + salePrice +
                ", sellCount=" + sellCount +
                '}';
    }
}
