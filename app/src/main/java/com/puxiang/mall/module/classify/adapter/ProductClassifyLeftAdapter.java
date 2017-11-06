package com.puxiang.mall.module.classify.adapter;

import android.graphics.Color;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxCatalog;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.widget.verticaltablayout.TabAdapter;
import com.puxiang.mall.widget.verticaltablayout.widget.QTabView;

import java.util.ArrayList;
import java.util.List;

public class ProductClassifyLeftAdapter implements TabAdapter {

    private List<RxCatalog> mList;

    public ProductClassifyLeftAdapter(List<RxCatalog> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    public void addData(List<RxCatalog> list) {
        mList.addAll(list);
    }

    public RxCatalog getItemData(int i) {
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
                .setContent(mList.get(position).getSysTypeName())
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