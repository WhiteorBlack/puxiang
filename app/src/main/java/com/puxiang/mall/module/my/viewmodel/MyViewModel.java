package com.puxiang.mall.module.my.viewmodel;

import android.databinding.ObservableField;
import android.support.v4.app.FragmentActivity;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.model.data.AppVersionJSON;
import com.puxiang.mall.model.data.RxAds;
import com.puxiang.mall.model.data.RxMessageState;
import com.puxiang.mall.model.data.RxMyUserInfo;
import com.puxiang.mall.module.im.model.IMRequest;
import com.puxiang.mall.module.im.model.IMUserInfoProvider;
import com.puxiang.mall.module.im.model.MessageCount;
import com.puxiang.mall.module.im.viewmodel.ImListViewModel;
import com.puxiang.mall.module.my.view.MyFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.StringUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.rong.imkit.RongIM;

public class MyViewModel implements ViewModel {


    private MyFragment fragment;
    private FragmentActivity activity;
    public ObservableField<RxMyUserInfo> userBean = new ObservableField<>();
    public ObservableField<String> bgUrl = new ObservableField<>();
    public String introduce = "";
    public String versionName = "";
    private boolean isInitIM = false;

    public MyViewModel(MyFragment fragment) {
        this.fragment = fragment;
        EventBus.getDefault().register(this);
        activity = fragment.getActivity();
        init();
    }

    private void init() {
        getCacheData();
        //TODO: 即使通讯连接
        if (!StringUtil.isEmpty(MyApplication.TOKEN)) {
            // 设置接收 push 消息的监听器。
            if (!isInitIM) {
                RongIM.setUserInfoProvider(IMUserInfoProvider.getInstance(), true);
                isInitIM = true;
            }
            IMRequest.IMConnect();
            MyApplication.isLoginOB.set(true);
        }
    }

    /**
     * 获取本地缓存数据
     */
    private void getCacheData() {
        //TODO: 获取用户信息
        MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class
                , myUserInfo -> userBean.set(myUserInfo));
        //TODO: 获取App版本信息
        MyApplication.mCache.getAsJSONBean(CacheKey.VERSION, AppVersionJSON.class, appVersionJSON -> {
            AppVersionJSON.ReturnObjectBean bean = appVersionJSON.getReturnObject();
            int versionCode = bean.getVersionCode();
            boolean isNewestVersion = ActivityUtil.getVersionCode(activity) >= versionCode;
            MyApplication.messageState.setNewestVersion(isNewestVersion);
            introduce = bean.getIntroduce();
            versionName = bean.getVersionName();
        });

        //TODO: 获取背景图Url
        MyApplication.mCache.getAsString(CacheKey.MY_BG_URL, bgUrlStr -> bgUrl.set(bgUrlStr));
    }

    //接收消息推送
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(MessageCount messageCount) {
        if (messageCount != null && !ImListViewModel.isInitOK) {
            int count = messageCount.getCount();
            MyApplication.messageState.setMyMessage(count);
            MyApplication.messageState.notifyMessageChanged();
        }
    }

    //接收更新请求
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(RxMyUserInfo myUserInfo) {
        if (myUserInfo != null) {
            userBean.set(myUserInfo);
            if (!isInitIM) {
                RongIM.setUserInfoProvider(IMUserInfoProvider.getInstance(), true);
                isInitIM = true;
            }
        }
    }

    public void getBgUrl() {
        ApiWrapper.getInstance()
                .getAds(URLs.MY_BACKGROUND)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxAds>>() {
                    @Override
                    public void onSuccess(List<RxAds> bean) {
                        String bgUrlStr = bean.get(0).getPicUrl();
                        if (!StringUtil.isEmpty(bgUrlStr)) {
                            MyApplication.mCache.put(CacheKey.MY_BG_URL, bgUrlStr);
                            bgUrl.set(bgUrlStr);
                        }
                    }
                });
    }

    public void getMessageState() {
        ApiWrapper.getInstance()
                .getMessageState()
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxMessageState>() {
                    @Override
                    public void onSuccess(RxMessageState bean) {
                        setMessageState(bean);
                    }
                });

    }


    private void setMessageState(RxMessageState message) {
        MyApplication.messageState.setData(message);
    }

    /**
     * 查阅消息
     *
     * @param messageCode 消息类型
     */
    public void setMessageReadTime(String messageCode) {
        ApiWrapper.getInstance()
                .setMessageReadTime(messageCode)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {

                    }
                });
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

}
