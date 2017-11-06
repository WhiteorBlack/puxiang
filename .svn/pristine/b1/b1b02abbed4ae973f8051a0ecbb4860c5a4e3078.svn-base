package com.puxiang.mall.module.personal.viewmodel;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.module.bbs.viewmodel.BbsRequest;
import com.puxiang.mall.module.personal.adapter.PublishPostAdapter;
import com.puxiang.mall.module.personal.view.PersonalPublishFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.widget.dialog.DefaultDialog;
import com.puxiang.mall.widget.dialog.OnDialogExecuteListener;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
/**
 * 我的发帖
 */

public class PublishViewModel implements ViewModel {

    private final PublishPostAdapter adapter;
    private final PersonalPublishFragment fragment;
    private final Activity activity;
    public String keyword = "";
    private boolean isInitData = false;

    private int pageNo = 1;
    private String userId;
    private String postId;

    public PublishViewModel(PersonalPublishFragment fragment, PublishPostAdapter adapter) {
        EventBus.getDefault().register(this);
        this.fragment = fragment;
        activity = fragment.getActivity();
        this.adapter = adapter;
        initData();
        getData(1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.RELOAD) {
            getData(1);
        }
    }

    private void initData() {
        userId = fragment.getArguments().getString(Config.USER_ID);
        if (userId != null) {
            adapter.setOwner(userId.equals(MyApplication.USER_ID));
        }
    }

    /**
     * 用户发布的帖子
     *
     * @param pageNo 页码
     */
    private void getData(final int pageNo) {
        ApiWrapper.getInstance()
                .myPosts(userId, pageNo)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .doOnTerminate(() -> {
                    if (isInitData) return;
                    isInitData = true;
                    fragment.setIsInitData();
                })
                .subscribe(new NetworkSubscriber<RxList<RxPostInfo>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(RxList<RxPostInfo> bean) {
                        List<RxPostInfo> list = bean.getList();
                        dealData(list);
                        adapter.setPagingData(list, pageNo);
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


    public void loadMore() {
        pageNo++;
        getData(pageNo);
    }

    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {
            private DefaultDialog dialog;

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<RxPostInfo> datas = adapter.getData();
                RxPostInfo bean = datas.get(position);
                ActivityUtil.startLink(activity, bean.getPost());
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

                    case R.id.iv_del_icon:
                        if (dialog == null) {
                            dialog = new DefaultDialog(activity, "是否删除该帖子？", listener);
                        }
                        postId = bean.getPost().getId();
                        dialog.show();

                        break;

                    case R.id.tv_from:
                    case R.id.tv_from_hint:
                        ActivityUtil.startPlatePostActivity(activity, bean.getPlate().getId());
                        break;
                    case R.id.tv_user_name:
                    case R.id.tv_time:
                    case R.id.sdv_user_pic:
                        String id = bean.getAccount().getUserId();
                        if (!userId.equals(id)) {
                            ActivityUtil.startPersonalActivity(activity, id);
                        }
                        break;
                }
            }
        };
    }

    private OnDialogExecuteListener listener = new OnDialogExecuteListener() {
        @Override
        public void execute() {
            deletePost();
        }

        @Override
        public void cancel() {

        }
    };

    private void deletePost() {
        ApiWrapper.getInstance().deletePost(postId)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {
                        List<RxPostInfo> datas = adapter.getData();
                        for (int i = 0; i < datas.size(); i++) {
                            if (postId.equals(datas.get(i).getPost().getId())) {
                                adapter.remove(i);
                                break;
                            }
                        }
                    }
                });
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
