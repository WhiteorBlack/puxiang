package com.puxiang.mall.module.bbs.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.module.bbs.adapter.FriendPostsAdapter;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;


public class FriendViewModel extends BaseObservable implements ViewModel {

    private final BaseBindActivity activity;
    private final FriendPostsAdapter adapter;
    private final LoadingWindow loadingWindow;
    public ObservableBoolean isInitData = new ObservableBoolean(false);
    private int pageNo = 1;

    public FriendViewModel(BaseBindActivity activity, FriendPostsAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        loadingWindow = new LoadingWindow(activity);
        loadingWindow.delayedShowWindow();
        getFriendPosts(1);
    }

    /**
     * 获取 朋友圈帖子
     *
     * @param pageNo 页码
     */
    public void getFriendPosts(final int pageNo) {
        ApiWrapper.getInstance()
                .friendPosts(pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> {
                    loadingWindow.hidWindow();
                    isInitData.set(true);
                })
                .subscribe(new NetworkSubscriber<RxList<RxPostInfo>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(RxList<RxPostInfo> bean) {
                        List<RxPostInfo> postInfoList = bean.getList();
                        dealData(postInfoList);
                        adapter.setPagingData(postInfoList, pageNo);
                    }
                });
    }

    /**
     * 将图片的信息处理成数组
     *
     * @param postInfoList
     */
    private void dealData(List<RxPostInfo> postInfoList) {
        for (RxPostInfo postInfo : postInfoList) {
            if (!TextUtils.isEmpty(postInfo.getPost().getPicUrl())) {
                String[] pics = postInfo.getPost().getPicUrl().split(",");
                postInfo.getPost().setPicUrls(pics);
            }
        }
    }
    /**
     * 加载更多
     */
    public void loadMore() {
        pageNo++;
        getFriendPosts(pageNo);
    }

    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<RxPostInfo> datas = adapter.getData();
                RxPostInfo bean = datas.get(position);
                ActivityUtil.startPostDetailActivity(activity, bean.getPost().getId());
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<RxPostInfo> datas = adapter.getData();
                RxPostInfo bean = datas.get(position);
                switch (view.getId()) {
                    case R.id.iv_like_icon:
                        if (!MyApplication.isLogin()) {
                            ActivityUtil.startLoginActivity(activity);
                        } else {
                            BbsRequest.setPostLike(bean);
                        }
                        break;

                    case R.id.tv_from:
                    case R.id.tv_from_hint:
                        ActivityUtil.startPlatePostActivity(activity, bean.getPlate().getId());
                        break;
                    case R.id.tv_user_name:
                    case R.id.tv_time:
                    case R.id.sdv_bbs_pic:
                        ActivityUtil.startPersonalActivity(activity, bean.getAccount().getUserId());
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
