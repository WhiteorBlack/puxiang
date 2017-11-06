package com.puxiang.mall.module.personal.viewmodel;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxReplyPostComment;
import com.puxiang.mall.module.personal.adapter.ReplyAdapter;
import com.puxiang.mall.module.personal.view.PersonalReplyFragment;
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

import static com.puxiang.mall.R.id.sdv_user_pic;
import static com.puxiang.mall.R.id.tv_time;
import static com.puxiang.mall.R.id.tv_user_name;


/**
 * 我的评论
 */

public class ReplyViewModel implements ViewModel {

    private final ReplyAdapter adapter;
    private final PersonalReplyFragment fragment;
    private final Activity activity;
    public String keyword = "";
    private boolean isInitData = false;

    private int pageNo = 1;
    private String userId;
    private Dialog dialog;
    private String commentId;

    public ReplyViewModel(PersonalReplyFragment fragment, ReplyAdapter adapter) {
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

    private void getData(final int pageNo) {
        ApiWrapper.getInstance()
                .getReplyPostComments(userId, pageNo)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .doOnTerminate(() -> {
                    if (isInitData) return;
                    isInitData = true;
                    fragment.setIsInitData();
                })
                .subscribe(new NetworkSubscriber<RxList<RxReplyPostComment>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(RxList<RxReplyPostComment> bean) {
                        adapter.setPagingData(bean.getList(),pageNo);
                    }
                });
    }

    public void loadMore() {
        pageNo++;
        getData(pageNo);
    }

    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int
                    i) {
                List<RxReplyPostComment> datas = adapter.getData();
                RxReplyPostComment data = datas.get(i);
                switch (view.getId()) {
                    case sdv_user_pic:
                    case tv_user_name:
                    case tv_time:
                        String id = data.getCommentUser().getUserId();
                        if (!userId.equals(id)) {
                            ActivityUtil.startPersonalActivity(activity, id);
                        }
                        break;
                    case R.id.tv_title:
                        ActivityUtil.startPostDetailActivity(activity, data.getPostId());
                        break;
                    case R.id.iv_del_icon:
                        if (dialog == null) {
                            dialog = new DefaultDialog(activity, "是否删除该回复？", listener);
                        }
                        commentId = data.getCommentId();
                        dialog.show();
                        break;
                    case R.id.tv_comment1:
                    case R.id.tv_comment2:
                    case R.id.tv_more_comment:
                        String commentId = data.getCommentId();
                        ActivityUtil.startCommentActivity(activity, commentId);
                        break;
                }
            }
        };
    }

    private OnDialogExecuteListener listener = new OnDialogExecuteListener() {
        @Override
        public void execute() {
            deleteComment();
        }

        @Override
        public void cancel() {

        }
    };

    private void deleteComment() {
        ApiWrapper.getInstance().deleteComment(commentId)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {
                        List<RxReplyPostComment> datas = adapter.getData();
                        for (int i = 0; i < datas.size(); i++) {
                            if (commentId.equals(datas.get(i).getCommentId())) {
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
