package com.puxiang.mall.model.data;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by zhaoyong bai on 2017/11/16.
 */

public class RxPostCity implements IPickerViewData{
    private String text;
    private String value;
    private List<RxPostCity> children;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<RxPostCity> getChildren() {
        return children;
    }

    public void setChildren(List<RxPostCity> children) {
        this.children = children;
    }

    @Override
    public String getPickerViewText() {
        return this.text;
    }
}
