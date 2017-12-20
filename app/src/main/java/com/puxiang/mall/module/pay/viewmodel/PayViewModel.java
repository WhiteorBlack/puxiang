package com.puxiang.mall.module.pay.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableDouble;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxPayChannel;
import com.puxiang.mall.model.data.RxPayPrice;
import com.puxiang.mall.model.data.RxWXPayInfo;
import com.puxiang.mall.module.pay.alipay.PayResult;
import com.puxiang.mall.module.pay.view.PayActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.widget.dialog.DefaultDialog;
import com.puxiang.mall.widget.dialog.OnDialogExecuteListener;
import com.puxiang.mall.wxapi.WeChatPay;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.unionpay.UPPayAssistEx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PayViewModel implements ViewModel {
    public static final int PLUGIN_VALID = 0;
    private static final int PLUGIN_NOT_INSTALLED = -1;
    private static final int PLUGIN_NEED_UPGRADE = 2;
    private final String mMode = "00";

    private final PayActivity activity;
    public ObservableDouble totalPrices = new ObservableDouble(0.00);
    public ObservableBoolean isInitData = new ObservableBoolean();
    public ObservableField<RxPayChannel> aliPayChannel = new ObservableField<>();
    public ObservableField<RxPayChannel> wxPayChannel = new ObservableField<>();
    public ObservableField<RxPayChannel> unionPayChannel = new ObservableField<>();
    private String orderId;

    private LoadingWindow loadingWindow;
    private String TAG = "PayViewModel";
    private DefaultDialog dialog;

    public PayViewModel(PayActivity activity) {
        EventBus.getDefault().register(this);
        this.activity = activity;
        loadingWindow = new LoadingWindow(activity);
        loadingWindow.delayedShowWindow();
        getCache();
        getPayInfo(Config.PRODUCTORDER, orderId);
        getPayChannel();
        createDialog();
    }

    private void createDialog() {
        dialog = new DefaultDialog(activity, "网络连接失败,请重试!", new OnDialogExecuteListener() {
            @Override
            public void execute() {

            }

            @Override
            public void cancel() {

            }
        });
    }

    private void getCache() {
        orderId = activity.getIntent().getStringExtra("orderId");
        activity.setPageTag("orderId:", orderId);
        Log.e(TAG, "getCache: " + orderId);
    }

    private void getPayChannel() {
        ApiWrapper.getInstance()
                .getPayChannel()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnComplete(() -> isInitData.set(true))
                .subscribe(new NetworkSubscriber<List<RxPayChannel>>() {
                    @Override
                    public void onSuccess(List<RxPayChannel> list) {
                        for (RxPayChannel payChannel : list) {
                            switch (payChannel.getCode()) {
                                case RxPayChannel.PAY_CHANNEL_ALIPAY:
                                    aliPayChannel.set(payChannel);
                                    break;
                                case RxPayChannel.PAY_CHANNEL_WEIXIN:
                                    wxPayChannel.set(payChannel);
                                    break;
                                case RxPayChannel.PAY_CHANNEL_UNIONPAY:
                                    unionPayChannel.set(payChannel);
                                    break;
                            }
                        }
                    }
                });
    }

    private void wxPay(RxWXPayInfo bean) {
        WeChatPay weChatPay = new WeChatPay(activity, bean);
        weChatPay.payWechat();
    }

    private void getPayInfo(String orderType, String orderId) {

        ApiWrapper.getInstance()
                .getPayInfo(orderType, orderId)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(loadingWindow::delayHideWindow)
                .doOnComplete(() -> isInitData.set(true))
                .subscribe(new NetworkSubscriber<RxPayPrice>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Logger.e(e.toString());
                    }

                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        activity.showNoneView("当前网络不可用~");
                    }

                    @Override
                    public void onSuccess(RxPayPrice bean) {
                        Logger.e("totalPrice--"+bean.getTotalPrice());
                        totalPrices.set(bean.getTotalPrice());
                    }
                });
    }


    /**
     * 获取银联支付签名
     */
    public void unionpayPay() {
        loadingWindow.showWindow();
        ApiWrapper.getInstance()
                .unionpaySign(0, orderId)
                .doOnTerminate(() -> loadingWindow.delayHideWindow())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String result) {
                        startUnionPay(result);
                    }
                });
    }

    /**
     * 调起银联支付
     */
    private void startUnionPay(String result) {
        String tn;
        if (StringUtil.isEmpty(result)) {
            dialog.show();
        } else {
            tn = result;
            doStartUnionPayPlugin(activity, tn, mMode);
        }
    }

    /**
     * 步骤2：通过银联工具类启动支付插件
     */
    private void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
        // mMode参数解释：
        // 0 - 启动银联正式环境
        // 1 - 连接银联测试环境
        int ret = UPPayAssistEx.startPay(activity, null, null, tn, mode);
        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
            // 需要重新安装控件
            Log.e("UnionPay", " plugin not found or need upgrade!!!");

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("提示");
            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

            builder.setNegativeButton("确定",
                    (dialog, which) -> {
                        UPPayAssistEx.installUPPayPlugin(activity);
                        dialog.dismiss();
                    });

            builder.setPositiveButton("取消",
                    (dialog, which) -> dialog.dismiss());
            builder.create().show();
        }
        Log.e("UnionPay", "" + ret);
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void aliPay() {
        ApiWrapper.getInstance()
                .alipaySign(Config.PRODUCTORDER, orderId)
                .subscribeOn(Schedulers.io())
                .map((String alipaySign) -> {
                    PayTask alipay = new PayTask(activity);
                    Logger.e("alipaySign--"+alipaySign);
                    // 调用支付接口，获取支付结果
                    return alipay.pay(alipaySign, true);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String result) {
                        aliPayResult(result);
                    }
                });
    }

    private void aliPayResult(String result) {
        PayResult payResult = new PayResult(result);
//TODO:同步返回的结果必须放置到服务端进行验证
//（验证的规则请看https://doc.open.alipay.com/doc2/detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&docType=1)
//建议商户依赖异步通知

        Log.e("333", "handleMessage: " + payResult.getResult());
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
        if (TextUtils.equals(resultStatus, "9000")) {
            ActivityUtil.startPayResultActivity(activity, 9000, orderId, totalPrices.get());

        } else {
            // 判断resultStatus 为非"9000"则代表可能支付失败
            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
                ActivityUtil.startPayResultActivity(activity, 8000, orderId,
                        totalPrices.get());
                ToastUtil.toast("支付结果确认中");
            } else {
                ActivityUtil.startPayResultActivity(activity, -1, orderId,
                        totalPrices.get());
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                ToastUtil.toast("支付失败");
            }
        }
    }

    /**
     * 获取微信支付信息
     */
    public void getWeixinPayInfo() {
        loadingWindow.showWindow();
        ApiWrapper.getInstance()
                .weixinPaySign(Config.PRODUCTORDER, 0, orderId)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> loadingWindow.hidWindow())
                .subscribe(new NetworkSubscriber<RxWXPayInfo>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Logger.e("wx"+e.toString());
                    }

                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        activity.showNoneView("当前网络不可用~");
                    }

                    @Override
                    public void onSuccess(RxWXPayInfo bean) {
                        Logger.e("wx"+bean.toString());
                        wxPay(bean);
                    }
                });
    }

    /**
     * 微信支付结果回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseResp resp) {
        int errCode = resp.errCode;
        ActivityUtil.startPayResultActivity(activity, errCode, orderId, totalPrices.get());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.FINISH||i==Event.GO_HOME) {
            activity.finish();
        }
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
        loadingWindow.dismiss();
    }

    /*************************************************
     * 步骤3：处理银联手机支付控件返回的支付结果
     ************************************************/
    public void showPayResult(Intent data) {
        if (data == null) {
            return;
        }
        int errCode = -1;
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str != null && str.equalsIgnoreCase("success")) {
            // 结果result_data为成功时，去商户后台查询一下再展示成功
            errCode = 9000;
            ToastUtil.toast("支付成功");
        } else {
            ToastUtil.toast("支付失败");
        }
        ActivityUtil.startPayResultActivity(activity, errCode, orderId, totalPrices.get());
    }
}
