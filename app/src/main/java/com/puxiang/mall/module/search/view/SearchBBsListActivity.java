package com.puxiang.mall.module.search.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.puxiang.mall.BaseBindActivity;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.databinding.ActivitySearchListBinding;
import com.puxiang.mall.module.search.viewmodel.SearchListViewModel;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.MyTextUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SearchBBsListActivity extends BaseBindActivity implements View.OnClickListener {
    private List<Fragment> fragments = new ArrayList<>();

    private final static int TYPE_PLATE = 1;
    private final static int TYPE_POST = 2;
    private final static int TYPE_MEMBER = 3;
    private ActivitySearchListBinding binding;
    private SearchListViewModel viewModel;
    private String keyword;

    @Override
    protected void initBind() {
        keyword = getIntent().getStringExtra(Config.KEYWORD);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_list);
        viewModel = new SearchListViewModel(keyword);
        binding.setViewModel(viewModel);
        mImmersionBar.keyboardEnable(false).init();
    }

    public void initView() {
        binding.toolbarLayout.et.setCursorVisible(false);
        binding.toolbarLayout.et.setOnClickListener(v -> binding.toolbarLayout.et.setCursorVisible(true));
        binding.toolbarLayout.et.setHint("输入帖子名称");
        binding.vp.setOffscreenPageLimit(3);
        setPageTag("keyword:", keyword);
        binding.toolbarLayout.et.setText(keyword);
        initData();
    }

    private void initData() {
        newInstance(TYPE_POST, keyword);
        newInstance(TYPE_PLATE, keyword);
        newInstance(TYPE_MEMBER, keyword);
        initIndicator();
        initIndicatorViewPager();
    }

    private void initIndicatorViewPager() {
        String[] tabs = getResources().getStringArray(R.array.tabs_search);
        FragmentManager fm = getSupportFragmentManager();
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(binding.fivIndicator, binding.vp);
        indicatorViewPager.setAdapter(new MyAdapter(fm, tabs, fragments));
        MyAdapter myAdapter = new MyAdapter(fm, tabs, fragments);
        indicatorViewPager.setAdapter(myAdapter);
        binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.toolbarLayout.et.setHint("输入帖子名称");
                        break;
                    case 1:
                        binding.toolbarLayout.et.setHint("输入圈子名称");
                        break;
                    case 2:
                        binding.toolbarLayout.et.setHint("输入用户名称");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initIndicator() {
        // init Indicator
        int tabColor = AppUtil.getColor(R.color.title);
        int color = AppUtil.getColor(R.color.tab_select_color);
        binding.fivIndicator.setScrollBar(new ColorBar(getApplicationContext(), color, Config.TabColorBarHeight));
        binding.fivIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(color, tabColor));
    }

    public void newInstance(int type, String keyword) {
        Fragment fragment = null;
        switch (type) {
            case TYPE_PLATE:
                fragment = new SearchPlateFragment();
                break;
            case TYPE_POST:
                fragment = new SearchPostFragment();
                break;
            case TYPE_MEMBER:
                fragment = new SearchMemberFragment();
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        if (fragment != null) {
            fragment.setArguments(bundle);
        }
        fragments.add(fragment);
    }

    public void onClick(View view) {
        closeInput();
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_search_btn:
                String keyword = MyTextUtils.getEditTextString(binding.toolbarLayout.et);
                viewModel.keyword.set(keyword);
                MobclickAgent.onEvent(this, "keyword--->" + keyword);
                //请求SearchFragment刷新数据
                EventBus.getDefault().post(keyword);
                break;
        }
    }

    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        private String[] tabs;
        private List<Fragment> fragments;

        MyAdapter(FragmentManager fragmentManager, String[] tabs, List<Fragment> fragments) {
            super(fragmentManager);
            this.tabs = tabs;
            this.fragments = fragments;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, final ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(container.getContext())
                        .inflate(R.layout.view_home_tab_tv, container, false);
                AutoUtils.auto(convertView);
            }
            TextView textView = (TextView) convertView;
            final String title = tabs[position];
            textView.setText(title);
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            return fragments.get(position);
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (binding.vp != null) binding.vp.removeAllViews();
        if (viewModel != null) viewModel.destroy();
    }
}
