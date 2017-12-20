package com.puxiang.mall.module.seller.view;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentStepOneBinding;
import com.puxiang.mall.databinding.FragmentStepTwoBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.seller.viewmodel.ApplyDealerViewModel;

/**
 * Created by zhaoyong bai on 2017/12/19.
 */

public class FragmentStepTwo extends BaseBindFragment implements View.OnClickListener {

    private FragmentStepTwoBinding binding;
    private ApplyDealerViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_two, container, false);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding.sdvBack.setOnClickListener(this);
        binding.sdvFace.setOnClickListener(this);
        binding.ivDelBack.setOnClickListener(this);
        binding.ivFaceDel.setOnClickListener(this);
    }

    public void setViewModel(ApplyDealerViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected void viewModelDestroy() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sdv_face:
                viewModel.getPic(0);
                break;

            case R.id.sdv_back:
                viewModel.getPic(1);
                break;

            case R.id.iv_face_del:
                viewModel.delFace();
                break;
            case R.id.iv_del_back:
                viewModel.delBack();
                break;
        }
    }
}
