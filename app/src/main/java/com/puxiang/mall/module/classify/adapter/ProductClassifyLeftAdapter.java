package com.puxiang.mall.module.classify.adapter;

import android.graphics.Color;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxCatalog;
import com.puxiang.mall.model.data.RxClassfy;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.widget.verticaltablayout.TabAdapter;
import com.puxiang.mall.widget.verticaltablayout.widget.QTabView;

import java.util.ArrayList;
import java.util.List;

public class ProductClassifyLeftAdapter implements TabAdapter {

    private List<RxClassfy> mList;

    public ProductClassifyLeftAdapter(List<RxClassfy> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    public void addData(List<RxClassfy> list) {
        mList.addAll(list);
    }

    public RxClassfy getItemData(int i) {
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
                .setTextColor(Color.parseColor("#FF5400"), Color.parseColor("#282830"))
                .setTextSize(34)
                .setPadding(0, 0, 0, 0)
                .build();
    }

    @Override
    public int getBackground(int position) {
        return 0;
    }
}