package com.puxiang.mall.module.my.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityPostAddressBinding;
import com.puxiang.mall.module.my.adapter.PostAddressAdapter;
import com.puxiang.mall.module.my.viewmodel.PostAddressViewModel;
import com.puxiang.mall.utils.ActivityUtil;

/**
 * Created by zhaoyong bai on 2017/11/14.
 */

public class PostAddress extends BaseBindActivity {
    private ActivityPostAddressBinding binding;
    private PostAddressViewModel viewModel;
    private PostAddressAdapter addressAdapter;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_address);
        addressAdapter = new PostAddressAdapter(R.layout.item_post_address);
        viewModel = new PostAddressViewModel(addressAdapter, this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("收货地址");
        initPostRv(binding.rvAddress);
        setBarHeight(binding.toolbar.ivBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getAddressData();
    }

    private void initPostRv(RecyclerView rvAddress) {
        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.line_address));
        rvAddress.addItemDecoration(itemDecoration);
        rvAddress.setAdapter(addressAdapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_add:
                ActivityUtil.startAddEditAddressActivity(this, null);
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {

    }
}
