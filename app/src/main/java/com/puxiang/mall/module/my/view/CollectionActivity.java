package com.puxiang.mall.module.my.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityCollectionBinding;
import com.puxiang.mall.module.my.adapter.CollectionAdapter;
import com.puxiang.mall.module.my.viewmodel.CollectionViewModel;

/**
 * Created by zhaoyong bai on 2017/12/11.
 */

public class CollectionActivity extends BaseBindActivity {
    private ActivityCollectionBinding binding;
    private CollectionViewModel viewModel;
    private CollectionAdapter adapter;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_collection);
        adapter = new CollectionAdapter(R.layout.item_collection);
        viewModel = new CollectionViewModel(this, adapter);
        binding.setViewModel(viewModel);

    }

    private void initRv(RecyclerView rvCollect) {
        rvCollect.setLayoutManager(new GridLayoutManager(this, 2));
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rvCollect);
        rvCollect.setAdapter(adapter);
        rvCollect.addOnItemTouchListener(viewModel.onItemTouchListener());
    }

    @Override
    public void initView() {
        initRv(binding.rvCollect);
        binding.toolbar.setTitle("我的收藏");
        binding.toolbar.setBackSrc(R.mipmap.nav_back_g);
        binding.toolbar.setColor(R.color.white);
        binding.toolbar.setTextColor(R.color.text_black);
        viewModel.getCollectProducts(10,1);
    }

    @Override
    protected void viewModelDestroy() {

    }
}
