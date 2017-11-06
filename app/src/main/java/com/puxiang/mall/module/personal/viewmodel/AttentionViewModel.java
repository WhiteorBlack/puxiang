package com.puxiang.mall.module.personal.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.model.data.RxAttentionUserData;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.module.personal.adapter.AttentionUsersAdapter;
import com.puxiang.mall.module.personal.view.AttentionUsersActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.StringUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class AttentionViewModel implements ViewModel {

    private final AttentionUsersAdapter adapter;
    private final AttentionUsersActivity activity;
    private final LoadingWindow loadingWindow;
    private boolean isInitData = false;

    private int pageNo = 1;
    private String userId;

    public AttentionViewModel(AttentionUsersActivity activity, AttentionUsersAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        loadingWindow = new LoadingWindow(activity);
        loadingWindow.delayedShowWindow();
        initData();
        getData(1);
    }


    private void initData() {
        userId = activity.getIntent().getStringExtra(Config.USER_ID);
    }

    /**
     * 获取已关注的人
     *
     * @param pageNo 页码
     */
    private void getData(final int pageNo) {
        ApiWrapper.getInstance()
                .getAttentionUsers(userId, pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> {
                    if (isInitData) return;
                    isInitData = true;
                    activity.setIsInitData();
                    loadingWindow.hidWindow();
                }).subscribe(new NetworkSubscriber<RxList<RxAttentionUserData>>() {
            @Override
            public void onFail(RetrofitUtil.APIException e) {
                super.onFail(e);
                adapter.loadMoreFail();
            }

            @Override
            public void onSuccess(RxList<RxAttentionUserData> bean) {
                adapter.setPagingData(bean.getList(), pageNo);
            }
        });
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        pageNo++;
        getData(pageNo);
    }

    /**
     * 点击响应
     */
    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                RxAttentionUserData bean = adapter.getData().get(i);
                ActivityUtil.startPersonalActivity(activity, bean.getAttentionUser().getUserId());
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int
                    position) {
                super.onItemChildClick(baseQuickAdapter, view, position);
                RxAttentionUserData bean = adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.tv_attention:
                        setAttentUser(bean);
                        //  baseQuickAdapter.notifyItemChanged(position, null);
                        break;
                }
            }
        };
    }

    /**
     * 关注用户
     */
    private void setAttentUser(RxAttentionUserData bean) {
        if (StringUtil.isEmpty(MyApplication.TOKEN)) {
            ActivityUtil.startLoginActivity(activity);
            return;
        }

        String id = bean.getAttentionUser().getUserId();
        boolean isAttented = !bean.getIsAttented();
        bean.setIsAttented(isAttented);
        ApiWrapper.getInstance()
                .attentUser(id, isAttented)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {
                    }
                });
    }

    @Override
    public void destroy() {
        loadingWindow.dismiss();
    }
}
