package com.puxiang.mall.module.bbs.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityFriendBinding;
import com.puxiang.mall.module.bbs.adapter.FriendPostsAdapter;
import com.puxiang.mall.module.bbs.viewmodel.FriendViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;
import com.puxiang.mall.utils.StringUtil;

public class FriendActivity extends BaseBindActivity {
    private FriendPostsAdapter adapter;
    private ActivityFriendBinding binding;
    private FriendViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_friend);
        adapter = new FriendPostsAdapter(R.layout.item_friend, this);
        viewModel = new FriendViewModel(this, adapter);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle(StringUtil.getString(R.string.friend_plate));
        initRecyclerView(binding.rv);
        initRefresh(binding.storeHousePtrFrame);
        binding.setViewModel(viewModel);
        setBarHeight(binding.toolbar.ivBar);
        setBarHeight(binding.toolbar.ivBar);
    }

    public void initRecyclerView(RecyclerView rv) {
        RecycleViewUtils.setEmptyView(adapter, rv, getLayoutInflater(), getString(R.string
                .friend_none_data_tips));
        //上拉刷新设置
        adapter.setHasStableIds(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);
        rv.addOnItemTouchListener(viewModel.itemClickListener());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    @Override
    public void refreshData() {
        viewModel.getFriendPosts(1);
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
