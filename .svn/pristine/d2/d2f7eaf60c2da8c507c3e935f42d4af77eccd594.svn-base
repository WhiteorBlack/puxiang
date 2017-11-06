package com.puxiang.mall.module.search.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxUserInfo;
import com.puxiang.mall.module.search.adapter.MemberQuickAdapter;
import com.puxiang.mall.module.search.view.SearchMemberFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.StringUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SearchMemberViewModel extends BaseObservable implements ViewModel {
    private final Activity activity;
    private final LoadingWindow loadingWindow;
    private final SearchMemberFragment fragment;
    private String keyword = "";
    private int pageNo = 1;
    private boolean isInitData = false;
    private MemberQuickAdapter adapter;

    public SearchMemberViewModel(SearchMemberFragment fragment, MemberQuickAdapter adapter) {
        EventBus.getDefault().register(this);
        this.activity = fragment.getActivity();
        this.fragment = fragment;
        this.adapter = adapter;
        loadingWindow = new LoadingWindow(activity);
        initData();
    }

    private void initData() {
        keyword = fragment.getArguments().getString("keyword");
        fragment.setIsInitData();
    }

    /**
     * 获取搜索的用户数据
     *
     * @param pageNo 页码
     */
    private void update(int pageNo) {
        ApiWrapper.getInstance()
                .searchMember(keyword, pageNo)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .doOnTerminate(() -> {
                    loadingWindow.hidWindow();
                    if (isInitData) return;
                    isInitData = true;
                    fragment.setIsInitData();
                })
                .subscribe(new NetworkSubscriber<RxList<RxUserInfo>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(RxList<RxUserInfo> bean) {

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
        setNoneText(keyword);
        if (StringUtil.isEmpty(keyword)) {
            adapter.getData().clear();
            adapter.notifyDataSetChanged();
        } else {
            loadingWindow.showWindow();
            update(pageNo);
        }

    }

    /**
     * 空页面
     */
    private void setNoneText(String kw) {
        if (StringUtil.isEmpty(kw)) {
            adapter.getData().clear();
            adapter.setEmptyViewText("请输入搜索关键字~");
            return;
        }
        adapter.setEmptyViewText("搜索不到该用户~");
    }


    /**
     * 加载更多
     */
    public void loadMore() {
        pageNo++;
        update(pageNo);
    }

    /**
     * 点击事件
     */
    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                RxUserInfo userInfo = adapter.getData().get(i);
                String userId = userInfo.getUserId();
//                Intent personalIntent1 = new Intent(activity, PersonalActivity.class);
//                personalIntent1.putExtra(Config.USER_ID, userId);
//                activity.startActivity(personalIntent1);
            }
        };
    }


    @Override
    public void destroy() {
        loadingWindow.dismiss();
        EventBus.getDefault().unregister(this);
    }
}
