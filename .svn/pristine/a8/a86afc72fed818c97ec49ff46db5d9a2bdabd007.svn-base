package com.puxiang.mall.module.im.model;

import android.net.Uri;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.model.data.RxUserInfo;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class IMUserInfoProvider implements RongIM.UserInfoProvider {

    private static final String TAG = "IMUserInfoProvider";
    public static final String NULL = "IMUserInfoProvider_NULL";

    private IMUserInfoProvider() {
    }

    private static IMUserInfoProvider imUserInfoProvider;

    public static IMUserInfoProvider getInstance() {
        if (imUserInfoProvider == null) {
            imUserInfoProvider = new IMUserInfoProvider();

        }
        return imUserInfoProvider;
    }

    @Override
    public UserInfo getUserInfo(String userId) {
        UserInfo userInfo = null;
        try {
            RxUserInfo rxUserInfo = MyApplication.mCache.getAsJSONBean("userId=" + userId, RxUserInfo.class);
            if (rxUserInfo != null) {
                String id = rxUserInfo.getUserId();
                String name = rxUserInfo.getViewName();
                String userImage = rxUserInfo.getUserImage();
                userInfo = new UserInfo(id, name, Uri.parse(userImage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        IMRequest.getUserInfo(userId);
        return userInfo;
    }
}
