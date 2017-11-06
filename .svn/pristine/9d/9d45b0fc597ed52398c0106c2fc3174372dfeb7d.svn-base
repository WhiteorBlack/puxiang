package com.puxiang.mall.module.emotion.viewmodel.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Description: EmotiomComplateFragment界面Viewpager数据适配器
 */
public class EmotionPagerAdapter extends PagerAdapter {

    private List<RecyclerView> rvs;

    public EmotionPagerAdapter(List<RecyclerView> rvs) {
        this.rvs = rvs;
    }

    @Override
    public int getCount() {
        return rvs.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(rvs.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(rvs.get(position));
        return rvs.get(position);
    }

}
