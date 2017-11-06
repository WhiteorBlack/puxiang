package com.puxiang.mall.module.post.model;

public class ClickEvent {
    public ClickEvent(String url) {
        this.url = url;
    }

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
