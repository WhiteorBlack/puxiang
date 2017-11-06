package com.puxiang.mall.module.integral.viewmodel.model;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.puxiang.mall.model.data.RxIntegral;
import com.puxiang.mall.model.data.RxTasks;

public class IntegralData extends SectionEntity<RxTasks> {

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    private boolean isFirst = false;

    public RxIntegral getIntegral() {
        return integral;
    }

    public void setIntegral(RxIntegral integral) {
        this.integral = integral;
    }

    private RxIntegral integral;

    public IntegralData(boolean isHeader, String header, RxIntegral integral ) {
        super(isHeader, header);
        this.integral = integral;
    }

    public IntegralData(RxTasks tasks) {
        super(tasks);
    }
}

