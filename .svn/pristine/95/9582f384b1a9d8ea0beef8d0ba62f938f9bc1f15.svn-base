package com.puxiang.mall.module.personal.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityRvPagerBinding;
import com.puxiang.mall.module.personal.adapter.FansAdapter;
import com.puxiang.mall.module.personal.viewmodel.FansViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;

public class FansActivity extends BaseBindActivity {
    private FansAdapter adapter;
    private ActivityRvPagerBinding binding;
    private FansViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rv_pager);
        adapter = new FansAdapter(R.layout.item_personal_fans);
        viewModel = new FansViewModel(this, adapter);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("粉丝");
        initRv(binding.rv);
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    private void initRv(RecyclerView rv) {
        RecycleViewUtils.setEmptyView(adapter, rv, getLayoutInflater(), "暂无粉丝~");
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore());

        rv.addOnItemTouchListener(viewModel.itemClickListener());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    public void onClick(View view) {
        onBackPressed();
    }

    public void setIsInitData() {
        binding.setIsInitData(true);
    }
}
