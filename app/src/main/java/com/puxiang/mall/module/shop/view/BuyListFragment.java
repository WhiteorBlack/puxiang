package com.puxiang.mall.module.shop.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentRvPagerBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.search.adapter.SearchListAdapter;
import com.puxiang.mall.module.shop.adapter.BuyListAdapter;
import com.puxiang.mall.module.shop.viewModel.BuyFragmentViewModel;
import com.puxiang.mall.module.shop.viewModel.ShopGoodsViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BuyListFragment extends BaseBindFragment {

    private BuyListAdapter adapter;

    private LayoutInflater inflater;
    private BuyFragmentViewModel viewModel;
    private FragmentRvPagerBinding binding;


    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        this.inflater = inflater;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rv_pager, container, false);
        adapter = new BuyListAdapter(R.layout.item_buy_goods);
        viewModel = new BuyFragmentViewModel(this, adapter);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        initRecycle(binding.rv);
    }

    private void initRecycle(RecyclerView rv) {
        RecycleViewUtils.setEmptyViewTop(adapter, rv, inflater, "");
        adapter.setHasStableIds(true);
        //上拉加载更多设置
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);
        rv.setHasFixedSize(true);
        rv.addOnItemTouchListener(viewModel.itemClickListener());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.update(1);
    }


    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) {
            viewModel.destroy();
        }
    }

    public void setIsInitData() {
        binding.setIsInitData(true);
    }
}
