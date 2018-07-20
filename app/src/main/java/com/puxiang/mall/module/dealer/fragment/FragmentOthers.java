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
import com.puxiang.mall.module.dealer.viewmodel.OthersViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/6/3.
 */
public class FragmentOthers extends BaseBindFragment {
    private MineAdapter adapter;
    private OthersViewModel viewModel;
    private FragmentAllDealerBinding binding;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        adapter = new MineAdapter(R.layout.item_others_dealer);
        viewModel = new OthersViewModel(this, adapter);
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_all_dealer,container,false);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rv.setAdapter(adapter);
        binding.rv.addOnItemTouchListener(viewModel.onItemTouchListener());
        viewModel.getDealerList(1);
        binding.llContent.setVisibility(View.GONE);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
