package com.puxiang.mall.module.userinfo.viewmodel;

import android.util.Log;

import com.puxiang.mall.module.main.view.MainActivity;

import org.greenrobot.eventbus.EventBus;

public class ForgetRequest {


    public static boolean isStart = true;

    private static int time = 60;
    private static SmsTime smsTime;

    public static void cancelCodeTime() {
        isStart = false;
    }

    public static void smsCodeTime() {
        if (smsTime == null) {
            smsTime = new SmsTime();
        }
        MainActivity.handler.postDelayed(() -> {
            if (isStart) {
                if (time-- <= 0) {
                    time = 60;

                } else {
                    smsCodeTime();
                }
                smsTime.setTime(time);
                EventBus.getDefault().post(smsTime);
            }
        }, 1000);
    }

    public static void stopCount(){

    }

   public static class SmsTime {
        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        private int time;
    }
}
