package com.puxiang.mall.module.from.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityFromBinding;
import com.puxiang.mall.module.from.adapter.FromAdapter;
import com.puxiang.mall.module.from.viewmodel.FromViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;

public class FromActivity extends BaseBindActivity {
    private FromAdapter adapter;
    private ActivityFromBinding binding;
    private FromViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_from);
        adapter = new FromAdapter(R.layout.item_from_show, this);
        viewModel = new FromViewModel(this, adapter);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        String typeName = getIntent().getStringExtra("typeName");
        binding.toolbar.setTitle(typeName);
        initRecyclerView(binding.rv);
        initRefresh(binding.storeHousePtrFrame);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        viewModel.getData(1);
    }

    private void initRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addOnItemTouchListener(viewModel.itemClickListener());
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);
        rv.setAdapter(adapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }
}
