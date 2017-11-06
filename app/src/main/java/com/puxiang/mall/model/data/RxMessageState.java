package com.puxiang.mall.model.data;

public class RxMessageState {
    private int total;
    private int commentMe;
    private int likeMe;
    private int notifyMessage;
    private int sysMessage;

    public int getNotifyMessage() {
        return notifyMessage;
    }

    public void setNotifyMessage(int notifyMessage) {
        this.notifyMessage = notifyMessage;
    }

    public int getSysMessage() {
        return sysMessage;
    }

    public void setSysMessage(int sysMessage) {
        this.sysMessage = sysMessage;
    }


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCommentMe() {
        return commentMe;
    }

    public void setCommentMe(int commentMe) {
        this.commentMe = commentMe;
    }

    public int getLikeMe() {
        return likeMe;
    }

    public void setLikeMe(int likeMe) {
        this.likeMe = likeMe;
    }

    @Override
    public String toString() {
        return "RxMessageState{" +
                "total=" + total +
                ", commentMe=" + commentMe +
                ", likeMe=" + likeMe +
                ", notifyMessage=" + notifyMessage +
                ", sysMessage=" + sysMessage +
                '}';
    }
}
