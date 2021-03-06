package com.puxiang.mall.module.shop.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.databinding.ActivityShopDetialBinding;
import com.puxiang.mall.module.classify.viewmodel.HeadBannerViewModel;
import com.puxiang.mall.module.mall.viewmodel.MsgCountViewModel;
import com.puxiang.mall.module.shop.adapter.SimpleFragmentAdapter;
import com.puxiang.mall.module.shop.viewModel.ShopDetialViewModel;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.utils.MyTextUtils;
import com.puxiang.mall.utils.ScreenUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2017/10/17.
 */

public class ShopDetial extends BaseBindActivity implements TextView.OnEditorActionListener {
    private ActivityShopDetialBinding binding;
    private ShopDetialViewModel viewModel;
    private MsgCountViewModel msgCountViewModel;
    private HeadBannerViewModel headBannerViewModel;
    private String shopId;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_detial);
        viewModel = new ShopDetialViewModel(this);
        msgCountViewModel = new MsgCountViewModel(this);
        binding.setMsgModel(msgCountViewModel);
        binding.setViewModel(viewModel);
        binding.headBanner.setAdapter(viewModel);
        shopId = getIntent().getStringExtra("shopId");
        mImmersionBar.statusBarDarkFont(false).keyboardEnable(false).init();
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        initIndicator(binding.bottomView);
        initBanner(binding.headBanner);
        viewModel.getShopDetialData();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.headBanner.getLayoutParams();
        params.height = (int) (MyApplication.widthPixels * 0.347);
        binding.headBanner.setLayoutParams(params);
        binding.nslParent.setScrollable(false);
        binding.toolbarShopDetial.et.setOnEditorActionListener(this);
        setBarHeight(binding.toolbarShopDetial.ivBar);
    }


    private List<Fragment> initData() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            fragments.add(newInstance(i, "", shopId));
        }
        return fragments;
    }

    public Fragment newInstance(int i, String keyword, String shopId) {
        ShopGoodsFragment fragment = new ShopGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", i + 1 + "");
        bundle.putString("shopId", shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initIndicator(ViewPager vp) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vp.getLayoutParams();
        vp.setLayoutParams(params);
        FragmentManager fm = getSupportFragmentManager();
        vp.setAdapter(new SimpleFragmentAdapter(fm, initData()));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.txt_search:
                String keyword = MyTextUtils.getEditTextString(binding.toolbarShopDetial.et);
                //请求SearchFragment刷新数据
                EventBus.getDefault().post(keyword);
                break;
        }
    }

    public void setCurrentPos(int pos) {
        binding.bottomView.setCurrentItem(pos);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.ENABLE_SCROLL) {
            binding.nslParent.setScrollable(true);
        }
    }

    @Override
    protected void viewModelDestroy() {
        EventBus.getDefault().unregister(this);
        if (viewModel != null) {
            viewModel.destroy();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String keyword = binding.toolbarShopDetial.et.getText().toString();
            EventBus.getDefault().post(keyword);
            closeInput();
            return true;
        }
        return false;
    }
}
