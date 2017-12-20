package com.puxiang.mall.module.userinfo.viewmodel;

import android.databinding.ObservableField;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxMyUserInfo;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.utils.ACache;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;
import com.puxiang.mall.widget.dialog.DefaultDialog;
import com.puxiang.mall.widget.dialog.OnDialogExecuteListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import io.rong.imkit.RongIM;

public class SettingsViewModel implements ViewModel {
    private final BaseBindActivity activity;
    private DefaultDialog dialog;
    public ObservableField<RxMyUserInfo> userBean = new ObservableField<>();
    public SettingsViewModel(BaseBindActivity activity) {
        EventBus.getDefault().register(this);
        this.activity = activity;
        getUserInfo();
    }

    private void getUserInfo() {
        //TODO: 获取用户信息
        MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class
                , new ACache.CacheResultListener<RxMyUserInfo>() {
                    @Override
                    public void onResult(RxMyUserInfo myUserInfo) {
                        userBean.set(myUserInfo);
                    }
                });
    }

    /**
     * 退出登录弹窗
     */
    public void logOutDialog() {
        if (dialog == null) {
            dialog = new DefaultDialog(activity, "是否退出登录？", new OnDialogExecuteListener() {
                @Override
                public void execute() {
                    logOut();
                    exit();
                }

                @Override
                public void cancel() {

                }
            });
        }
        dialog.show();
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * 退出登录
     */
    public static void logOut() {
        MyApplication.mCache.remove(CacheKey.USER_ID);
        MyApplication.mCache.remove(CacheKey.INFO);
        MyApplication.mCache.remove(CacheKey.TOKEN);
        MyApplication.mCache.remove(CacheKey.RONG_TOKEN);
        MyApplication.mCache.remove(CacheKey.ISSELLER);
        MyApplication.mCache.remove(CacheKey.ISDEALER);
        MyApplication.RONG_TOKEN="";
        MyApplication.TOKEN = "";
        MyApplication.USER_ID = "";
        System.gc();
        System.gc();
        MobclickAgent.onProfileSignOff();
        WebUtil.removeCookie();
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().logout();
        }
        EventBus.getDefault().post(Event.RELOAD);
        MyApplication.isLoginOB.set(false);
        MyApplication.messageState.setIsSeller(false);
        MyApplication.messageState.setIsDealer(false);
        MyApplication.messageState.setIsMember(false);
    }

    //接收关闭请求
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.FINISH) {
            activity.finish();
        }
    }

    /**
     * 退出登录
     */
    public void exit() {
        UMShareAPI mShareAPI = UMShareAPI.get(activity);
        mShareAPI.deleteOauth(activity, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
            }
        });
        ToastUtil.toast("已退出登录");
        activity.onBackPressed();
    }
}
