package com.puxiang.mall.module.shop.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentShopBinding;
import com.puxiang.mall.databinding.ViewShopHeadBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.mall.viewmodel.MsgCountViewModel;
import com.puxiang.mall.module.shop.adapter.ShopListAdapter;
import com.puxiang.mall.module.shop.viewModel.ShopHeadViewModel;
import com.puxiang.mall.module.shop.viewModel.ShopViewModel;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2017/10/12.
 * 精选商家列表页面
 */

public class ShopListFragment extends BaseBindFragment {
    private FragmentShopBinding shopBinding;
    private ViewShopHeadBinding shopHeadBinding;
    private ShopViewModel shopViewModel;
    private ShopHeadViewModel shopHeadViewModel;
    private MsgCountViewModel msgCountViewModel;
    private ShopListAdapter shopListAdapter;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        shopBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, container, false);
        shopHeadBinding = DataBindingUtil.inflate(inflater, R.layout.view_shop_head, container, false);
        shopListAdapter = new ShopListAdapter(R.layout.item_shop);
        shopViewModel = new ShopViewModel(this, shopListAdapter);
        shopHeadViewModel = new ShopHeadViewModel(this);
        msgCountViewModel = new MsgCountViewModel(this);

        shopBinding.setHeadModel(shopHeadViewModel);
        shopBinding.setViewModel(shopViewModel);
        shopBinding.setMsgModel(msgCountViewModel);
        shopHeadBinding.setViewModel(shopViewModel);
        shopHeadBinding.setHeadModel(shopHeadViewModel);

        AutoUtils.auto(shopHeadBinding.getRoot());
        return shopBinding.getRoot();
    }

    @Override
    public void initView() {
        shopListAdapter.addHeaderView(shopHeadBinding.getRoot());
        shopListAdapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        shopListAdapter.setEnableLoadMore(true);
        shopListAdapter.setOnLoadMoreListener(() -> shopViewModel.loadMore(), shopBinding.rvShop);
        shopBinding.rvShop.setLayoutManager(new LinearLayoutManager(getContext()));
        shopBinding.rvShop.setAdapter(shopListAdapter);
        shopBinding.rvShop.addOnItemTouchListener(shopViewModel.onItemTouchListener());

        initBanner(shopHeadBinding.banner);
        initRefresh(shopBinding.ptrFrame);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) shopHeadBinding.banner.getLayoutParams();
        params.height = MyApplication.widthPixels / 2;
        shopHeadBinding.banner.setLayoutParams(params);
        shopHeadBinding.banner.setAdapter(shopHeadViewModel);
    }

    @Override
    public void update() {
        super.update();
        shopBinding.ptrFrame.autoRefresh();
    }

    @Override
    public void refreshData() {
        super.refreshData();
        shopHeadViewModel.getBannerData();
        shopViewModel.getShopList(1, "", "", "");
        msgCountViewModel.getMsgCountData();
    }

    @Override
    protected void viewModelDestroy() {
        if (shopViewModel != null) {
            shopViewModel.destroy();
        }
        if (shopHeadViewModel != null) {
            shopHeadViewModel.destroy();
        }
        if (msgCountViewModel != null) msgCountViewModel.destroy();
    }
}
