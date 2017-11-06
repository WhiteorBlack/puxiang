package com.puxiang.mall.model.data;

import com.chad.library.adapter.base.entity.MultiItemEntity;


public class PostDetailMultiItemEntity implements MultiItemEntity {
    private int itemType;
    private Object bean;

    public PostDetailMultiItemEntity(int itemType, Object bean) {
        this.itemType = itemType;
        this.bean = bean;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public <E extends Object> E getBean() {
        return (E) bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }
}
