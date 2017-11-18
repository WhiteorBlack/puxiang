package com.puxiang.mall.module.my.viewmodel;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxPostAddress;
import com.puxiang.mall.module.my.adapter.PostAddressAdapter;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2017/11/14.
 */

public class PostAddressViewModel implements ViewModel, BaseQuickAdapter.OnItemChildClickListener {
    private PostAddressAdapter addressAdapter;
    private BaseBindActivity activity;
    private LoadingWindow loadingWindow;

    public PostAddressViewModel(PostAddressAdapter addressAdapter, BaseBindActivity activity) {
        this.addressAdapter = addressAdapter;
        this.activity = activity;
        loadingWindow = new LoadingWindow(activity);
//        loadingWindow.delayedShowWindow();
//        getAddressData();
        this.addressAdapter.setOnItemChildClickListener(this);
    }

    public void getAddressData() {
        ApiWrapper.getInstance()
                .getAddresses()
                .doOnTerminate(loadingWindow::hidWindow)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxPostAddress>>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(List<RxPostAddress> data) {
                        addressAdapter.setNewData(data);
                    }
                });
    }

    private void deleteAddress(String addressId) {
        ApiWrapper.getInstance()
                .deleteAddress(addressId)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                    }
                });
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_edit:
                Bundle bundle = new Bundle();
                bundle.putString("json", new Gson().toJson(((List<RxPostAddress>) adapter.getData()).get(position), RxPostAddress.class));
                ActivityUtil.startAddEditAddressActivity(activity, bundle);
                break;
            case R.id.tv_del:
                deleteAddress(addressAdapter.getData().get(position).getAddressId());
                addressAdapter.getData().remove(position);
                addressAdapter.notifyItemRemoved(position);
                break;
        }
    }
}
