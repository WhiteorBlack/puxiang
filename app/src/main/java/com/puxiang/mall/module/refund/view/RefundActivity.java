package com.puxiang.mall.module.refund.view;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.flyco.dialog.widget.NormalListDialog;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityRefundBinding;
import com.puxiang.mall.module.bbs.adapter.PublishAdapter;
import com.puxiang.mall.module.refund.viewmodel.RefundViewModel;
import com.puxiang.mall.utils.MyTextUtils;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;

import java.util.List;

public class RefundActivity extends BaseBindActivity implements EasyPermission.PermissionCallback {
    private ActivityRefundBinding binding;
    private PublishAdapter adapter;
    private RefundViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_refund);
        adapter = new PublishAdapter(R.layout.item_publish);
        viewModel = new RefundViewModel(this, adapter);
        binding.setViewModel(viewModel);
        mImmersionBar.keyboardEnable(false);
    }

    @Override
    public void initView() {

        initRV(binding.rvRefund);
        setWhiteTitle(binding.toolbarLayout);
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    private void initRV(RecyclerView rv) {
        rv.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(viewModel.itemClickListener());
    }

    public String getInputMoney() {
        return binding.etMoney.getText().toString();
    }

    public String getInputExplain() {
        return binding.etExplain.getText().toString();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_refund_reasons:
                NormalListDialog dialog = viewModel.dialog;
                if (dialog != null) {
                    dialog.show();
                }
                break;
            case R.id.btn:
                closeInput();
                if (MyTextUtils.isEmpty(binding.etMoney)) {
                    ToastUtil.toast("退款金额不能为空");
                    return;
                } else {
                    Double inputMoney = Double.valueOf(binding.etMoney.getText().toString());
                    if (inputMoney > viewModel.refundBean.get().getRefundMoney() || inputMoney <= 0) {
                        ToastUtil.toast("退款金额输入有误");
                        return;
                    }
                }
                if (MyTextUtils.isEmpty(binding.etExplain)) {
                    ToastUtil.toast("退款说明不能为空");
                    return;
                }
                if (viewModel.refundReasonId == -1) {
                    ToastUtil.toast("选择退款原因");
                    return;
                }
                if (viewModel.list.size() > 1) {
                    viewModel.publish();
                } else if (viewModel.isRefundMoney.get()) {
                    ToastUtil.toast("请上传凭证");
                } else {
                    viewModel.publish();
                }
                break;
        }
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
