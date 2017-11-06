package com.puxiang.mall.module.post.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxPostComment;
import com.puxiang.mall.model.data.RxReply;
import com.puxiang.mall.module.post.adapter.CommentDetailQuickAdapter;
import com.puxiang.mall.module.post.view.CommentActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.LoadingWindow;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CommentViewModel extends BaseObservable implements ViewModel {

    private final CommentActivity activity;
    private final CommentDetailQuickAdapter adapter;
    private final LoadingWindow loadingWindow;
    public ObservableBoolean isInitData = new ObservableBoolean(false);
    private int pageNo = 1;
    public String commentId;
    public String postId;
    public String userId;
    public String byReplyUserId;

    public CommentViewModel(CommentActivity activity, CommentDetailQuickAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        loadingWindow = new LoadingWindow(activity);
        loadingWindow.delayedShowWindow();
        initData();
        getData(1);
        getPostComment();
    }

    private void initData() {
        commentId = activity.getIntent().getStringExtra("commentId");
        activity.setPageTag("commentId", commentId);
    }

    /**
     * 获取评论详情
     */
    private void getPostComment() {
        ApiWrapper.getInstance()
                .getPostComment(commentId)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> {
                    loadingWindow.hidWindow();
                    isInitData.set(true);
                })
                .subscribe(new NetworkSubscriber<RxPostComment>() {
                    @Override
                    public void onSuccess(RxPostComment bean) {
                        setData(bean);
                        activity.initHeadView(bean);
                    }
                });
    }

    private void setData(RxPostComment bean) {
        postId = bean.getPost().getId();
        userId = bean.getCommentUser().getUserId();
        byReplyUserId = bean.getComment().getCommentUserId();
    }

    /**
     * 获取评论中的回复
     *
     * @param pageNo 页码
     */
    public void getData(final int pageNo) {
        ApiWrapper.getInstance()
                .getCommentReplys(commentId, pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxList<RxReply>>() {
                    @Override
                    public void onSuccess(RxList<RxReply> bean) {
                        setData(bean, pageNo);
                    }
                });
    }

    /**
     * 刷新 数据
     */
    public void refresh() {
        pageNo = 1;
        EventBus.getDefault().post(Event.RELOAD_COMMENT);
        getData(pageNo);
    }

    /**
     * 设置回复数据
     *
     * @param replyList 回复数据
     * @param pageNo    页码
     */
    private void setData(RxList<RxReply> replyList, int pageNo) {
        List<RxReply> list = replyList.getList();
        if (pageNo == 1) {
            adapter.setNewData(list);
        } else {
            adapter.addData(list);
        }
        if (list.size() < URLs.PAGE_SIZE) {
            adapter.loadMoreEnd();
            if (pageNo == 1 && list.isEmpty()) {
                View footView = LayoutInflater.from(activity)
                        .inflate(R.layout.view_comment_none, null);
                AutoUtils.auto(footView);
                adapter.addFooterView(footView);
            } else {
                adapter.removeAllFooterView();
//                View footView = LayoutInflater.from(activity)
//                        .inflate(R.layout.view_bottom_line, null);
//                AutoUtils.auto(footView);
//                adapter.addFooterView(footView);
            }
        } else {
            adapter.loadMoreComplete();
        }
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
                String userId = adapter.getData().get(i).getCommentUser().getUserId();
                ActivityUtil.startPersonalActivity(activity, userId);
            }
        };
    }

    @Override
    public void destroy() {
        loadingWindow.dismiss();
    }
}
