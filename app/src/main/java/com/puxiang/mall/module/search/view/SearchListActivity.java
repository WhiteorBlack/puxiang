package com.puxiang.mall.module.search.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.puxiang.mall.BaseBindActivity;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.databinding.ActivitySearchListBinding;
import com.puxiang.mall.module.search.OrderEvent;
import com.puxiang.mall.module.search.viewmodel.SearchListViewModel;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.MyTextUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SearchListActivity extends BaseBindActivity implements View.OnClickListener {
    private List<Fragment> fragments = new ArrayList<>();
    private ActivitySearchListBinding binding;
    private String keyword;
    private String catoryId;
    private SearchListViewModel viewModel;

    @Override
    protected void initBind(){
        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");
        catoryId=intent.getStringExtra("categoryId");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_list);
        viewModel = new SearchListViewModel(keyword);
        binding.setViewModel(viewModel);
    }

    public void initView() {
        setPageTag("keyword:" , keyword);
        binding.toolbarLayout.et.setText(keyword);
        binding.toolbarLayout.et.setCursorVisible(false);
        binding.toolbarLayout.et.setOnClickListener(v -> binding.toolbarLayout.et.setCursorVisible(true));
        initData();
    }

    private void initData() {
        String[] tabs = getResources().getStringArray(R.array.tabs_kown);
        for (int i = 1; i < tabs.length + 1; i++) {
            fragments.add(newInstance(i, keyword));
        }
        FragmentManager fm = getSupportFragmentManager();
        initIndicator(tabs, fm);
    }

    private void initIndicator(String[] tabs, FragmentManager fm) {
        binding.vp.setOffscreenPageLimit(fragments.size());
        int color = AppUtil.getColor(R.color.tab_select_color);
        int tabColor = AppUtil.getColor(R.color.title);
        binding.fivIndicator.setScrollBar(new ColorBar(getApplicationContext(), color, Config.TabColorBarHeight));
        binding.fivIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(color, tabColor));
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(binding.fivIndicator, binding.vp);
        indicatorViewPager.setAdapter(new MyAdapter(fm, tabs, fragments));
    }

    public Fragment newInstance(int i, String keyword) {
        SearchListFragment fragment = new SearchListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", i + "");
        bundle.putString("keyword", keyword);
        bundle.putString("categoryId",catoryId);
        fragment.setArguments(bundle);
        return fragment;
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


    @Override
    protected void viewModelDestroy() {
        if (binding.vp != null) binding.vp.removeAllViews();
        if (viewModel != null) viewModel.destroy();
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
            if (position < 2) {
                Drawable image = MyApplication.getContext().getResources().getDrawable(R.mipmap.filter_bottom);
                image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
                SpannableString sb = new SpannableString(title + "   ");
                ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
                sb.setSpan(imageSpan, title.length() + 1, title.length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(sb);
                convertView.setId(position);
                convertView.setTag("down");
                convertView.setOnClickListener(v -> {
                    if (binding.fivIndicator.getCurrentItem() == v.getId()) {
                        Drawable image1;
                        if ((v.getTag()).equals("down")) {
                            image1 = MyApplication.getContext().getResources().getDrawable(R.mipmap.filer_top);
                            v.setTag("up");
                            EventBus.getDefault().post(new OrderEvent("asc", v.getId() + 1 + ""));
                        } else {
                            image1 = MyApplication.getContext().getResources().getDrawable(R.mipmap.filter_bottom);
                            v.setTag("down");
                            EventBus.getDefault().post(new OrderEvent("desc", v.getId() + 1 + ""));
                        }
                        image1.setBounds(0, 0, image1.getIntrinsicWidth(), image1.getIntrinsicHeight());
                        SpannableString sb1 = new SpannableString(title + "   ");
                        ImageSpan imageSpan1 = new ImageSpan(image1, ImageSpan.ALIGN_BASELINE);
                        sb1.setSpan(imageSpan1, title.length() + 1, title.length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ((TextView) v).setText(sb1);
                    } else {
                        binding.vp.setCurrentItem(v.getId());
                    }
                });
            } else {
                textView.setText(title);
            }
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            return fragments.get(position);
        }
    }
}
