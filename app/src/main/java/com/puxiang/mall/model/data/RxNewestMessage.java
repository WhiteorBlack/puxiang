package com.puxiang.mall.model.data;


public class RxNewestMessage {
    /**
     * id : 655
     * linkId : 231132123
     * linkUrl : 456465
     * messageTitle : 外链222
     * isLink : 1
     * messagePublishTime : 2017-04-11 16:03:03
     * messageContent : 发发发
     * linkType : integralMall
     * messageType : 1
     * isReaded : 0
     */

    private String id;
    private String linkId;
    private String linkUrl;
    private String messageTitle;
    private int isLink;
    private String messagePublishTime;
    private String messageContent;
    private String linkType;
    private int messageType;
    private int isReaded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public int getIsLink() {
        return isLink;
    }

    public void setIsLink(int isLink) {
        this.isLink = isLink;
    }

    public String getMessagePublishTime() {
        return messagePublishTime;
    }

    public void setMessagePublishTime(String messagePublishTime) {
        this.messagePublishTime = messagePublishTime;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getIsReaded() {
        return isReaded;
    }

    public void setIsReaded(int isReaded) {
        this.isReaded = isReaded;
    }

}
