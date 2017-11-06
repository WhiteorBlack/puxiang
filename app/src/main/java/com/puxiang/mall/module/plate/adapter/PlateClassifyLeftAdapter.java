package com.puxiang.mall.module.plate.adapter;

import android.graphics.Color;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxPlateType;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.widget.verticaltablayout.TabAdapter;
import com.puxiang.mall.widget.verticaltablayout.widget.QTabView;

import java.util.ArrayList;
import java.util.List;

public class PlateClassifyLeftAdapter implements TabAdapter {

    private List<RxPlateType> mList;

    public PlateClassifyLeftAdapter(List<RxPlateType> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    public void addData(List<RxPlateType> list) {
        mList.addAll(list);
    }

    public RxPlateType getItemData(int i) {
        return mList.get(i);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getBadge(int position) {
        return 0;
    }

    @Override
    public QTabView.TabIcon getIcon(int position) {
        return null;
    }

    @Override
    public QTabView.TabTitle getTitle(int position) {
        return new QTabView.TabTitle.Builder(MyApplication.getContext())
                .setContent(mList.get(position).getName())
                .setTextColor(AppUtil.getColor(R.color.sale_price), Color.parseColor("#384258"))
                .setTextSize(28)
                .setPadding(0, 40, 0, 40)
                .build();
    }

    @Override
    public int getBackground(int position) {
        return 0;
    }
}