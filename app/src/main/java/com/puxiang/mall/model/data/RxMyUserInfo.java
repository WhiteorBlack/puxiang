package com.puxiang.mall.model.data;

import java.util.List;

public class RxMyUserInfo {
    private String birthday;
    private int sex;
    private String nickname;
    private String userImage;
    private String postcode;
    private String city;
    private String regeistTime;
    private String viewName;
    private String address;
    private String email;
    private String county;
    private String userId;
    private String province;
    private String userName;
    private String realName;
    private String mobile;
    private List<RxRoles> roles;
    private RxShop shop;

    public List<RxRoles> getRoles() {
        return roles;
    }

    public void setRoles(List<RxRoles> roles) {
        this.roles = roles;
    }

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegeistTime() {
        return regeistTime;
    }

    public void setRegeistTime(String regeistTime) {
        this.regeistTime = regeistTime;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "{\"birthday\":\"" + getBirthday()
                + "\",\"sex\":" + getSex()
                + ",\"nickname\":\"" + getNickname()
                + "\",\"userImage\":\"" + getUserImage()
                + "\",\"postcode\":" + getPostcode()
                + ",\"city\":\"" + getCity()
                + "\",\"regeistTime\":\"" + getRegeistTime()
                + "\",\"viewName\":\"" + getViewName()
                + "\",\"address\":" + getAddress()
                + ",\"email\":\"" + getEmail()
                + "\",\"county\":\"" + getCounty()
                + "\",\"userId\":" + getUserId()
                + ",\"province\":\"" + getProvince()
                + "\",\"userName\":\"" + getUserName()
                + "\",\"realName\":\"" + getRealName()
                + "\",\"mobile\":\"" + getMobile() + "\"}";
    }
}
