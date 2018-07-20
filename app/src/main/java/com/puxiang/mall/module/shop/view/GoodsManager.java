package com.puxiang.mall.module.shop.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.databinding.ActivityGoodsManagerBinding;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.module.shop.adapter.GoodsAdapter;
import com.puxiang.mall.module.shop.viewModel.GoodsManagerViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;
import com.puxiang.mall.utils.ToastUtil;

/**
 * Created by zhaoyong bai on 2018/4/25.
 */
public class GoodsManager extends BaseBindActivity {
    private GoodsManagerViewModel viewModel;
    private ActivityGoodsManagerBinding binding;
    private GoodsAdapter adapter;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goods_manager);
        adapter = new GoodsAdapter(R.layout.item_goods_manager);
        viewModel = new GoodsManagerViewModel(this, adapter);
        binding.setViewModel(viewModel);
        mImmersionBar.keyboardEnable(false).init();
    }

    @Override
    public void initView() {
        initRv(binding.rvGoods);
        viewModel.getShopGoods("", MyApplication.SHOP_ID, 1);
//        viewModel.getTestData();
    }

    private void initRv(RecyclerView rvGoods) {
        adapter.setEnableLoadMore(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setOnLoadMoreListener(() -> viewModel.onLoadMore(), rvGoods);
        rvGoods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvGoods.setAdapter(adapter);
        rvGoods.addOnItemTouchListener(viewModel.onItemTouchListener());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search_btn:
                String keyword = binding.toolbar.et.getText().toString();

                viewModel.getShopGoods(keyword, MyApplication.SHOP_ID, 1);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            String dataString = getIntent().getStringExtra("data");
            if (TextUtils.isEmpty(dataString)) {
                viewModel.getShopGoods("", MyApplication.SHOP_ID, 1);
            } else{
                viewModel.modifyGoods(dataString);
            }
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
