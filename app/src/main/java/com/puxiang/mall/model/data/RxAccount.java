package com.puxiang.mall.model.data;

public class RxAccount {
    private String sex;
    private String viewName;
    private String nickname;
    private String userId;
    private String userImage;
    private String userName;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
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
    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "{" +
                "sex='" + sex + '\'' +
                ", viewName='" + viewName + '\'' +
                ", nickname='" + nickname + '\'' +
                ", userId='" + userId + '\'' +
                ", userImage='" + userImage + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
