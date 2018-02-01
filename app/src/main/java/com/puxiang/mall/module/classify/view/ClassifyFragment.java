package com.puxiang.mall.module.classify.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentClassifyBinding;
import com.puxiang.mall.databinding.ViewHeadBannerBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.classify.adapter.ClassifyRightAdapter;
import com.puxiang.mall.module.classify.viewmodel.ClassifyViewModel;
import com.puxiang.mall.module.classify.viewmodel.HeadBannerViewModel;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.utils.ActivityUtil;

/**
 * Created by ChenHengQuan on 2016/8/2.
 */
public class ClassifyFragment extends BaseBindFragment implements View.OnClickListener {

    private ClassifyRightAdapter rightAdapter;
    private FragmentClassifyBinding binding;
    private ClassifyViewModel viewModel;
    private ViewHeadBannerBinding headViewBinding;
    private HeadBannerViewModel headViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classify, container, false);
        rightAdapter = new ClassifyRightAdapter(R.layout.item_classify_right);
        viewModel = new ClassifyViewModel(this, rightAdapter);

        headViewModel = new HeadBannerViewModel(this);
        binding.layoutHead.setViewModel(headViewModel);
        return binding.getRoot();
    }

    @Override
    public void update() {
        super.update();
        headViewModel.getBannerData("classfy");
    }

    /**
     * 初始化view
     */
    @Override
    public void initView() {
        initRecycler();
        initHeanView();
        initBanner(binding.layoutHead.headBanner);
        binding.toolbar.llClassifyToolbar.setOnClickListener(this);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) binding.layoutHead.headBanner.getLayoutParams();
        params.height=(int) (MyApplication.widthPixels * 0.347*0.7);
        binding.layoutHead.headBanner.setLayoutParams(params);
    }

    private void initHeanView() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.layoutHead.headBanner.getLayoutParams();
        params.height = MyApplication.widthPixels * 7 / 20;
        binding.layoutHead.headBanner.setLayoutParams(params);
        binding.layoutHead.headBanner.setAdapter(headViewModel);
        binding.layoutHead.headBanner.setDelegate(headViewModel);
    }


    /**
     * 初始化RV
     */
    private void initRecycler() {
        binding.rv.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        binding.rv.addOnItemTouchListener(viewModel.clickListener());
        binding.rv.setAdapter(rightAdapter);
        binding.vtl.setItemOnClickListener(position -> viewModel.getRightData(position));
        binding.setViewModel(viewModel);
        binding.rv.setNestedScrollingEnabled(false);
        binding.rv.setHasFixedSize(true);
        binding.rv.setFocusable(false);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_classify_toolbar:
                ActivityUtil.startSearchActivity(this.getActivity(), "");
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) {
            viewModel.destroy();
        }
        if (headViewModel!=null) headViewModel.destroy();
    }
}
