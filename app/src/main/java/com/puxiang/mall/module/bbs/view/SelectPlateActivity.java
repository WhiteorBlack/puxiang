package com.puxiang.mall.module.bbs.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivitySelectPlateBinding;
import com.puxiang.mall.module.bbs.adapter.SelectPlateAdapter;
import com.puxiang.mall.module.bbs.viewmodel.SelectPlateViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;

public class SelectPlateActivity extends BaseBindActivity {

    private SelectPlateAdapter adapter;
    private ActivitySelectPlateBinding binding;
    private SelectPlateViewModel viewModel;


    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_plate);
        adapter = new SelectPlateAdapter(R.layout.item_select_plate);
        viewModel = new SelectPlateViewModel(this, adapter);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle(getString(R.string.select_plate));
        initRecyclerView();
        setBarHeight(binding.toolbar.ivBar);
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    private void initRecyclerView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setAdapter(adapter);
        RecycleViewUtils.setEmptyView(adapter, binding.rv, getLayoutInflater(), getString(R.string.select_plate_err));
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore());
        binding.rv.addOnItemTouchListener(viewModel.itemClickListener());
    }

    public void onClick(View view) {
        if (view.getId() == R.id.iv_back) {
            onBackPressed();
        }
    }
}
