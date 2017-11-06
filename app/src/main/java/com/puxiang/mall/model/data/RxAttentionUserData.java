package com.puxiang.mall.model.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.puxiang.mall.BR;

public class RxAttentionUserData extends BaseObservable {

    /**
     * birthday : 1998-01-06
     * sex : 1
     * viewName : 流刃若火
     * nickname : 流刃若火
     * userId : 30
     * userImage : http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201611/8729f5fe54eb42f5a817a5ef2631a89202775.jpg
     * userName : M000000020
     */

    private RxUserInfo attentionUser;
    /**
     * attentionUser : {"birthday":"1998-01-06","sex":1,"viewName":"流刃若火","nickname":"流刃若火","userId":30,"userImage":"http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201611/8729f5fe54eb42f5a817a5ef2631a89202775.jpg","userName":"M000000020"}
     * isAttented : true
     * currentUser : {"sex":null,"viewName":"哈哈","nickname":"哈哈","userId":433,"userImage":"http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201611/23e68d3f7839492b900982f559b3343b74207.jpg","userName":"M000000420"}
     * isOwner : false
     * myUserId : 433
     * attentionTime : 2016-11-21 09:21:02
     */

    private boolean isAttented;
    /**
     * sex : null
     * viewName : 哈哈
     * nickname : 哈哈
     * userId : 433
     * userImage : http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201611/23e68d3f7839492b900982f559b3343b74207.jpg
     * userName : M000000420
     */

    private RxUserInfo currentUser;
    private boolean isOwner;
    private int myUserId;
    private String attentionTime;

    public RxUserInfo getAttentionUser() {
        return attentionUser;
    }

    public void setAttentionUser(RxUserInfo attentionUser) {
        this.attentionUser = attentionUser;
    }

    @Bindable
    public boolean getIsAttented() {
        return isAttented;
    }

    public void setIsAttented(boolean isAttented) {
        this.isAttented = isAttented;
        notifyPropertyChanged(BR.isAttented);
    }

    public RxUserInfo getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(RxUserInfo currentUser) {
        this.currentUser = currentUser;
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

    public String getAttentionTime() {
        return attentionTime;
    }

    public void setAttentionTime(String attentionTime) {
        this.attentionTime = attentionTime;
    }

}
