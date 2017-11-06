package com.puxiang.mall.module.from.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.module.from.adapter.FromAdapter;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.trello.rxlifecycle2.android.ActivityEvent;

public class FromViewModel extends BaseObservable implements ViewModel {

    private final BaseBindActivity activity;
    private final FromAdapter adapter;
    private final LoadingWindow loadingWindow;
    private final String typeId;
    public ObservableBoolean isInitData = new ObservableBoolean(false);
    private int pageNo = 1;

    public FromViewModel(BaseBindActivity activity, FromAdapter adapter) {
        typeId = activity.getIntent().getStringExtra("typeId");
        activity.setPageTag("typeId", typeId);
        this.activity = activity;
        this.adapter = adapter;

        loadingWindow = new LoadingWindow(activity);
        loadingWindow.delayedShowWindow();
        getData(1);
    }

    public void getData(final int pageNo) {
        ApiWrapper.getInstance().officialPosts(typeId, pageNo)
                .doOnTerminate(() -> {
                    loadingWindow.hidWindow();
                    isInitData.set(true);
                })
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxList<RxPostInfo>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(RxList<RxPostInfo> bean) {
                        adapter.setPagingData(bean.getList(), pageNo);
                    }
                });
    }

    public void loadMore() {
        pageNo++;
        getData(pageNo);
    }

    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                try {
                    RxPostInfo bean = adapter.getData().get(i);
                    String postId = bean.getPost().getId();
                    ActivityUtil.startPostDetailActivity(activity, postId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                RxPostInfo bean;
                try {
                    bean = adapter.getData().get(position);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                switch (view.getId()) {
                    //类型来源
                    case R.id.iv_from_icon:
                    case R.id.tv_from:
                        if (bean != null) {
                            int postType = bean.getPost().getPostType();
                            switch (postType) {
                                case RxPostInfo.POST_PLATE:
                                    String plateId = bean.getPlate().getId();
                                    ActivityUtil.startPlatePostActivity(activity, plateId);
                                    break;
                                case RxPostInfo.POST_VIDEO:
                                case RxPostInfo.POST_OFFICIAL:
                                    int typeId = bean.getPost().getPostTypeId();
                                    String typeName = bean.getPost().getPostTypeName();
                                    ActivityUtil.startFromActivity(activity, typeId + "", typeName);
                                    break;
                                case RxPostInfo.POST_LINK:
                                    break;
                            }
                        }
                        break;
                    //用户信息
                    case R.id.tv_user_name:
                    case R.id.sdv_user_pic:
                        if (bean != null) {
                            String userId = bean.getAccount().getUserId();
                            ActivityUtil.startPersonalActivity(activity, userId);
                        }
                        break;
                }
            }
        };
    }

    @Override
    public void destroy() {
        loadingWindow.dismiss();
    }
}
