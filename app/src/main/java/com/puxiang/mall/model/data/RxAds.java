package com.puxiang.mall.model.data;

public class RxAds {

    /**
     * text :
     * linkUrl : http://m.esomic.com/mission_center.html
     * picUrl : http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201611/777c48fbceb6449483a1d16d726abc9710829.png
     * channelCode : indexNavigate
     * linkContent :
     * contentType : integralTask
     * adCode : integral
     * contentId :
     * adName : 去赚积分
     */


    public String channelCode;
    public String adCode;
    public String adName;
    public String picUrl;
    public String text;
    public String linkUrl;
    public String linkContent;
    public String contentType;
    public String contentId;
    public String keyword;

    private RxShuohuInfo shuohuInfo;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getLinkContent() {
        return linkContent;
    }

    public void setLinkContent(String linkContent) {
        this.linkContent = linkContent;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public RxShuohuInfo getShuohuInfo() {
        return shuohuInfo;
    }

    public void setShuohuInfo(RxShuohuInfo shuohuInfo) {
        this.shuohuInfo = shuohuInfo;
    }
}
