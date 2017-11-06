package com.puxiang.mall.module.im.model;

import org.greenrobot.eventbus.EventBus;

import io.rong.imkit.RongIM;

/**
 * 接收未读消息的监听器。
 */
public class MyReceiveUnreadCountChangedListener implements RongIM.OnReceiveUnreadCountChangedListener {

    private String TAG = "MyReceiveUnreadCountChangedListener";

    /**
     * @param count 未读消息数。
     */
    @Override
    public void onMessageIncreased(int count) {
        if (count > 0) {
            EventBus.getDefault().postSticky(new MessageCount(count));
        }
    }
}
