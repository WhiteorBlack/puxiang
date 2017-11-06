package com.puxiang.mall.module.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.puxiang.mall.module.circle.view.CirclePageFragment;
import com.puxiang.mall.module.classify.view.ClassifyFragment;
import com.puxiang.mall.module.mall.view.MallFragment;
import com.puxiang.mall.module.my.view.MyFragment;
import com.puxiang.mall.module.shop.view.ShopListFragment;
import com.puxiang.mall.module.shoppingcart.view.ShoppCartFragment;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.utils.AutoUtils;

/**
 * create by zhaoyong bai on 2017/09/06
 */

public class ViewPagerAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
    private LayoutInflater inflater;
    private int[] icons;
    private SparseArray<Fragment> navigateMap = new SparseArray<>();

    public ViewPagerAdapter(FragmentManager fragmentManager, int[] icons) {
        super(fragmentManager);
        inflater = LayoutInflater.from(MyApplication.getContext());
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return icons.length;
    }


    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tab_main, container, false);
        }
        ImageView imageView = (ImageView) convertView;
        imageView.setImageDrawable(AppUtil.getDrawable(icons[position]));
        AutoUtils.auto(imageView);
        return imageView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        Fragment fragment = navigateMap.get(position);
        if (fragment != null) {
            return fragment;
        }
        fragment=new Fragment();
        switch (position) {
            case 0://首页
                fragment = new MallFragment();
                break;
            case 1://分类
                fragment=new ClassifyFragment();
                break;
            case 2://社区
                fragment = new CirclePageFragment();
                break;
            case 3://购物车
                fragment = new ShoppCartFragment();
                break;
            case 4://我的
                fragment = new MyFragment();
                break;
        }
        navigateMap.put(position, fragment);
        return fragment;
    }
}