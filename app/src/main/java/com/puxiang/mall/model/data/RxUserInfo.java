package com.puxiang.mall.model.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.puxiang.mall.BR;

public class RxUserInfo extends BaseObservable {
    private String birthday;
    private int sex;
    private String viewName;
    private String nickname;
    private String userId;
    private String userImage;
    private String userName;
    private String userRole;
    private RxShop shop;

    /**
     * member  普通会员
     * seller  商家
     * dealer 经销商
     *
     * @return
     */
    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }


    /**
     * 当userRole 为seller时返回商家信息
     *
     * @return
     */
    public RxShop getShop() {
        return shop;
    }

    public void setShop(RxShop shop) {
        this.shop = shop;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Bindable
    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
        notifyPropertyChanged(BR.userImage);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
