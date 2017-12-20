package com.puxiang.mall.module.seller.view;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentStepOneBinding;
import com.puxiang.mall.databinding.FragmentStepThreeBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.seller.viewmodel.ApplyDealerViewModel;

/**
 * Created by zhaoyong bai on 2017/12/19.
 */

public class FragmentStepThree extends BaseBindFragment {

    private FragmentStepThreeBinding binding;
    private ApplyDealerViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_three, container, false);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {

    }

    public void setViewModel(ApplyDealerViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected void viewModelDestroy() {

    }
}
