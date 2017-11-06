package com.puxiang.mall.module.personal.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxPlate;
import com.puxiang.mall.module.personal.adapter.UserPlatesAdapter;
import com.puxiang.mall.module.personal.view.UserPlatesActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.trello.rxlifecycle2.android.ActivityEvent;


/**
 * 关注的圈子
 */

public class UserPlatesViewModel implements ViewModel {

    private final UserPlatesAdapter adapter;
    private final UserPlatesActivity activity;
    private final LoadingWindow loadingWindow;
    private boolean isInitData = false;

    private int pageNo = 1;
    private String userId;

    public UserPlatesViewModel(UserPlatesActivity activity, UserPlatesAdapter adapter) {
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
     * 获取用户关注的圈子
     *
     * @param pageNo 页码
     */
    private void getData(final int pageNo) {
        ApiWrapper.getInstance()
                .userPlates(userId, pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> {
                    if (isInitData) return;
                    isInitData = true;
                    activity.setIsInitData();
                    loadingWindow.hidWindow();
                })
                .subscribe(new NetworkSubscriber<RxList<RxPlate>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(RxList<RxPlate> bean) {
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

    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int
                    i) {
                RxPlate bean = adapter.getData().get(i);
                switch (view.getId()) {
                    case R.id.tv_attention:
                        setAttentPlate(bean);
                        break;
                }
            }

            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                super.onItemClick(baseQuickAdapter, view, position);
                RxPlate bean = adapter.getData().get(position);
                ActivityUtil.startPlatePostActivity(activity, bean.getId());
            }
        };
    }

    /**
     * 加入圈子
     *
     * @param bean
     */
    private void setAttentPlate(RxPlate bean) {
        if (!MyApplication.isLogin()) {
            ActivityUtil.startLoginActivity(activity);
            return;
        }
        String id = bean.getId();
        boolean isAttented = !bean.getIsAttented();
        bean.setIsAttented(isAttented);
        ApiWrapper.getInstance().joinPlate(id, isAttented)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {

                    }
                });
    }

    @Override
    public void destroy() {
        if (loadingWindow != null) loadingWindow.dismiss();
    }
}
