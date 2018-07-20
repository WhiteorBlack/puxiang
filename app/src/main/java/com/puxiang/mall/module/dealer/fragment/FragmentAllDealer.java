package com.puxiang.mall.module.dealer.fragment;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentAllDealerBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.dealer.adapter.DealerAdapter;
import com.puxiang.mall.module.dealer.viewmodel.AllViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/6/3.
 */
public class FragmentAllDealer extends BaseBindFragment {
    private DealerAdapter adapter;
    private AllViewModel viewModel;
    private FragmentAllDealerBinding binding;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        adapter = new DealerAdapter(R.layout.item_all_dealer);
        viewModel = new AllViewModel(this, adapter);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_dealer, container, false);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rv.setAdapter(adapter);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView(false));
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), binding.rv);
        binding.rv.addOnItemTouchListener(viewModel.onItemTouchListener());
        binding.btnAll.setOnClickListener(this::onClick);
        binding.btnCancel.setOnClickListener(this::onClick);
        binding.btnCenter.setOnClickListener(this::onClick);
        binding.btnCenter.setText("关联");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_all:
                viewModel.selectAll();
                break;

            case R.id.btn_center:
                viewModel.link();
                break;
            case R.id.btn_cancel:
                viewModel.cancelAll();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
