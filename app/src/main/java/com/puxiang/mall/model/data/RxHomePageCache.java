package com.puxiang.mall.model.data;

import java.util.List;

public class RxHomePageCache {
    private List<RxChannel> list;

    public List<RxChannel> getList() {
        return list;
    }

    public void setList(List<RxChannel> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "{list=" + list +'}';
    }
}
