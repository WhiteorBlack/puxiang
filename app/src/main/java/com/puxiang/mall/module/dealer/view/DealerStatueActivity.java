package com.puxiang.mall.module.dealer.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityDealerStatueBinding;
import com.puxiang.mall.module.dealer.viewmodel.StatueViewModel;

/**
 * Created by zhaoyong bai on 2018/6/3.
 */
public class DealerStatueActivity extends BaseBindActivity {
    private StatueViewModel viewModel;
    private ActivityDealerStatueBinding binding;

    @Override
    protected void initBind() {
        viewModel = new StatueViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dealer_statue);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("审核详情");
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

    }
}
