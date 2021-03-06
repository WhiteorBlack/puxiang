package com.puxiang.mall.module.shop.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityBuyListBinding;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.module.classify.viewmodel.HeadBannerViewModel;
import com.puxiang.mall.module.mall.viewmodel.MsgCountViewModel;
import com.puxiang.mall.module.shop.adapter.SimpleFragmentAdapter;
import com.puxiang.mall.module.shop.viewModel.BuyListViewModel;
import com.puxiang.mall.network.URLs;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2017/10/17.
 */

public class BuyGoodsList extends BaseBindActivity implements TextView.OnEditorActionListener {
    private ActivityBuyListBinding binding;
    private BuyListViewModel viewModel;
    private MsgCountViewModel msgCountViewModel;
    private HeadBannerViewModel headBannerViewModel;
    private String shopId;
    private String[] keyword = new String[]{"白酒", "葡萄酒", "茶", ""};

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_buy_list);
        viewModel = new BuyListViewModel(this);
        msgCountViewModel = new MsgCountViewModel(this);
        binding.setMsgModel(msgCountViewModel);
        binding.setViewModel(viewModel);
        headBannerViewModel = new HeadBannerViewModel(this);
        binding.headBanner.headBanner.setAdapter(headBannerViewModel);
        binding.setHeadModel(headBannerViewModel);
        mImmersionBar.statusBarDarkFont(false).keyboardEnable(false).init();
    }

    @Override
    public void initView() {
        initIndicator(binding.bottomView);
//        viewModel.getShopDetialData();
        headBannerViewModel.getBannerData(URLs.STOK_CAROUSEL);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.headBanner.headBanner.getLayoutParams();
        params.height = (int) (MyApplication.widthPixels * 0.347);
        binding.headBanner.headBanner.setLayoutParams(params);
        binding.toolbarShopDetial.et.setOnEditorActionListener(this);
    }


    private List<Fragment> initData() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            fragments.add(newInstance(i, this.keyword[i], shopId));
        }
        return fragments;
    }

    public Fragment newInstance(int i, String keyword, String shopId) {
        BuyListFragment fragment = new BuyListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", i + 1 + "");
        bundle.putString("keyword", keyword);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initIndicator(ViewPager vp) {
        vp.setOffscreenPageLimit(3);
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
            case R.id.btn_commit:
                viewModel.buy();
                break;
            case R.id.txt_search:
                String keyword = binding.toolbarShopDetial.et.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putBoolean("search", true);
                bundle.putString("keyword", keyword);
                EventBus.getDefault().post(bundle);
                viewModel.setSelectPos(4);
                closeInput();
                break;
        }
    }

    public void setCurrentPos(int pos) {
        binding.bottomView.setCurrentItem(pos);
    }

    public void setTotalPrice(double money) {
        viewModel.setTotalMoney(money);
    }

    public void selectGoods(boolean isSelected, RxProduct rxProduct) {
        viewModel.setSelectGoods(isSelected, rxProduct);
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            String keyword = binding.toolbarShopDetial.et.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putBoolean("search", true);
            bundle.putString("keyword", keyword);
            EventBus.getDefault().post(bundle);
            viewModel.setSelectPos(4);
            closeInput();
            return true;
        }
        return false;
    }
}
