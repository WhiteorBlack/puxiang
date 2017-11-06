package com.puxiang.mall.module.integral.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxIntegral;
import com.puxiang.mall.model.data.RxIntegralAccount;
import com.puxiang.mall.model.data.RxTasks;
import com.puxiang.mall.module.integral.adapter.IntegralAdapter;
import com.puxiang.mall.module.integral.viewmodel.model.IntegralData;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.WebUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class IntegralViewModel extends BaseObservable implements ViewModel {

    private final BaseBindActivity activity;
    private final IntegralAdapter adapter;
    private final LoadingWindow loadingWindow;
    public ObservableField<String> total = new ObservableField<>("0");
    public ObservableBoolean isInitData = new ObservableBoolean(false);

    public IntegralViewModel(BaseBindActivity activity, IntegralAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        loadingWindow = new LoadingWindow(activity);
        loadingWindow.delayedShowWindow();
    }

    /**
     * 获取积分任务列表
     */
    public void getData() {
        ApiWrapper.getInstance()
                .getIntegralTask()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> {
                    loadingWindow.hidWindow();
                    isInitData.set(true);
                })
                .subscribe(new NetworkSubscriber<List<RxIntegral>>() {
                    @Override
                    public void onSuccess(List<RxIntegral> bean) {
                        adapter.setData(bean);
                    }
                });


        getIntegralAccount();
    }

    /**
     * 获取账户积分
     */
    private void getIntegralAccount() {
        if (!StringUtil.isEmpty(MyApplication.TOKEN)) {
            ApiWrapper.getInstance()
                    .getIntegralAccount()
                    .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new NetworkSubscriber<RxIntegralAccount>() {
                        @Override
                        public void onSuccess(RxIntegralAccount bean) {
                            total.set(String.valueOf(bean.getBalance()));
                        }
                    });
        }
    }

    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.iv:
                        List<IntegralData> list = adapter.getData();
                        RxTasks tasks = list.get(i).t;
                        go(tasks.getUrl());
                        break;
                }
            }
        };
    }

    /**
     * 跳转响应
     *
     * @param url Link地址
     */
    private void go(String url) {
        if (StringUtil.isEmpty(url)) return;
        if (url.contains(URLs.HTML_LOGIN_KEY) || !MyApplication.isLogin()) {
            ActivityUtil.startLoginActivity(activity);
        } else if (url.contains(URLs.HTML_POST_KEY) || url.contains(URLs.HTML_VIDEO_KEY)) {
            String postId = StringUtil.getUrlValue(url, "postId=");
            EventBus.getDefault().post(Event.KILL_POST);
            ActivityUtil.startPostDetailActivity(activity, postId);
        } else if (url.contains(URLs.HTML_MORE_COMMENT_KEY)) {
            String commentId = StringUtil.getUrlValue(url, "commentId=");
            ActivityUtil.startCommentActivity(activity, commentId);
        } else if (url.contains(URLs.HTML_PLATE_KEY)) {
            String plateId = StringUtil.getUrlValue(url, "plateId=");
            ActivityUtil.startPlatePostActivity(activity, plateId);
        } else if (url.contains(URLs.HTML_INDEX_KEY)) {
            ActivityUtil.startMainActivity(activity);
        } else if (url.contains(URLs.HTML_REGISTER_KEY)) {
            ActivityUtil.startRegisterActivity(activity);
        } else if (url.contains(URLs.HTML_HOT_PLATES_KEY)) {
            EventBus.getDefault().post(Event.GO_PLATES);
            ActivityUtil.startMainActivity(activity);
        } else if (url.contains(URLs.HTML_USER_INFO)) {
            ActivityUtil.startInfoActivity(activity);
        } else if (url.contains(URLs.HTML_USER_CENTER_KEY)) {
            String userId = StringUtil.getUrlValue(url, "userId=");
            ActivityUtil.startPersonalActivity(activity, userId);
        } else {
            WebUtil.jumpWeb(url, activity);
        }
    }

    @Override
    public void destroy() {
        loadingWindow.dismiss();
    }
}
