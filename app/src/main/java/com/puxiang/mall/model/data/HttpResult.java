package com.puxiang.mall.model.data;


import com.puxiang.mall.network.retrofit.RetrofitUtil;

public class HttpResult<T> {

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    private String errorMessage;
    private String token;
    private int errorCode;


    //用来模仿Data
    private T returnObject;

    public T getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(T returnObject) {
        this.returnObject = returnObject;
    }

    public void getReturnObjectClass(T t) {
        t.getClass();
    }

    public boolean isSuccess() {
        return errorCode == RetrofitUtil.APIException.OK;
    }

}
