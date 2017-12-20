package com.puxiang.mall.module.my.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.AcitivityAddEditAddressBinding;
import com.puxiang.mall.model.data.RxPostAddress;
import com.puxiang.mall.module.my.viewmodel.AddEditViewModel;

/**
 * Created by zhaoyong bai on 2017/11/15.
 */

public class AddEditAddressActivity extends BaseBindActivity {
    private AcitivityAddEditAddressBinding binding;
    private AddEditViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.acitivity_add_edit_address);
        viewModel = new AddEditViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        viewModel.setAdd(bundle == null);
        binding.toolbar.setEdit("保存");
        if (viewModel.isAdd()) {
            binding.toolbar.setTitle("添加地址");
        } else {
            binding.toolbar.setTitle("编辑地址");
        }

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_add_edit:
                viewModel.saveAddress(binding.chbDefault.isChecked());
                closeInput();
                break;
            case R.id.tv_address:
                closeInput();
                viewModel.showPicker();
                break;
            case R.id.ll_del:
                viewModel.deleteAddress();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {

    }
}
