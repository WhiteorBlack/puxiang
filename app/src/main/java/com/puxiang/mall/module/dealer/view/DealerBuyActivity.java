package com.puxiang.mall.module.dealer.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityDealerBuyBinding;
import com.puxiang.mall.module.dealer.adapter.MineAdapter;
import com.puxiang.mall.module.dealer.viewmodel.BuyViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/6/4.
 */
public class DealerBuyActivity extends BaseBindActivity {
    private BuyViewModel viewModel;
    private MineAdapter adapter;
    private ActivityDealerBuyBinding binding;

    @Override
    protected void initBind() {
        adapter = new MineAdapter(R.layout.item_buy_dealer);
        viewModel = new BuyViewModel(this, adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dealer_buy);
    }


    public void initView() {
        binding.toolbar.setTitle("采购");
        initRv(binding.rv);
        viewModel.getDealerList(1);
    }

    private void initRv(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setEnableLoadMore(true);
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);
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
        viewModel.destroy();
    }
}
