package com.puxiang.mall.module.bbs.adapter;

import android.content.Context;

import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxChildren;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.widget.verticaltablayout.TabAdapter;
import com.puxiang.mall.widget.verticaltablayout.widget.QTabView;

import java.util.ArrayList;
import java.util.List;

public class VideoClassifyLeftAdapter implements TabAdapter {

    private List<RxChildren> mList;
    private Context mContext;

    public VideoClassifyLeftAdapter(Context context, List<RxChildren> list) {
        mList = list == null ? new ArrayList<>() : list;
        mContext = context;
    }

    public void addData(List<RxChildren> list) {
        mList.addAll(list);
    }

    public RxChildren getItemData(int i) {
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
        return new QTabView.TabTitle.Builder(mContext)
                .setContent(mList.get(position).getChannelName())
                .setTextColor(AppUtil.getColor(R.color.indicator_color), AppUtil.getColor(R.color.title))
                .setTextSize(28)
                .setTextBackground(AppUtil.getColor(R.color.white), 0x00000000)
                .setPadding(0, 40, 0, 40)
                .build();
    }

    @Override
    public int getBackground(int position) {
        return 0;
    }
}