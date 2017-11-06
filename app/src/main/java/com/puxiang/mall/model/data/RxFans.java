package com.puxiang.mall.model.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.puxiang.mall.BR;

public class RxFans extends BaseObservable {
    /**
     * isAttented : true
     * isOwner : false
     * myUserId : 433
     * fansUser : {"birthday":"1998-01-06","sex":1,"viewName":"流刃若火","nickname":"流刃若火","userId":30,"userImage":"http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201611/8729f5fe54eb42f5a817a5ef2631a89202775.jpg","userName":"M000000020"}
     * attentionTime : 2016-11-30 13:43:24
     */

    private boolean isAttented;
    private boolean isOwner;
    private int myUserId;
    /**
     * birthday : 1998-01-06
     * sex : 1
     * viewName : 流刃若火
     * nickname : 流刃若火
     * userId : 30
     * userImage : http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201611/8729f5fe54eb42f5a817a5ef2631a89202775.jpg
     * userName : M000000020
     */

    private RxUserInfo fansUser;
    private String attentionTime;

    @Bindable
    public boolean getIsAttented() {
        return isAttented;
    }

    public void setIsAttented(boolean isAttented) {
        this.isAttented = isAttented;
        notifyPropertyChanged(BR.isAttented);
    }

    public boolean getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    public int getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(int myUserId) {
        this.myUserId = myUserId;
    }

    public RxUserInfo getFansUser() {
        return fansUser;
    }

    public void setFansUser(RxUserInfo fansUser) {
        this.fansUser = fansUser;
    }

    public String getAttentionTime() {
        return attentionTime;
    }

    public void setAttentionTime(String attentionTime) {
        this.attentionTime = attentionTime;
    }

}
