package com.puxiang.mall.module.personal.view;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.puxiang.mall.BaseBindActivity;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.databinding.ActivityPersonalBinding;
import com.puxiang.mall.model.data.RxUserCommunity;
import com.puxiang.mall.module.im.model.IMUserInfoProvider;
import com.puxiang.mall.module.personal.viewmodel.PersonalViewModel;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.widget.AppBarStateChangeListener;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import io.rong.imkit.RongIM;

public class PersonalActivity extends BaseBindActivity implements View.OnClickListener {

    private ActivityPersonalBinding binding;
    private PersonalViewModel viewModel;

    @Override
    protected void initBind() {
//        setStatusBar();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal);
        viewModel = new PersonalViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        initToolbar(binding.mobileToolbar);
        initAppBarLayout(binding.abl, binding.ctl);
        initIndicator(binding.vp, binding.fivSearch);

        binding.vChat.setOnClickListener(this);
        binding.sdvUserPic.setOnClickListener(this);
        binding.llAttention.setOnClickListener(this);
        binding.vAttention.setOnClickListener(this);
        binding.llFans.setOnClickListener(this);
        binding.llPlate.setOnClickListener(this);
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
        WeakReference<Activity> wr = new WeakReference<>(this);
    }

    private void initIndicator(ViewPager vp, FixedIndicatorView indicator) {
        String[] tabs = getResources().getStringArray(R.array.tabs_personal);
        FragmentManager fm = getSupportFragmentManager();

        int color = AppUtil.getColor(R.color.tab_select_color);
        int tabColor = AppUtil.getColor(R.color.title);
        vp.setOffscreenPageLimit(3);
        indicator.setScrollBar(new ColorBar(getApplicationContext(), color, Config
                .TabColorBarHeight));
        indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(color, tabColor));

        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(indicator, vp);
        IndicatorAdapter IndicatorAdapter = new IndicatorAdapter(fm, tabs, viewModel.userId);
        indicatorViewPager.setAdapter(IndicatorAdapter);
    }

    private void initAppBarLayout(AppBarLayout abl, CollapsingToolbarLayout ctl) {
        abl.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if (state == State.EXPANDED) {
                    ctl.setTitle("");
                    ctl.setContentScrimColor(getResources().getColor(R.color.Alpha_theme));
                    EventBus.getDefault().post(false);
                    mImmersionBar.barColor(R.color.Alpha_theme);
                    //展开状态
                } else if (state == State.COLLAPSED) {
                    EventBus.getDefault().post(true);
                    //折叠状态
//                    ctl.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_GRAVITY);
                    RxUserCommunity userCommunity = viewModel.userBean.get();
                    String viewName;
                    if (userCommunity != null) {
                        viewName = userCommunity.getAccount().getViewName();
                    } else {
                        viewName = "";
                    }
                    ctl.setTitle(viewName);
                    ctl.setCollapsedTitleTextColor(getResources().getColor(R.color.searchTextColor));
                    ctl.setContentScrimColor(getResources().getColor(R.color.toolbarColor));
                    mImmersionBar.barColor(R.color.toolbarColor);
                } else {
                    //中间状态
                    ctl.setTitle("");
                    ctl.setContentScrimColor(getResources().getColor(R.color.white_alpha));
                    mImmersionBar.barColor(R.color.white_alpha);
                }
            }
        });
    }

    public void onClick(View view) {
        if (viewModel.userBean.get() == null) {
            ToastUtil.toast(R.string.no_network);
            return;
        }
        switch (view.getId()) {
            case R.id.v_chat:
                if (!MyApplication.isLogin()) {
                    ActivityUtil.startLoginActivity(this);
                } else {
                    RongIM.getInstance().startPrivateChat(this, viewModel.userId,
                            IMUserInfoProvider.NULL);
                }
                break;
            case R.id.v_attention:
                viewModel.setAttentUser();
                break;
            case R.id.sdv_user_pic:
                ActivityUtil.startShowHeadPicActivity(this, viewModel.userBean.get().getAccount()
                        .getUserImage(), viewModel.userId);
                break;
            case R.id.ll_plate:
                ActivityUtil.startUserPlatesActivity(this, viewModel.userId);
                break;
            case R.id.ll_attention:
                ActivityUtil.startAttentionUsersActivity(this, viewModel.userId);
                break;
            case R.id.ll_fans:
                ActivityUtil.startFansActivity(this, viewModel.userId);
                break;
        }
    }


    private class IndicatorAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        private final String userId;
        private String[] tabs;

        IndicatorAdapter(FragmentManager fragmentManager, String[] tabs, String userId) {
            super(fragmentManager);
            this.tabs = tabs;
            this.userId = userId;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(container.getContext()).inflate(R.layout
                        .view_home_tab_tv, container, false);
                AutoUtils.auto(convertView);
            }
            TextView textView = (TextView) convertView;
            String str = tabs[position];
            textView.setText(str);


            int width = getTextWidth(textView);
            int padding = MyApplication.widthPixels / 25;
            //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
            //1.3f是根据上面字体大小变化的倍数1.3f设置
            textView.setWidth((int) (width * 1.2f) + padding);
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            Fragment fragment = null;
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    fragment = new PersonalPublishFragment();
                    break;
                case 1:
                    fragment = new PersonalReplyFragment();
                    break;
                case 2:
                    fragment = new PersonalCollectFragment();
                    break;
            }
            if (fragment != null) {
                bundle.putString(Config.USER_ID, userId);
                fragment.setArguments(bundle);
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
            return bounds.left + bounds.width();
        }
    }
}
