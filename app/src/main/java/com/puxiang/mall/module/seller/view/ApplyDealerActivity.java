package com.puxiang.mall.module.seller.view;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityApplyDealerBinding;
import com.puxiang.mall.module.seller.viewmodel.ApplyDealerViewModel;
import com.puxiang.mall.module.shop.adapter.SimpleFragmentAdapter;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2017/12/19.
 * 申请成为经销商
 */

public class ApplyDealerActivity extends BaseBindActivity implements EasyPermission.PermissionCallback {
    private ActivityApplyDealerBinding binding;
    private ApplyDealerViewModel viewModel;
    private List<Fragment> fragments = new ArrayList<>();
    private SimpleFragmentAdapter fragmentAdapter;
    private int pos = 0;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_apply_dealer);
        viewModel = new ApplyDealerViewModel(this);
        binding.setViewModel(viewModel);
        mImmersionBar.keyboardEnable(false).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED).statusBarDarkFont(false).flymeOSStatusBarFontColor(R.color.white).init();
    }

    @Override
    public void initView() {
        setWhiteTitle(binding.toolbar);
        binding.toolbar.setTitle("成为经销商");
        fragmentAdapter = new SimpleFragmentAdapter(getSupportFragmentManager(), fragments);
        binding.viewpager.setAdapter(fragmentAdapter);
//        binding.viewpager.setCanScroll(true);
        setBarHeight(binding.toolbar.ivBar);
        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
                switch (position) {
                    case 0:
                        viewModel.setStepText("下一步");
                        break;
                    case 1:
                        viewModel.setStepTwo(true);
                        binding.btnStep.setText("提交");
                        break;
                    case 2:
                        viewModel.setStepThree(true);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setViewPagerData();
    }

    public void setViewPagerData() {
        FragmentStepOne fragmentStepOne = new FragmentStepOne();
        fragmentStepOne.setViewModel(viewModel);
        fragments.add(fragmentStepOne);
        FragmentStepTwo fragmentStepTwo = new FragmentStepTwo();
        fragmentStepTwo.setViewModel(viewModel);
        fragments.add(fragmentStepTwo);
        FragmentStepThree fragmentStepThree = new FragmentStepThree();
        fragmentStepThree.setViewModel(viewModel);
        fragments.add(fragmentStepThree);
        fragmentAdapter.notifyDataSetChanged();
    }

    public void setCurrentItem(int pos) {
        binding.viewpager.setCurrentItem(pos);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_step:
                viewModel.onStepClick(pos);
//                pos++;
                break;
            case R.id.btn_back:
                if (pos == 2) {
                    if (viewModel.status == 3) { //取消
                        onBackPressed();
                        return;
                    }

                    if (viewModel.status == 2) {
                        //查看详情

                        return;
                    }
                }
                pos--;
                pos = pos < 0 ? 0 : pos;
                setCurrentItem(pos);
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        if (pos == 1) {
//            binding.viewpager.setCurrentItem(0);
//            return;
//        } else {
//            finish();
//        }
        super.onBackPressed();
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {
        if (requestCode == PermissionCode.RG_CAMERA_PERM) {
            if (viewModel != null) {
                viewModel.openCamera();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case PermissionCode.RG_CAMERA_PERM:
                ToastUtil.toast(getString(R.string.rationale_camera));
                break;
        }
    }
}
