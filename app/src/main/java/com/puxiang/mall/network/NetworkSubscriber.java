package com.puxiang.mall.network;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.R;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 功能：订阅处理结果,可以不局限于网络,但{@link #onError(Throwable)}仅处理了网络出错的情况
 * 修改：
 */

public abstract class NetworkSubscriber<T> implements Observer<T> {
    private boolean isShowToast = true;
    public Disposable mDisposable;

    protected NetworkSubscriber() {
    }

    public NetworkSubscriber(boolean isShowToast) {
        this.isShowToast = isShowToast;
    }


    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    public void dispose() {
        mDisposable.dispose();
    }

    @Override
    public void onError(Throwable e) {
        Throwable throwable = e;
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }
        RetrofitUtil.APIException exception;
        if (e instanceof RetrofitUtil.APIException) {//response is success data exception
            exception = (RetrofitUtil.APIException) e;
        } else if (e instanceof HttpException) {//response fail
            exception = new RetrofitUtil.APIException(((HttpException) e).code(), StringUtil
                    .getString(R.string.http_error, ((HttpException) e).code()));
        } else if (e instanceof SocketTimeoutException) {
            exception = new RetrofitUtil.APIException(RetrofitUtil.APIException.SOCKE_TTIMEOUT,
                    StringUtil.getString(R.string.net_timeout));
        } else {//未知错误
            Logger.e(e.toString());
            exception = new RetrofitUtil.APIException(RetrofitUtil.APIException
                    .NETWORK_ERROR_STATUS, StringUtil.getString(R.string.net_error));
        }
        e.printStackTrace();
        onFail(exception);
    }

    @Override
    public void onNext(T data) {
        onSuccess(data);
    }

    public abstract void onSuccess(T data);

    public void onFail(RetrofitUtil.APIException e) {
        showToast(e.getMessage());
    }

    private void showToast(String msg) {
        if (isShowToast) {
            ToastUtil.toast(msg);
        }
    }
}