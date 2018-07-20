package com.puxiang.mall.module.dealer.fragment;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentAllDealerBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.dealer.adapter.MineAdapter;
import com.puxiang.mall.module.dealer.viewmodel.MineViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/6/3.
 */
public class FragmentMine extends BaseBindFragment {
    private MineViewModel viewModel;
    private MineAdapter adapter;
    private FragmentAllDealerBinding binding;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        adapter = new MineAdapter(R.layout.item_mine_dealer);
        viewModel = new MineViewModel(this, adapter);
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_all_dealer,container,false);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rv.setAdapter(adapter);
        binding.rv.addOnItemTouchListener(viewModel.onItemTouchListener());
        viewModel.getDealerList(1);
        binding.btnAll.setOnClickListener(this::onClick);
        binding.btnCancel.setOnClickListener(this::onClick);
        binding.btnCenter.setOnClickListener(this::onClick);
        binding.btnCenter.setText("取关");
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
