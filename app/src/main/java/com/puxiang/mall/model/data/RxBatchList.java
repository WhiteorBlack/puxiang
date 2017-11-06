package com.puxiang.mall.model.data;

import java.util.List;

/**
 * Created by zhaoyong bai on 2017/11/6.
 */

public class RxBatchList {
    private int totalCount;
    private List<RxProduct> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<RxProduct> getList() {
        return list;
    }

    public void setList(List<RxProduct> list) {
        this.list = list;
    }
}
