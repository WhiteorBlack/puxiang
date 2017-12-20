package com.puxiang.mall.model.data;

/**
 * Created by zhaoyong bai on 2017/12/7.
 */

public class RxOrderState {
    private int buyerWaitComment;
    private int buyerWaitPay;
    private int buyerWaitRefund;
    private int buyerWaitDeliveryGoods;
    private int buyerWaitReceiveGoods;
    private int sellerWaitPay;
    private int sellerWaitRefund;
    private int sellerWaitReceiveGoods;
    private int sellerWaitDeliveryGoods;

    public int getBuyerWaitComment() {
        return buyerWaitComment;
    }

    public void setBuyerWaitComment(int buyerWaitComment) {
        this.buyerWaitComment = buyerWaitComment;
    }

    public int getBuyerWaitPay() {
        return buyerWaitPay;
    }

    public void setBuyerWaitPay(int buyerWaitPay) {
        this.buyerWaitPay = buyerWaitPay;
    }

    public int getBuyerWaitRefund() {
        return buyerWaitRefund;
    }

    public void setBuyerWaitRefund(int buyerWaitRefund) {
        this.buyerWaitRefund = buyerWaitRefund;
    }

    public int getBuyerWaitDeliveryGoods() {
        return buyerWaitDeliveryGoods;
    }

    public void setBuyerWaitDeliveryGoods(int buyerWaitDeliveryGoods) {
        this.buyerWaitDeliveryGoods = buyerWaitDeliveryGoods;
    }

    public int getBuyerWaitReceiveGoods() {
        return buyerWaitReceiveGoods;
    }

    public void setBuyerWaitReceiveGoods(int buyerWaitReceiveGoods) {
        this.buyerWaitReceiveGoods = buyerWaitReceiveGoods;
    }

    public int getSellerWaitPay() {
        return sellerWaitPay;
    }

    public void setSellerWaitPay(int sellerWaitPay) {
        this.sellerWaitPay = sellerWaitPay;
    }

    public int getSellerWaitRefund() {
        return sellerWaitRefund;
    }

    public void setSellerWaitRefund(int sellerWaitRefund) {
        this.sellerWaitRefund = sellerWaitRefund;
    }

    public int getSellerWaitReceiveGoods() {
        return sellerWaitReceiveGoods;
    }

    public void setSellerWaitReceiveGoods(int sellerWaitReceiveGoods) {
        this.sellerWaitReceiveGoods = sellerWaitReceiveGoods;
    }

    public int getSellerWaitDeliveryGoods() {
        return sellerWaitDeliveryGoods;
    }

    public void setSellerWaitDeliveryGoods(int sellerWaitDeliveryGoods) {
        this.sellerWaitDeliveryGoods = sellerWaitDeliveryGoods;
    }

    public int getSellerCount(){
        return this.sellerWaitDeliveryGoods+this.sellerWaitPay+this.sellerWaitReceiveGoods;
    }
}
