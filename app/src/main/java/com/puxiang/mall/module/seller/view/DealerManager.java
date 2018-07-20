package com.puxiang.mall.module.seller.view;

import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.WindowManager;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityDealerManagerBinding;
import com.puxiang.mall.module.seller.viewmodel.DealerManagerViewModel;

/**
 * Created by zhaoyong bai on 2018/5/4.
 */
public class DealerManager extends BaseBindActivity {
    private ActivityDealerManagerBinding binding;
    private DealerManagerViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dealer_manager);
        viewModel = new DealerManagerViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("经销商管理");
        mImmersionBar.keyboardEnable(false).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED).init();
    }

    public void onClick(View view) {
        onBackPressed();
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) {
            viewModel.destroy();
        }
    }
}
