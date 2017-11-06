package com.puxiang.mall.module.main.view;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.databinding.ActivityMainBinding;
import com.puxiang.mall.module.main.adapter.ViewPagerAdapter;
import com.puxiang.mall.module.main.viewmodel.MainViewModel;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.WebUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.shizhefei.view.indicator.IndicatorViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends BaseBindActivity implements EasyPermission.PermissionCallback, IndicatorViewPager.OnIndicatorPageChangeListener {

    public static Handler handler = new Handler();
    private int[] tabIcons = {R.drawable.home_main, R.drawable.home_class, R.drawable.home_circle, R.drawable.home_shop_car, R.drawable.home_mine};
    private IndicatorViewPager indicatorViewPager;
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    public static boolean isInit = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getShopData();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initBind() {
        EventBus.getDefault().register(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new MainViewModel(this);
        binding.setMessageState(MyApplication.messageState);
        isInit = true;
        binding.imgPos.setOnClickListener((view -> ActivityUtil.startPublishActivity(MainActivity.this)));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        switch (i) {
            case Event.GO_HOME:
                setCurrentItem(0);
                break;
            case Event.GO_PLATES:
                setCurrentItem(2);
                break;
            case Event.GO_MALL:
                setCurrentItem(0);
                break;
        }
    }

    @Override
    public void initView() {
        indicatorViewPager = new IndicatorViewPager(binding.tabMainIndicator, binding.tabMainViewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabIcons);
        indicatorViewPager.setAdapter(adapter);
        binding.tabMainViewPager.setCanScroll(false);
        binding.tabMainViewPager.setOffscreenPageLimit(tabIcons.length);
        indicatorViewPager.setCurrentItem(0, false);
        indicatorViewPager.setOnIndicatorPageChangeListener(this);
    }


    public void setCurrentItem(int position) {
        indicatorViewPager.setCurrentItem(position, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isInit = false;
        WebUtil.removeCookie();
        EventBus.getDefault().unregister(this);
        if (MyApplication.isHotFix) {
            MyApplication.isHotFix = false;
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) {
            viewModel.destroy();
        }
    }

    @Override
    public void onIndicatorPageChange(int preItem, int currentItem) {
        viewModel.setIsPost(currentItem == 2);
        if (currentItem == 2) {
            binding.imgPos.setVisibility(View.VISIBLE);
            binding.ivPublish.setVisibility(View.VISIBLE);
        } else {
            binding.imgPos.setVisibility(View.GONE);
            binding.ivPublish.setVisibility(View.GONE);
        }
    }
}
