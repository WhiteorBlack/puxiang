package com.puxiang.mall.model.data;

public class RxAuthorizeUserInfo {

    /**
     * token : 5a537fd16d0f4c7f91f338145b63a50d_app
     * state : 1
     * userInfo : {"birthday":"","sex":null,"nickname":null,"userImage":"http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/mine_head.png","postcode":null,"city":null,"regeistTime":"2016-12-21 13:22:12","viewName":"18620295407","address":null,"email":null,"county":null,"userId":90,"province":null,"userName":"MKqpzNd000000005","realName":null,"mobile":"18620295407"}
     */
    private String token;
    private int state;
    /**
     * birthday :
     * sex : null
     * nickname : null
     * userImage : http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/mine_head.png
     * postcode : null
     * city : null
     * regeistTime : 2016-12-21 13:22:12
     * viewName : 18620295407
     * address : null
     * email : null
     * county : null
     * userId : 90
     * province : null
     * userName : MKqpzNd000000005
     * realName : null
     * mobile : 18620295407
     */

    private RxMyUserInfo userInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public RxMyUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(RxMyUserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
