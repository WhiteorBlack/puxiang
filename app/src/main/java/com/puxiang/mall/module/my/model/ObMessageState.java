package com.puxiang.mall.module.my.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.puxiang.mall.BR;
import com.puxiang.mall.model.data.RxMessageState;

public class ObMessageState extends BaseObservable {
    private int total;
    private int commentMe;
    private int likeMe;
    private int sysMessage;
    private int notifyMessage;
    private int myMessage;

    private boolean hasMyMessage = false;
    private boolean isNewestVersion = true;

    public void setData(RxMessageState messageState) {
        setTotal(messageState.getTotal());
        setCommentMe(messageState.getCommentMe());
        setLikeMe(messageState.getLikeMe());
        setSysMessage(messageState.getSysMessage());
        setNotifyMessage(messageState.getNotifyMessage());
    }

    @Bindable
    public boolean isNewestVersion() {
        return isNewestVersion;
    }

    public void setNewestVersion(boolean isNewestVersion) {
        if (this.isNewestVersion == isNewestVersion) return;
        this.isNewestVersion = isNewestVersion;
        notifyPropertyChanged(BR.isNewestVersion);
        notifyMessageChanged();
    }

    @Bindable
    public boolean isHasMyMessage() {
        return hasMyMessage;
    }

    private void setHasMyMessage() {
        boolean hasMgs = false;
        if (sysMessage > 0 || notifyMessage > 0 || myMessage > 0) {
            hasMgs = true;
        }
        if (hasMgs != hasMyMessage) {
            hasMyMessage = hasMgs;
            notifyMessageChanged();
            notifyPropertyChanged(BR.hasMyMessage);
        }

    }

    @Bindable
    public int getMyMessage() {
        return myMessage;
    }

    public void setMyMessage(int myMessage) {
        if (this.myMessage == myMessage) return;
        this.myMessage = myMessage;
        notifyPropertyChanged(BR.myMessage);
        setHasMyMessage();
    }

    @Bindable
    public int getNotifyMessage() {
        return notifyMessage;
    }

    public void setNotifyMessage(int notifyMessage) {
        if (this.notifyMessage == notifyMessage) return;
        this.notifyMessage = notifyMessage;
        notifyPropertyChanged(BR.notifyMessage);
        setHasMyMessage();
    }


    @Bindable
    public int getSysMessage() {
        return sysMessage;
    }

    public void setSysMessage(int sysMessage) {
        if (this.sysMessage == sysMessage) return;
        this.sysMessage = sysMessage;
        notifyPropertyChanged(BR.sysMessage);
        setHasMyMessage();
    }

    @Bindable
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        if (this.total == total) return;
        this.total = total;
        notifyPropertyChanged(BR.total);
    }

    @Bindable
    public int getCommentMe() {
        return commentMe;
    }

    public void setCommentMe(int commentMe) {
        if (this.commentMe == commentMe) return;
        this.commentMe = commentMe;
        notifyPropertyChanged(BR.commentMe);
        notifyMessageChanged();
    }

    @Bindable
    public int getLikeMe() {
        return likeMe;
    }

    public void setLikeMe(int likeMe) {
        if (this.likeMe == likeMe) return;
        this.likeMe = likeMe;
        notifyPropertyChanged(BR.likeMe);
        notifyMessageChanged();
    }

    public void clearMessage() {
        setLikeMe(0);
        setMyMessage(0);
        setCommentMe(0);
        setNotifyMessage(0);
        setSysMessage(0);
        setTotal(0);
    }

    public void notifyMessageChanged() {
        if (likeMe == 0 && commentMe == 0 && !hasMyMessage && isNewestVersion) {
            setTotal(0);
        } else {
            setTotal(1);
        }
    }


    @Override
    public String toString() {
        return "ReturnObjectBean{" +
                "total=" + total +
                ", commentMe=" + commentMe +
                ", likeMe=" + likeMe +
                '}';
    }


}
