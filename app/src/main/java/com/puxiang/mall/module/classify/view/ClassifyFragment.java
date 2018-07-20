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
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.classify.adapter.ClassfyRightSectionAdapter;
import com.puxiang.mall.module.classify.viewmodel.ClassifyViewModel;
import com.puxiang.mall.utils.ActivityUtil;

/**
 * Created by ChenHengQuan on 2016/8/2.
 */
public class ClassifyFragment extends BaseBindFragment implements View.OnClickListener {

    private ClassfyRightSectionAdapter rightAdapter;
    private FragmentClassifyBinding binding;
    private ClassifyViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classify, container, false);
        rightAdapter = new ClassfyRightSectionAdapter(R.layout.item_classify_right, R.layout.item_classfy_title, null);
        viewModel = new ClassifyViewModel(this, rightAdapter);
        return binding.getRoot();
    }

    @Override
    public void update() {
        super.update();
    }

    /**
     * 初始化view
     */
    @Override
    public void initView() {
        initRecycler();
        binding.toolbar.llClassifyToolbar.setOnClickListener(this);
        initTopImage();
        setBarHeight(binding.toolbar.ivBar);
    }

    /**
     * 初始化顶部图片尺寸
     */
    private void initTopImage() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.sdvClassfy.getLayoutParams();
        params.height = (int) ((MyApplication.widthPixels * 2 / 3 - 40) * 0.5);
        binding.sdvClassfy.setLayoutParams(params);
    }

    /**
     * 初始化RV
     */
    private void initRecycler() {
        binding.rv.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
        binding.rv.addOnItemTouchListener(viewModel.clickListener());
        binding.rv.setAdapter(rightAdapter);
        binding.vtl.setTabMargin(120);
        binding.vtl.setItemOnClickListener(position -> viewModel.setRightData(position));
        binding.setViewModel(viewModel);
        binding.rv.setNestedScrollingEnabled(false);
        binding.rv.setHasFixedSize(true);
        binding.rv.setFocusable(false);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_classify_toolbar:
            case R.id.tv_search:
                ActivityUtil.startSearchActivity(this.getActivity(), "");
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) {
            viewModel.destroy();
        }
    }
}
