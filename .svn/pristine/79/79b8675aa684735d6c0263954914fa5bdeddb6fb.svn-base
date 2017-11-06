package com.puxiang.mall.module.shoppingcart.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentShoppingCartBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.shoppingcart.adapter.ShoppingAdapter;
import com.puxiang.mall.module.shoppingcart.viewmodel.ShoppingCartViewModel;

public class ShoppCartFragment extends BaseBindFragment implements View.OnClickListener {
    private ShoppingAdapter adapter;
    private FragmentShoppingCartBinding binding;
    private ShoppingCartViewModel viewModel;


    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_cart, container, false);
        adapter = new ShoppingAdapter(R.layout.item_shopping, R.layout.head_shop_cart, null);
        viewModel = new ShoppingCartViewModel(this, adapter);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("购物车");
        binding.toolbar.ivBack.setVisibility(View.GONE);
        adapter.openLoadAnimation();
        binding.rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.rv.setAdapter(adapter);
        binding.setViewModel(viewModel);
        binding.tvQx.setOnClickListener(this);
        binding.cbAll.setOnClickListener(this);
        binding.btnBuy.setOnClickListener(this);
        binding.llNone.ivStartBuy.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getData();
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
        }
    }
}
