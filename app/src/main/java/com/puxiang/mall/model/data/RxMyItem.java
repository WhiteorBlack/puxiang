package com.puxiang.mall.model.data;

/**
 * Created by zhaoyong bai on 2017/11/14.
 * 我的模块儿中 订单 我是买家 我是买家，设置 数据类型
 */

public class RxMyItem {
    private String name;
    private int resId;
    private boolean hasMsg = false;

    public boolean isHasMsg() {
        return hasMsg;
    }

    public void setHasMsg(boolean hasMsg) {
        this.hasMsg = hasMsg;
    }

    public RxMyItem(String name, int resId) {
        this.name = name;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
