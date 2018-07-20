package com.puxiang.mall.model.data;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by zhaoyong bai on 2018/4/2.
 */
public class RxClassfyRightItem<T> extends SectionEntity{

    private String parentId;
    private String iconUrl;
    private String level;
    private String name;
    private String id;
    private String linkType;
    private String linkContent;

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkContent() {
        return linkContent;
    }

    public void setLinkContent(String linkContent) {
        this.linkContent = linkContent;
    }

    public RxClassfyRightItem(boolean isHeader, String header) {
        super(isHeader, header);
    }

//    private RxClassfyRightItem children;

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

//    public RxClassfyRightItem getChildren() {
//        return children;
//    }
//
//    public void setChildren(RxClassfyRightItem children) {
//        this.children = children;
//    }
}
