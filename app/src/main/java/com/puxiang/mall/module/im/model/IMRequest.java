package com.puxiang.mall.module.im.model;

import android.net.Uri;
import android.util.Log;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.model.data.RxMyUserInfo;
import com.puxiang.mall.model.data.RxUserInfo;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;

import org.greenrobot.eventbus.EventBus;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class IMRequest {
    public static final String TAG = "IMRequest";

    public static void IMConnect() {
        ApiWrapper.getInstance().getRYToken()
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String token) {
                        setRongToken(token);
                    }
                });
    }

    private static void setRongToken(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.e("MainActivity", "------onTokenIncorrect----");
                refreshToken();
            }

            @Override
            public void onSuccess(String userId) {
                Log.e("MainActivity", "------onSuccess----" + userId);
                MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class, myUserInfo -> {
                    if (myUserInfo == null) {
                        return;
                    }
                    try {
                        String id = myUserInfo.getUserId();
                        String name = myUserInfo.getViewName();
                        String userImage = myUserInfo.getUserImage();
                        RongIM.getInstance().setCurrentUserInfo(new UserInfo(id, name, Uri.parse(userImage)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
                RongIM.getInstance().setMessageAttachedUserInfo(true);
                if (RongIM.getInstance() != null) {
                    /**
                     * 接收未读消息的监听器。
                     *
                     * @param listener          接收所有未读消息消息的监听器。
                     */
//                    RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new
//                            MyReceiveUnreadCountChangedListener());
                    /**
                     * 设置接收未读消息的监听器。
                     *
                     * @param listener          接收未读消息消息的监听器。
                     * @param conversationTypes 接收指定会话类型的未读消息数。
                     */
//                    RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new MyReceiveUnreadCountChangedListener(), Conversation.ConversationType.PRIVATE);
//                    RongIM.getInstance().addUnReadMessageCountChangedObserver(count ->
//                            EventBus.getDefault().postSticky(new MessageCount(count)));
                    RongIM.getInstance().addUnReadMessageCountChangedObserver(i -> {

                    }, Conversation.ConversationType.PRIVATE);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("MainActivity", "------onError----" + errorCode);
            }
        });
    }


    public static void getUserInfo(final String userId) {
        ApiWrapper.getInstance().getCommunityUserInfo(userId)
                .subscribe(new NetworkSubscriber<RxUserInfo>() {
                    @Override
                    public void onSuccess(RxUserInfo bean) {
                        setUserInfo(bean);
                    }
                });
    }

    private static void setUserInfo(RxUserInfo bean) {
        String id = bean.getUserId();
        String name = bean.getViewName();
        String userImage = bean.getUserImage();
        final UserInfo userInfo = new UserInfo(id, name, Uri.parse(userImage));
        IMTitle imTitle = new IMTitle();
        imTitle.setTitle(name);
        EventBus.getDefault().post(imTitle);
        RongIM.getInstance().refreshUserInfoCache(userInfo);
    }

    private static void refreshToken() {
        ApiWrapper.getInstance().refreshToken()
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {
                        setRongToken(bean);
                    }
                });
    }
}
