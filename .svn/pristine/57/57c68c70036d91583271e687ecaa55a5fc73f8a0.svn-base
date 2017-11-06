package com.puxiang.mall.model.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.puxiang.mall.BR;


public class RxUnreadMessage extends BaseObservable {

    public static final int MESSAGE_TYPE_SYSTEM = 1;
    public static final int MESSAGE_TYPE_NOTICE = 2;

    private RxNewestMessage newestMessage;
    private int unreadCount;
    private int messageType;

    public RxNewestMessage getNewestMessage() {
        return newestMessage;
    }

    public void setNewestMessage(RxNewestMessage newestMessage) {
        this.newestMessage = newestMessage;
    }

    @Bindable
    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
        notifyPropertyChanged(BR.unreadCount);
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

}
