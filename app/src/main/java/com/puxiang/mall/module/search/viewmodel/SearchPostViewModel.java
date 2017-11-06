package com.puxiang.mall.module.search.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.module.search.adapter.PostQuickAdapter;
import com.puxiang.mall.module.search.view.SearchPostFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SearchPostViewModel extends BaseObservable implements ViewModel {

    private final Activity activity;
    private final LoadingWindow loadingWindow;
    private final PostQuickAdapter adapter;
    private final SearchPostFragment fragment;
    public String keyword = "";
    private boolean isInitData = false;

    private int pageNo = 1;

    public SearchPostViewModel(SearchPostFragment fragment, PostQuickAdapter adapter) {
        EventBus.getDefault().register(this);
        this.fragment = fragment;
        activity = fragment.getActivity();
        this.adapter = adapter;
        loadingWindow = new LoadingWindow(activity);
        initData();
        update(1);
    }

    private void initData() {
        keyword = fragment.getArguments().getString("keyword");
    }

    /**
     * 获取搜索帖子数据
     */
    private void update(final int pageNo) {
        ApiWrapper.getInstance().searchPost(keyword, pageNo)
                .doOnTerminate(() -> {
                    loadingWindow.hidWindow();
                    if (isInitData) return;
                    isInitData = true;
                    fragment.setIsInitData();
                })
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
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

    /**
     * 事件订阅
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String kw) {
        pageNo = 1;
        keyword = kw;
        loadingWindow.showWindow();
        update(pageNo);
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        pageNo++;
        update(pageNo);
    }

    /**
     * 点击响应
     */
    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                RxPostInfo bean = adapter.getData().get(i);
                ActivityUtil.startLink(activity, bean.getPost());
            }
        };
    }

    @Override
    public void destroy() {
        if (loadingWindow != null) {
            loadingWindow.dismiss();
        }
        EventBus.getDefault().unregister(this);
    }
}
