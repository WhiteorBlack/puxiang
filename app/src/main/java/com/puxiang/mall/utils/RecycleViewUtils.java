package com.puxiang.mall.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.puxiang.mall.R;
import com.puxiang.mall.widget.CustomLoadMoreView;

public class RecycleViewUtils {
    public static TextView setEmptyView(BaseQuickAdapter adapter, RecyclerView recycle, LayoutInflater inflater,
                                        String noneStr) {
        View emptyView = inflater.inflate(R.layout.view_bbs_none, ((ViewGroup) recycle.getParent()), false);
        AutoUtils.auto(emptyView);
        TextView none_tv = (TextView) emptyView.findViewById(R.id.tv_none);
        none_tv.setText(noneStr);
        adapter.setEmptyView(emptyView);
        return none_tv;
    }

    public static TextView setEmptyViewTop(BaseQuickAdapter adapter, RecyclerView recycle, LayoutInflater inflater,
                                        String noneStr) {
        View emptyView = inflater.inflate(R.layout.view_buy_list_none, ((ViewGroup) recycle.getParent()), false);
        AutoUtils.auto(emptyView);
        TextView none_tv = (TextView) emptyView.findViewById(R.id.tv_none);
        none_tv.setText(noneStr);
        adapter.setEmptyView(emptyView);
        return none_tv;
    }

    public static TextView setCollapseEmptyView(BaseQuickAdapter adapter, RecyclerView recycle, LayoutInflater
            inflater, String noneStr) {
        View emptyView = inflater.inflate(R.layout.view_collapse_none, ((ViewGroup) recycle.getParent()), false);
        AutoUtils.auto(emptyView);
        TextView none_tv = (TextView) emptyView.findViewById(R.id.tv_none);
        none_tv.setText(noneStr);
        adapter.setEmptyView(emptyView);
        return none_tv;
    }


    public static LoadMoreView getLoadMoreView() {
        return new CustomLoadMoreView(true);
    }

    public static LoadMoreView getLoadMoreView(boolean isShowEndView) {
        return new CustomLoadMoreView(isShowEndView);
    }

}
