package com.puxiang.mall.receiver.rong;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import io.rong.push.RongPushClient;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

public class ImNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {

        String targetId = pushNotificationMessage.getTargetId();
        RongPushClient.ConversationType type = pushNotificationMessage.getConversationType();
        String targetName = pushNotificationMessage.getTargetUserName();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri.Builder builder = Uri.parse("rong://" + context.getPackageName()).buildUpon();
        builder.appendPath("conversation").appendPath(type.getName())
                .appendQueryParameter("targetId", targetId)
                .appendQueryParameter("title", targetName);
        Uri uri = builder.build();
        intent.setData(uri);
        context.startActivity(intent);
        return true;
    }
}
