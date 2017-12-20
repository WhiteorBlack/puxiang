package com.puxiang.mall.module.seller.view;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gyf.barlibrary.ImmersionBar;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentStepOneBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.seller.viewmodel.ApplyDealerViewModel;


/**
 * Created by zhaoyong bai on 2017/12/19.
 */

public class FragmentStepOne extends BaseBindFragment implements View.OnClickListener {

    private FragmentStepOneBinding binding;
    private ApplyDealerViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_one, container, false);
        binding.setViewModel(viewModel);
        ImmersionBar.with(this).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE).keyboardEnable(false).init();
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding.tvCity.setOnClickListener(this);
    }

    public void setViewModel(ApplyDealerViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected void viewModelDestroy() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_city:
                closeInput();
                viewModel.showPicker();
                break;
        }
    }
}
