package com.puxiang.mall.module.post.model;
public class ShareInfo {
    private String url;
    private String title;
    private String imgUrl;
    private String rawUrl;
    private String describe;


    public ShareInfo(String url, String title, String imgUrl, String rawUrl) {
        this.url = url;
        this.title = title;
        this.imgUrl = imgUrl;
        this.rawUrl = rawUrl;
    }

    public ShareInfo(String url, String title, String imgUrl, String rawUrl,String describe) {
        this.url = url;
        this.describe=describe;
        this.title = title;
        this.imgUrl = imgUrl;
        this.rawUrl = rawUrl;
    }


    public void setInfo(String url, String title, String imgUrl, String rawUrl,String describe) {
        this.url = url;
        this.describe=describe;
        this.title = title;
        this.imgUrl = imgUrl;
        this.rawUrl = rawUrl;
    }


    public void setInfo(String url, String title, String imgUrl, String rawUrl) {
        this.url = url;
        this.title = title;
        this.imgUrl = imgUrl;
        this.rawUrl = rawUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRawUrl() {
        return rawUrl;
    }

    public void setRawUrl(String rawUrl) {
        this.rawUrl = rawUrl;
    }

    @Override
    public String toString() {
        return "ShareInfo{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", rawUrl='" + rawUrl + '\'' +
                '}';
    }
}
