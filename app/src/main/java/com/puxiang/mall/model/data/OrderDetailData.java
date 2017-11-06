package com.puxiang.mall.model.data;

public class OrderDetailData {

    /**
     * errorMessage : 业务操作成功
     * token : ec9562f82d4183dc35b6f2eb43122c3a
     * returnObject : {"orderId":"20160812173006284723","score":0,"productName":"G925游戏耳机","price":0.01,"productSkuId":12019,"productId":10328,"lowStocks":"n","total0":0.01,"isComment":"n","specInfo":null,"productSkuAttr":"[{\"attrId\":582,\"attrName\":\"颜色\",\"valueId\":5965,\"value\":\"白色\",\"valuePic\":\"attached/images/201607/dbe867d58ec5418b90a309603244c88e64443.jpg\",\"valueAbsolutePic\":\"http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201607/dbe867d58ec5418b90a309603244c88e64443.jpg\",\"isChecked\":true},{\"attrId\":583,\"attrName\":\"套餐\",\"valueId\":5974,\"value\":\"官方标配\",\"valuePic\":\"\",\"valueAbsolutePic\":\"http://somicshop.oss-cn-shenzhen.aliyuncs.com/\",\"isChecked\":true}]","mainPictureUrl":"/attached/image/2016-05-13/g925/left/2.jpg","afterState":null,"giftId":"","fee":0,"id":1284,"number":1}
     * errorCode : 000
     */

    private String errorMessage;
    private String token;
    /**
     * orderId : 20160812173006284723
     * score : 0
     * productName : G925游戏耳机
     * price : 0.01
     * productSkuId : 12019
     * productId : 10328
     * lowStocks : n
     * total0 : 0.01
     * isComment : n
     * specInfo : null
     * productSkuAttr : [{"attrId":582,"attrName":"颜色","valueId":5965,"value":"白色","valuePic":"attached/images/201607/dbe867d58ec5418b90a309603244c88e64443.jpg","valueAbsolutePic":"http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201607/dbe867d58ec5418b90a309603244c88e64443.jpg","isChecked":true},{"attrId":583,"attrName":"套餐","valueId":5974,"value":"官方标配","valuePic":"","valueAbsolutePic":"http://somicshop.oss-cn-shenzhen.aliyuncs.com/","isChecked":true}]
     * mainPictureUrl : /attached/image/2016-05-13/g925/left/2.jpg
     * afterState : null
     * giftId :
     * fee : 0.0
     * id : 1284
     * number : 1
     */

    private ReturnObjectBean returnObject;
    private String errorCode;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ReturnObjectBean getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(ReturnObjectBean returnObject) {
        this.returnObject = returnObject;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public static class ReturnObjectBean {
        private String orderId;
        private int score;
        private String productName;
        private double price;
        private int productSkuId;
        private String productId;
        private String lowStocks;
        private double total0;
        private String isComment;
        private Object specInfo;
        private String productSkuAttr;
        private String mainPictureUrl;
        private Object afterState;
        private String giftId;
        private double fee;
        private int id;
        private int number;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getProductSkuId() {
            return productSkuId;
        }

        public void setProductSkuId(int productSkuId) {
            this.productSkuId = productSkuId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getLowStocks() {
            return lowStocks;
        }

        public void setLowStocks(String lowStocks) {
            this.lowStocks = lowStocks;
        }

        public double getTotal0() {
            return total0;
        }

        public void setTotal0(double total0) {
            this.total0 = total0;
        }

        public String getIsComment() {
            return isComment;
        }

        public void setIsComment(String isComment) {
            this.isComment = isComment;
        }

        public Object getSpecInfo() {
            return specInfo;
        }

        public void setSpecInfo(Object specInfo) {
            this.specInfo = specInfo;
        }

        public String getProductSkuAttr() {
            return productSkuAttr;
        }

        public void setProductSkuAttr(String productSkuAttr) {
            this.productSkuAttr = productSkuAttr;
        }

        public String getMainPictureUrl() {
            return mainPictureUrl;
        }

        public void setMainPictureUrl(String mainPictureUrl) {
            this.mainPictureUrl = mainPictureUrl;
        }

        public Object getAfterState() {
            return afterState;
        }

        public void setAfterState(Object afterState) {
            this.afterState = afterState;
        }

        public String getGiftId() {
            return giftId;
        }

        public void setGiftId(String giftId) {
            this.giftId = giftId;
        }

        public double getFee() {
            return fee;
        }

        public void setFee(double fee) {
            this.fee = fee;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
