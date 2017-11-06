package com.puxiang.mall.module.bbs.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentVideoBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxChildren;
import com.puxiang.mall.module.bbs.adapter.VideoClassifyLeftAdapter;
import com.puxiang.mall.module.bbs.adapter.VideoRightAdapter;
import com.puxiang.mall.module.bbs.viewmodel.VideoViewModel;
import com.puxiang.mall.widget.verticaltablayout.VTabLayout;

import java.util.List;

public class VideoFragment extends BaseBindFragment {

    private VideoClassifyLeftAdapter tabAdapter;
    private VideoViewModel viewModel;
    private FragmentVideoBinding binding;
    private VideoRightAdapter adapter;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false);
        return binding.getRoot();
    }

    @Override
    public void update() {
    }

    /**
     * 初始化view
     */
    public void initView() {
        List<RxChildren> list = getArguments().getParcelableArrayList("list");
        adapter = new VideoRightAdapter(R.layout.video_item_right);
        tabAdapter = new VideoClassifyLeftAdapter(getContext(), list);
        viewModel = new VideoViewModel(this, adapter, tabAdapter);
        initRecycler(binding.rvVideoRight);
        initVT(binding.tablayout);
        viewModel.getRightData(0);
    }

    private void initRecycler(RecyclerView rv) {
        rv.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        rv.addOnItemTouchListener(viewModel.itemClickListener());
        rv.setAdapter(adapter);
    }

    private void initVT(VTabLayout tabLayout) {
        tabLayout.setItemOnClickListener(viewModel.tabItemClickListener());
        tabLayout.setTabAdapter(tabAdapter);
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }
}
