package com.puxiang.mall.widget.verticaltablayout;


import com.puxiang.mall.widget.verticaltablayout.widget.QTabView;

public interface TabAdapter {

    int getCount();

    int getBadge(int position);

    QTabView.TabIcon getIcon(int position);

    QTabView.TabTitle getTitle(int position);

    int getBackground(int position);
}
