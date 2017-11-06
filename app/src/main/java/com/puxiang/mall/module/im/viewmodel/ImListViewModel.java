package com.puxiang.mall.module.im.viewmodel;

import android.databinding.ObservableField;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.model.data.RxUnreadMessage;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;


public class ImListViewModel implements ViewModel {
    private final BaseBindActivity activity;
    public ObservableField<RxUnreadMessage> systemMessage = new ObservableField<>();
    public ObservableField<RxUnreadMessage> noticeMessage = new ObservableField<>();
    public static boolean isInitOK = false;

    public ImListViewModel(BaseBindActivity activity) {
        this.activity = activity;
        initData();
        isInitOK = true;
    }

    private void initData() {
        getUnreadMessage();
    }

    /**
     * 获取未读消息
     */
    private void getUnreadMessage() {
        ApiWrapper.getInstance().getUnreadMessage()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxUnreadMessage>>() {
                    @Override
                    public void onSuccess(List<RxUnreadMessage> unreadMessageList) {
                        setUnreadMessage(unreadMessageList);
                    }
                });
    }

    /**
     * 处理未读消息
     *
     * @param unreadMessageList 未读消息集合
     */
    private void setUnreadMessage(List<RxUnreadMessage> unreadMessageList) {
        MyApplication.mCache.put(CacheKey.UNREAD_MESSAGE, unreadMessageList);
        for (RxUnreadMessage unreadMessage : unreadMessageList) {
            switch (unreadMessage.getMessageType()) {
                case RxUnreadMessage.MESSAGE_TYPE_SYSTEM:
                    systemMessage.set(unreadMessage);
                    break;
                case RxUnreadMessage.MESSAGE_TYPE_NOTICE:
                    noticeMessage.set(unreadMessage);
                    break;
            }
        }
    }


    @Override
    public void destroy() {
        isInitOK = false;
    }
}
