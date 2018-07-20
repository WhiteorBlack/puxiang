package com.puxiang.mall.module.personal.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityRvPagerBinding;
import com.puxiang.mall.module.personal.adapter.UserPlatesAdapter;
import com.puxiang.mall.module.personal.viewmodel.UserPlatesViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;

public class UserPlatesActivity extends BaseBindActivity {

    private UserPlatesAdapter adapter;
    private ActivityRvPagerBinding binding;
    private UserPlatesViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rv_pager);
        adapter = new UserPlatesAdapter(R.layout.item_user_plate);
        viewModel = new UserPlatesViewModel(this, adapter);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("圈子");
        initRv(binding.rv);
        setBarHeight(binding.toolbar.ivBar);
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    private void initRv(RecyclerView rv) {
        RecycleViewUtils.setEmptyView(adapter, rv, getLayoutInflater(), "暂无关注的圈子~");

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
