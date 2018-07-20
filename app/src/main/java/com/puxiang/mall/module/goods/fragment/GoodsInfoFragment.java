package com.puxiang.mall.module.goods.fragment;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentGoodsInfoBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.goods.viewmodel.GoodsInfoViewModel;

/**
 * Created by zhaoyong bai on 2018/3/23.
 */

public class GoodsInfoFragment extends BaseBindFragment {
    private FragmentGoodsInfoBinding binding;
    private GoodsInfoViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goods_info, container, false);
        viewModel = new GoodsInfoViewModel(this);
        return binding.getRoot();
    }

    @Override
    public void initView() {

    }

    @Override
    protected void viewModelDestroy() {

    }
}
