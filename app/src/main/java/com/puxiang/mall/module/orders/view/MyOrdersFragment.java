package com.puxiang.mall.module.orders.view;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentMyOrdersBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.orders.adapter.MyOrderAdapter;
import com.puxiang.mall.module.orders.viewmodel.MyOrderFragmentViewModel;

/**
 * Created by zhaoyong bai on 2017/12/15.
 */

public class MyOrdersFragment extends BaseBindFragment {
    private FragmentMyOrdersBinding binding;
    private MyOrderAdapter adapter;
    private MyOrderFragmentViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_orders, container, false);

        return binding.getRoot();
    }

    @Override
    public void initView() {

    }

    @Override
    protected void viewModelDestroy() {

    }
}
