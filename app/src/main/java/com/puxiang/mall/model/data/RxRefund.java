package com.puxiang.mall.model.data;

import java.util.List;

public class RxRefund {

    /**
     * orderPayDate : 2016-12-01 17:38:20
     * productNumber : 1
     * refundMoney : 0.01
     * price : 0.01
     * mainPictureUrl : http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201611/d6e49124dd014d06a9c21947af50f6ca45940.jpg?430x430
     * refundReasons : [{"refundReasonDes":"收到商品破损","refundReasonId":7},{"refundReasonDes":"商品错发/漏发","refundReasonId":8},{"refundReasonDes":"商品需要维护","refundReasonId":9},{"refundReasonDes":"发票问题","refundReasonId":10},{"refundReasonDes":"收到商品与描述不符","refundReasonId":11},{"refundReasonDes":"商品质量问题","refundReasonId":12},{"refundReasonDes":"未收到货","refundReasonId":13},{"refundReasonDes":"未按约定时间发货","refundReasonId":14},{"refundReasonDes":"退运费","refundReasonId":15}]
     * orderStatus : payed
     * productName :
     * orderId : 201612011735344333460
     * totalPrice : 0.01
     */

    private String orderPayDate;
    private int productNumber;
    private double refundMoney;
    private double price;
    private String mainPictureUrl;
    private String orderStatus;
    private String productName;
    private String orderId;
    private double totalPrice;
    /**
     * refundReasonDes : 收到商品破损
     * refundReasonId : 7
     */

    private List<RxRefundReasons> refundReasons;

    public String getOrderPayDate() {
        return orderPayDate;
    }

    public void setOrderPayDate(String orderPayDate) {
        this.orderPayDate = orderPayDate;
    }

    public int getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(int productNumber) {
        this.productNumber = productNumber;
    }

    public double getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(double refundMoney) {
        this.refundMoney = refundMoney;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMainPictureUrl() {
        return mainPictureUrl;
    }

    public void setMainPictureUrl(String mainPictureUrl) {
        this.mainPictureUrl = mainPictureUrl;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<RxRefundReasons> getRefundReasons() {
        return refundReasons;
    }

    public void setRefundReasons(List<RxRefundReasons> refundReasons) {
        this.refundReasons = refundReasons;
    }
}
