package com.puxiang.mall.module.plate.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentPlateClassifyBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.plate.adapter.PlateRightAdapter;
import com.puxiang.mall.module.plate.viewmodel.PlateClassifyViewModel;

public class PlateClassifyFragment extends BaseBindFragment {

    private PlateRightAdapter rightAdapter;
    private FragmentPlateClassifyBinding binding;
    private PlateClassifyViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plate_classify, container, false);
        rightAdapter = new PlateRightAdapter(R.layout.item_plate_right);
        viewModel = new PlateClassifyViewModel(getActivity(), rightAdapter);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        initRecycler(binding.rvClassifyRight);
        binding.tabLayout.setItemOnClickListener(position -> viewModel.getRightData(position));
    }

    private void initRecycler(RecyclerView rvClassifyRight) {
        rvClassifyRight.setLayoutManager(new LinearLayoutManager(getContext()));
        rvClassifyRight.addOnItemTouchListener(viewModel.clickListener());
        rvClassifyRight.setAdapter(rightAdapter);
    }


//    private void initToolBar() {
//        binding.toolbar.tvUserName.setOnClickListener(this);
//        binding.toolbar.ivSearchIcon.setOnClickListener(this);
//        binding.toolbar.sdvBbsPic.setOnClickListener(this);
//    }
//

//
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.sdv_bbs_pic:
//            case R.id.tv_user_name:
//                if (StringUtil.isEmpty(MyApplication.TOKEN)) {
//                    ActivityUtil.startLoginActivity(getActivity());
//                } else {
//                    ActivityUtil.startPersonalActivity(getActivity(), MyApplication.USER_ID);
//                }
//                break;
//            case R.id.iv_search_icon:
//                ActivityUtil.startSearchBBsListActivity(getActivity(), "");
//                break;
//        }
//    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }
}
