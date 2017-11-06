package com.puxiang.mall.module.circle.view;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.puxiang.mall.databinding.FragmentCirclePageBinding;
import com.puxiang.mall.module.circle.viewmodel.CircleFragmentFactory;
import com.puxiang.mall.module.circle.viewmodel.CirclePageViewModel;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxChannel;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.MeasureUtil;
import com.puxiang.mall.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class CirclePageFragment extends BaseBindFragment implements View.OnClickListener, IndicatorViewPager.OnIndicatorPageChangeListener {

    private HomePageAdapter homePageAdapter;
    private FragmentCirclePageBinding binding;
    private CirclePageViewModel viewModel;

    public final static int STATUS_FIRST = 1;
    public static int CREATE_STATUS;
    private boolean isRed = false;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_circle_page, container, false);
        homePageAdapter = new HomePageAdapter(getActivity().getSupportFragmentManager(), null);
        viewModel = new CirclePageViewModel(this, homePageAdapter);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        initToolBar();
//        binding.rlHomeSearch.setOnClickListener(this);
        binding.llNone.llNone.setOnClickListener(this);
        CircleFragment.isVisiable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                ObservableBoolean isVisiable = (ObservableBoolean) sender;
                isRed = isVisiable.get();
                try {
                    if (isVisiable.get()) {

                        binding.toolbar.llTop.setBackgroundColor(getResources().getColor(R.color.sale_price));
                    } else {
                        binding.toolbar.llTop.setBackgroundColor(0);
                    }
                } catch (IllegalStateException e) {

                }

            }
        });
    }

    private void initToolBar() {
        initTab();
        binding.toolbar.tvUserName.setOnClickListener(this);
        binding.toolbar.sdvBbsPic.setOnClickListener(this);
    }

    private void initTab() {
        float unSelectSize = MeasureUtil.px2sp(MyApplication.getContext(), AutoUtils.getTextPixels(32));
        float selectSize = unSelectSize * 1.2f;
        int color = AppUtil.getColor(R.color.tab_on_color);
        int tabColor = AppUtil.getColor(R.color.tab_off_color);
        binding.toolbar.sivIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(color, tabColor)
                .setSize(selectSize, unSelectSize));
        binding.vpHome.setOffscreenPageLimit(2);
        binding.toolbar.sivIndicator.setSplitMethod(FixedIndicatorView.SPLITMETHOD_WRAP);
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(binding.toolbar.sivIndicator, binding.vpHome);
        indicatorViewPager.setAdapter(homePageAdapter);
        indicatorViewPager.setOnIndicatorPageChangeListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.rl_home_search:
//                ActivityUtil.startSearchBBsListActivity(getActivity(), viewModel.keyword.get());
//                break;
            case R.id.sdv_bbs_pic:
            case R.id.tv_user_name:
                if (StringUtil.isEmpty(MyApplication.TOKEN)) {
                    ActivityUtil.startLoginActivity(getActivity());
                } else {
                    ActivityUtil.startPersonalActivity(getActivity(), MyApplication.USER_ID);
                }
                break;
            case R.id.ll_none:
                viewModel.getChannels();
                break;
        }

    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    @Override
    public void onIndicatorPageChange(int preItem, int currentItem) {
        if (currentItem == 1) {
            binding.toolbar.llTop.setBackgroundColor(getResources().getColor(R.color.sale_price));
        } else {
            if (!isRed) {
                binding.toolbar.llTop.setBackgroundColor(0);
            }
        }
    }

    public class HomePageAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private final List<RxChannel> tabs;

        HomePageAdapter(FragmentManager fragmentManager, List<RxChannel> tabs) {
            super(fragmentManager);
            this.tabs = tabs == null ? new ArrayList<>() : tabs;
        }

        public void setNewData(List<RxChannel> list) {
            if (list != null) {
                CREATE_STATUS = STATUS_FIRST;
                tabs.clear();
                tabs.addAll(list);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(container.getContext()).inflate(R.layout
                        .view_home_tab_tv, container, false);
                AutoUtils.auto(convertView);
            }
            TextView textView = (TextView) convertView;
            String channelName = tabs.get(position).getChannelName();
            textView.setText(channelName);


            int width = getTextWidth(textView);
            int padding = MyApplication.widthPixels / 25;
            //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
            //1.3f是根据上面字体大小变化的倍数1.3f设置
            textView.setWidth((int) (width * 1.2f) + padding);
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            RxChannel channel = tabs.get(position);
            Fragment fragment = CircleFragmentFactory.create(channel);
            if (position == 0 && fragment != null) {
                Bundle bundle = fragment.getArguments();
                if (bundle != null) {
                    bundle.putBoolean("first", true);
                }
            }
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
            // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
            return PagerAdapter.POSITION_NONE;
        }

        private int getTextWidth(TextView textView) {
            if (textView == null) {
                return 0;
            }
            Rect bounds = new Rect();
            String text = textView.getText().toString();
            Paint paint = textView.getPaint();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int width = bounds.left + bounds.width();
            return width;
        }
    }
}


