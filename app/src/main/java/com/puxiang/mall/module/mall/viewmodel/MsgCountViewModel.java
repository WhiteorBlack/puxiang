package com.puxiang.mall.module.mall.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableInt;

import com.orhanobut.logger.Logger;
import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxUnreadMessage;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by zhaoyong bai on 2017/10/18.
 * toolbar 上面消息组件viewmodel
 */

public class MsgCountViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private BaseBindFragment fragment;
    private ObservableInt msgCount = new ObservableInt(0);

    public MsgCountViewModel(BaseBindActivity activity) {
        this.activity = activity;
        EventBus.getDefault().register(this);
    }

    public MsgCountViewModel(BaseBindFragment fragment) {
        this.fragment = fragment;
        EventBus.getDefault().register(this);
    }

    @Bindable
    public int getMsgCount() {
        return msgCount.get();
    }

    public void setMsgCount(int msgCount) {
        this.msgCount.set(msgCount);
        notifyPropertyChanged(BR.msgCount);
    }

    public void getMsgCountData() {
        if (!MyApplication.isLogin()) {
            return;
        }
        ApiWrapper.getInstance()
                .getUnreadMessage()
                .compose(activity == null ? fragment.bindUntilEvent(FragmentEvent.DESTROY) : activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxUnreadMessage>>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Logger.e("getMsgCount");
                    }

                    @Override
                    public void onSuccess(List<RxUnreadMessage> data) {
                        if (data.size() > 0) {
                            int count = 0;
                            for (int i = 0; i < data.size(); i++) {
                                count += data.get(i).getUnreadCount();
                            }
                            msgCount.set(count);
                            notifyPropertyChanged(BR.msgCount);
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer event) {
        switch (event) {
            case Event.LOGIN_REFRESH:
                getMsgCountData();
                break;
            case Event.LOGOUT:
                msgCount.set(0);
                notifyPropertyChanged(BR.msgCount);
                break;
        }
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
