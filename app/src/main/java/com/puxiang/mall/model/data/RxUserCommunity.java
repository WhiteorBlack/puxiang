package com.puxiang.mall.model.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.puxiang.mall.BR;

public class RxUserCommunity extends BaseObservable {

    /**
     * id : 433
     * account : {"birthday":"","sex":null,"viewName":"哈哈","nickname":"哈哈","userId":433,"userImage":"http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201611/23e68d3f7839492b900982f559b3343b74207.jpg","userName":"M000000420"}
     * isAttendFriend : 0
     * isOwner : true
     * fansNum : 1
     * attentNum : 14
     * plateNum : 1
     */

    private int id;
    /**
     * birthday :
     * sex : null
     * viewName : 哈哈
     * nickname : 哈哈
     * userId : 433
     * userImage : http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201611/23e68d3f7839492b900982f559b3343b74207.jpg
     * userName : M000000420
     */

    private RxUserInfo account;
    private int isAttendFriend;
    private boolean isOwner;
    private String fansNum;
    private String attentNum;
    private String plateNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RxUserInfo getAccount() {
        return account;
    }

    public void setAccount(RxUserInfo account) {
        this.account = account;
    }

    @Bindable
    public int getIsAttendFriend() {
        return isAttendFriend;
    }

    public void setIsAttendFriend(int isAttendFriend) {
        this.isAttendFriend = isAttendFriend;
        notifyPropertyChanged(BR.isAttendFriend);
    }

    public boolean getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    public String getFansNum() {
        return fansNum;
    }

    public void setFansNum(String fansNum) {
        this.fansNum = fansNum;
    }

    public String getAttentNum() {
        return attentNum;
    }

    public void setAttentNum(String attentNum) {
        this.attentNum = attentNum;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

}
