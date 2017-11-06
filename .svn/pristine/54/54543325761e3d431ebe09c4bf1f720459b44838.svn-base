package com.puxiang.mall.module.integral.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityIntegralBinding;
import com.puxiang.mall.module.integral.adapter.IntegralAdapter;
import com.puxiang.mall.module.integral.viewmodel.IntegralViewModel;
import com.puxiang.mall.module.main.view.MainActivity;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.utils.RecycleViewUtils;
import com.puxiang.mall.utils.WebUtil;

public class IntegralActivity extends BaseBindActivity {
    private IntegralAdapter adapter;
    private ActivityIntegralBinding binding;
    private IntegralViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_integral);
        adapter = new IntegralAdapter(R.layout.item_integral, R.layout.item_integral_type, null);
        RecycleViewUtils.setEmptyView(adapter, binding.rv, getLayoutInflater(), "抱歉~出错啦");
        viewModel = new IntegralViewModel(this, adapter);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("任务中心");
        initRV(binding.rv);
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    private void initRV(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addOnItemTouchListener(viewModel.itemClickListener());
        rv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getData();
    }

    @Override
    public void onBackPressed() {
        if (!MainActivity.isInit) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            super.onBackPressed();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn:
                WebUtil.jumpWeb(URLs.HTML_EXCHANGE_PAGE, this);
                break;
        }
    }
}
