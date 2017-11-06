package com.puxiang.mall.module.search.view;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentRvPagerBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.search.adapter.PostQuickAdapter;
import com.puxiang.mall.module.search.viewmodel.SearchPostViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;

public class SearchPostFragment extends BaseBindFragment {

    private PostQuickAdapter adapter;
    private FragmentRvPagerBinding binding;
    private SearchPostViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rv_pager, container, false);
        adapter = new PostQuickAdapter(R.layout.item_search_post);
        viewModel = new SearchPostViewModel(this, adapter);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        initRecycle(binding.rv);
    }

    private void initRecycle(RecyclerView rv) {
        rv.addOnItemTouchListener(viewModel.itemClickListener());
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        RecycleViewUtils.setEmptyView(adapter, rv, getActivity().getLayoutInflater(), "搜索不到该帖子~");
        adapter.setEnableLoadMore(true);
        adapter.setHasStableIds(true);
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(),rv);
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
