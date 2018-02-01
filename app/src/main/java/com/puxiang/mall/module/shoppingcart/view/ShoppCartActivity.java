package com.puxiang.mall.module.shoppingcart.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityShoppingCartBinding;
import com.puxiang.mall.module.shoppingcart.adapter.ShoppingAdapter;
import com.puxiang.mall.module.shoppingcart.viewmodel.ShoppingCartActivityViewModel;

public class ShoppCartActivity extends BaseBindActivity implements View.OnClickListener {
    private ShoppingAdapter adapter;
    private ActivityShoppingCartBinding binding;
    private ShoppingCartActivityViewModel viewModel;


    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shopping_cart);
        adapter = new ShoppingAdapter(R.layout.item_shopping, R.layout.head_shop_cart, null);
        viewModel = new ShoppingCartActivityViewModel(this, adapter);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("购物车");
        binding.toolbar.ivBack.setVisibility(View.GONE);
        adapter.openLoadAnimation();
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setAdapter(adapter);
        binding.rv.addOnItemTouchListener(viewModel.itemChildClickListener());
        binding.setViewModel(viewModel);
        binding.tvQx.setOnClickListener(this);
        binding.cbAll.setOnClickListener(this);
        binding.btnBuy.setOnClickListener(this);
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.ivBack.setOnClickListener(this);
        binding.llNone.ivStartBuy.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.isLogin()) {
            viewModel.getData();
        } else {
            adapter.setData(null);
            viewModel.isNone.set(true);
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_qx:
                binding.cbAll.setChecked(!binding.cbAll.isChecked());
            case R.id.cb_all:
                viewModel.selectAll(binding.cbAll.isChecked());
                break;
            case R.id.iv_start_buy:
                viewModel.startBuy();
                break;
            case R.id.btn_buy:
                viewModel.buy();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
