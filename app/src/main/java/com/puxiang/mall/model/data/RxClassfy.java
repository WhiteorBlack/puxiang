package com.puxiang.mall.model.data;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/4/2.
 */
public class RxClassfy {
    private String parentId;
    private String iconUrl;
    private String level;
    private String name;
    private String id;
    private String adChannelCode;
    private List<RxClassfyRight> children;

    public String getAdChannelCode() {
        return adChannelCode;
    }

    public void setAdChannelCode(String adChannelCode) {
        this.adChannelCode = adChannelCode;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RxClassfyRight> getChildren() {
        return children;
    }

    public void setChildren(List<RxClassfyRight> children) {
        this.children = children;
    }
}
