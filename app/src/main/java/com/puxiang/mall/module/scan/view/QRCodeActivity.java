package com.puxiang.mall.module.scan.view;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.flyco.dialog.widget.NormalDialog;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityQrcodeBinding;
import com.puxiang.mall.module.scan.viewmodel.QRCodeViewModel;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.widget.dialog.DefaultDialog;
import com.puxiang.mall.widget.dialog.OnDialogExecuteListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * Desc : 二维码扫描
 * version : v1.0
 */

public class QRCodeActivity extends BaseBindActivity implements QRCodeView.Delegate {
    private ActivityQrcodeBinding binding;
    private ZBarView mQRCodeView;
    private QRCodeViewModel viewModel;
    private NormalDialog dialog;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qrcode);
        mQRCodeView = binding.zScan;
        viewModel = new QRCodeViewModel(this);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("扫码");
        mQRCodeView.setDelegate(this);
    }

    /**
     * 请求相机权限。
     */
    private void requestCAMERAPermission() {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        mQRCodeView.startCamera();
                        mQRCodeView.showScanRect();
                        mQRCodeView.startSpot();
                    } else {
                        ToastUtil.toast("需要访问的相机~");
                        finish();
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        requestCAMERAPermission();
//        mQRCodeView.startCamera();
//        mQRCodeView.showScanRect();
//        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        ToastUtil.toast(result);

        if (result.contains("mbbs.esomic.com")) {
            viewModel.qrSing(result);
        } else {
            if (dialog == null) {
                createDialog();
            }
            dialog.show();

        }
        vibrate();
        mQRCodeView.startSpotDelay(3000);
    }

    private void createDialog() {
        dialog = new DefaultDialog(this, "为了安全，蒲象商城暂不支持第三方二维码扫描。", new OnDialogExecuteListener() {
            @Override
            public void execute() {

            }

            @Override
            public void cancel() {

            }
        });
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null)
            viewModel.destroy();
    }
}
