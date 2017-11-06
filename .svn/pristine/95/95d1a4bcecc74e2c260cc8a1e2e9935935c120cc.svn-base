package com.puxiang.mall.module.personal.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityRvPagerBinding;
import com.puxiang.mall.module.personal.adapter.AttentionUsersAdapter;
import com.puxiang.mall.module.personal.viewmodel.AttentionViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;

public class AttentionUsersActivity extends BaseBindActivity {

    private AttentionUsersAdapter adapter;
    private ActivityRvPagerBinding binding;
    private AttentionViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rv_pager);
        adapter = new AttentionUsersAdapter(R.layout.item_personal_attention_users);
        viewModel = new AttentionViewModel(this, adapter);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("关注的人");
        initRv(binding.rv);
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }


    private void initRv(RecyclerView rv) {
        RecycleViewUtils.setEmptyView(adapter, rv, getLayoutInflater(), "暂无关注的用户~");

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
