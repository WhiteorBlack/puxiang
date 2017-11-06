package com.puxiang.mall.model.data;

import java.util.List;

public class RxList<T> {
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "{" +
                "list=" + list +
                '}';
    }
}
