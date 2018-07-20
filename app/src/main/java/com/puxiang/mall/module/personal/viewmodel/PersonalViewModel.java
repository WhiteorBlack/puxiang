package com.puxiang.mall.module.personal.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.puxiang.mall.BR;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxAds;
import com.puxiang.mall.model.data.RxMyUserInfo;
import com.puxiang.mall.model.data.RxUserCommunity;
import com.puxiang.mall.module.personal.view.PersonalActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class PersonalViewModel extends BaseObservable implements ViewModel {
    private final PersonalActivity activity;
    private final LoadingWindow loadingWindow;
    public ObservableField<RxUserCommunity> userBean = new ObservableField<>();
    public ObservableField<String> bgUrl = new ObservableField<>();
    public ObservableBoolean isInitData = new ObservableBoolean(false);
    public ObservableBoolean isAttention = new ObservableBoolean(false);
    public String userId;

    public PersonalViewModel(PersonalActivity activity) {
        this.activity = activity;
        loadingWindow = new LoadingWindow(activity);
        EventBus.getDefault().post(Event.KILL_PERSONAL);
        EventBus.getDefault().register(this);
        initData();
        loadingWindow.delayedShowWindow();
        getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RxMyUserInfo userInfo) {
        if (userId.equals(MyApplication.USER_ID)) {
            String userImage = userInfo.getUserImage();
            userBean.get().getAccount().setUserImage(userImage);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.RELOAD) {
            getData();
        } else if (i == Event.KILL_PERSONAL) {
            activity.finish();
        }
    }

    private void initData() {
        userId = activity.getIntent().getStringExtra(Config.USER_ID);
        MyApplication.mCache.getAsString(CacheKey.PERSONAL_BG_URL, url -> bgUrl.set(url));
        activity.setPageTag("userId", userId);
    }

    private void getData() {
        getUserCommunity();
        getBgUrl();
    }

    /**
     * 个人中心背景图
     */
    private void getBgUrl() {
        ApiWrapper.getInstance()
                .getAds(URLs.CENTRE_BACKGROUND)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxAds>>() {
                    @Override
                    public void onSuccess(List<RxAds> bean) {
                        if (bean.size() > 0) {
                            String url = bean.get(0).getPicUrl();
                            if (!StringUtil.isEmpty(url)) {
                                MyApplication.mCache.put(CacheKey.PERSONAL_BG_URL, bgUrl);
                                bgUrl.set(url);
                            }
                        }
                    }
                });
    }

    /**
     * 获取个人数据
     */
    private void getUserCommunity() {
        if (StringUtil.isEmpty(userId)) {
            return;
        }
        ApiWrapper.getInstance()
                .getUserCommunity(userId)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> {
                    loadingWindow.hidWindow();
                    isInitData.set(true);
                })
                .subscribe(new NetworkSubscriber<RxUserCommunity>() {
                    @Override
                    public void onSuccess(RxUserCommunity bean) {
                        setUserCommunity(bean);
                    }
                });
    }

    private void setUserCommunity(RxUserCommunity bean) {
        if (bean != null) {
            MyApplication.mCache.put("userId=" + userId, bean.getAccount());
            userBean.set(bean);
            isAttention.set(bean.getIsAttendFriend() == 1);
        }
    }

    /**
     * 关注用户
     */
    public void setAttentUser() {
        if (!MyApplication.isLogin()) {
            ActivityUtil.startLoginActivity(activity);
            return;
        }

        RxUserCommunity rxUserCommunity = userBean.get();
        if (rxUserCommunity == null) {
            ToastUtil.toast(R.string.no_network);
            return;
        }


        ApiWrapper.getInstance()
                .attentUser(userId, !isAttention.get())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {
                        if (TextUtils.equals(bean, "000")) {
                            isAttention.set(!isAttention.get());
                            rxUserCommunity.setIsAttendFriend(isAttention.get() ? 1 : 0);
                        }
                    }
                });
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
        loadingWindow.dismiss();
    }
}
