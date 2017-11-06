package com.puxiang.mall.module.personal.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentRvPagerBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.personal.adapter.PublishPostAdapter;
import com.puxiang.mall.module.personal.viewmodel.PublishViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;

public class PersonalPublishFragment extends BaseBindFragment {

    private PublishPostAdapter adapter;
    private FragmentRvPagerBinding binding;
    private PublishViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        setIsFirstUserUpdate(false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rv_pager, container, false);
        adapter = new PublishPostAdapter(R.layout.item_personal_post);
        adapter.setRefreshListener(this);
        viewModel = new PublishViewModel(this, adapter);
        return binding.getRoot();
    }

    public void setIsInitData() {
        binding.setIsInitData(true);
    }

    @Override
    public void initView() {
        initRv(binding.rv);
    }

    private void initRv(RecyclerView rv) {
        RecycleViewUtils.setCollapseEmptyView(adapter, rv, getActivity().getLayoutInflater(), "暂无发帖~");
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore());

        rv.addOnItemTouchListener(viewModel.itemClickListener());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }
}
