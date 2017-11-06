package com.puxiang.mall.module.search.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentRvPagerBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.search.adapter.SearchListAdapter;
import com.puxiang.mall.module.search.viewmodel.SearchProductViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;

public class SearchListFragment extends BaseBindFragment {

    private SearchListAdapter adapter;

    private LayoutInflater inflater;
    private SearchProductViewModel viewModel;
    private FragmentRvPagerBinding binding;


    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        this.inflater = inflater;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rv_pager, container, false);
        adapter = new SearchListAdapter(R.layout.item_search);
        viewModel = new SearchProductViewModel(this, adapter);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        initRecycle(binding.rv);
    }


    private void initRecycle(RecyclerView rv) {
        adapter.setHasStableIds(true);
        RecycleViewUtils.setEmptyView(adapter, rv, inflater, "搜索不到该商品~");
        //上拉刷新设置
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(),rv);
        rv.addOnItemTouchListener(viewModel.itemClickListener());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
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
