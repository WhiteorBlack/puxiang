package com.puxiang.mall.module.scan.viewmodel;

import android.content.Intent;
import android.util.Log;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.model.data.RxSing;
import com.puxiang.mall.module.web.view.WebActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.ToastUtil;

public class QRCodeViewModel implements ViewModel {

    public static final String RESULT_FLAG = "qrSingResult";
    public static final int RESULT_CODE_SING = 200;
    private final BaseBindActivity activity;
    private final LoadingWindow loadingWindow;
    private String TAG = "QRCodeViewModel";

    public QRCodeViewModel(BaseBindActivity activity) {
        this.activity = activity;
        loadingWindow = new LoadingWindow(activity);
    }

    public void qrSing(String qrCode) {

        //获取电竞馆ID
        String shopId = activity.getIntent().getStringExtra("esportShopId");
        Double latitude = activity.getIntent().getDoubleExtra("latitude", 0);
        Double longitude = activity.getIntent().getDoubleExtra("longitude", 0);

        Log.e(TAG, "qrSing: " + "shopId : " + shopId + "  latitude : " + latitude + "  longitude : " + longitude);
        //发起签到请求
        ToastUtil.toast("签到中...");
        ApiWrapper.getInstance()
                .checkCard(shopId, latitude, longitude, qrCode)
                .doOnTerminate(loadingWindow::dismiss)
                .subscribe(new NetworkSubscriber<RxSing>() {
                    @Override
                    public void onSuccess(RxSing data) {
                        if ("1".equals(data.getStatus())) {
                            singSuccess();
                        }
                    }
                });
    }

    /**
     * 签到结果处理
     */
    private void singSuccess() {
        Intent data = new Intent(activity, WebActivity.class).putExtra(RESULT_FLAG, true);
        activity.setResult(RESULT_CODE_SING, data);
        activity.finish();
    }

    @Override
    public void destroy() {
        loadingWindow.dismiss();
    }
}
