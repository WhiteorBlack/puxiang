package com.puxiang.mall.module.personal.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.model.data.RxFans;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.module.personal.adapter.FansAdapter;
import com.puxiang.mall.module.personal.view.FansActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.StringUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class FansViewModel implements ViewModel {

    private final FansAdapter adapter;
    private final FansActivity activity;
    private final LoadingWindow loadingWindow;
    private boolean isInitData = false;

    private int pageNo = 1;
    private String userId;

    public FansViewModel(FansActivity activity, FansAdapter adapter) {
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
     * 获取用户粉丝
     *
     * @param pageNo 页码
     */
    private void getData(final int pageNo) {
        ApiWrapper.getInstance()
                .userFans(userId, pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> {
                    if (isInitData) return;
                    isInitData = true;
                    activity.setIsInitData();
                    loadingWindow.hidWindow();
                })
                .subscribe(new NetworkSubscriber<RxList<RxFans>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(RxList<RxFans> bean) {
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
                RxFans bean = adapter.getData().get(i);
                ActivityUtil.startPersonalActivity(activity, bean.getFansUser().getUserId());
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int
                    position) {
                super.onItemChildClick(baseQuickAdapter, view, position);
                RxFans bean = adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.tv_attention:
                        setAttentUser(bean);
//                        baseQuickAdapter.notifyItemChanged(position, null);
                        break;
                }
            }
        };
    }

    /**
     * 关注用户
     *
     * @param bean
     */
    private void setAttentUser(RxFans bean) {
        if (StringUtil.isEmpty(MyApplication.TOKEN)) {
            ActivityUtil.startLoginActivity(activity);
            return;
        }
        String id = bean.getFansUser().getUserId();
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
