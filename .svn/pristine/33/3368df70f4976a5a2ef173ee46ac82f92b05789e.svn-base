package com.puxiang.mall.model.data;


import com.chad.library.adapter.base.entity.SectionEntity;

public class MallData<T> extends SectionEntity {

    public boolean isLeft = true;
    public boolean isBottom = false;
    public boolean isTop = false;
    public boolean isClass=false;  //是热销还是酒的分类
    public boolean isTitle=false;  //是否是分类标题
    public boolean isMore=false;  //是否有更多按钮
    public int floower=0;          //第几层
    public String desc; //该层描述信息

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public MallData(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MallData(T t) {
        super(t);
    }

    private RxProduct bean;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RxProduct getBean() {
        return bean;
    }

    public void setBen(RxProduct bean) {
        this.bean = bean;
    }
}