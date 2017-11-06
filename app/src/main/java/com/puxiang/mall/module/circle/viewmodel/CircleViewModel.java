package com.puxiang.mall.module.circle.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.module.circle.adapter.CircleAdapter;
import com.puxiang.mall.module.circle.view.CircleFragment;
import com.puxiang.mall.module.circle.view.CirclePageFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class CircleViewModel extends BaseObservable implements ViewModel {

    private final Activity activity;
    private final CircleAdapter adapter;
    private final CircleFragment fragment;
    private String typeId;
    public boolean isFirst;
    private int pageNo = 1;
    public ObservableBoolean init = new ObservableBoolean();

    public CircleViewModel(CircleFragment fragment, CircleAdapter adapter) {
        EventBus.getDefault().register(this);
        this.fragment = fragment;
        this.activity = fragment.getActivity();
        this.adapter = adapter;
        getCache(fragment);
        fragment.setPageTag("typeId", typeId);
        if (!isFirst || CirclePageFragment.CREATE_STATUS != CirclePageFragment.STATUS_FIRST) {
            getData(1);
        }

    }


    /**
     * 获取缓存
     *
     * @param fragment
     */
    public void getCache(CircleFragment fragment) {
        isFirst = fragment.getArguments().getBoolean("first", false);
        typeId = fragment.getArguments().getString("typeId", typeId);
        getPostListCache();
    }


    /**
     * 获取页面缓存数据
     */
    private void getPostListCache() {
        MyApplication.mCache.getAsListBean(CacheKey.HOME_POSTS + typeId, RxPostInfo[].class, postInfoList -> {
            init.set(true);
            adapter.setPagingData(postInfoList, 1);
        });
    }

    /**
     * 请求首页页面数据
     *
     * @param pageNo 页码
     */
    public void getData(final int pageNo) {
        if (pageNo==1){
            this.pageNo=1;
        }
        if (isFirst) {
            //新频道数据
            ApiWrapper.getInstance()
                    .newestPosts(typeId, pageNo)
                    .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                    .doOnTerminate(() -> init.set(true))
                    .subscribe(new NetworkSubscriber<RxList<RxPostInfo>>() {
                        @Override
                        public void onFail(RetrofitUtil.APIException e) {
                            super.onFail(e);
                            adapter.loadMoreFail();
                        }

                        @Override
                        public void onSuccess(RxList<RxPostInfo> bean) {
                            List<RxPostInfo> postInfoList = bean.getList();
                            if (pageNo == 1) {
                                MyApplication.mCache.put(CacheKey.HOME_POSTS + typeId, postInfoList);
                            }
                            dealData(postInfoList);
                            adapter.setPagingData(postInfoList, pageNo);
                        }
                    });
        } else {
            //其他频道数据
            ApiWrapper.getInstance().officialPosts(typeId, pageNo)
                    .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                    .doOnTerminate(() -> init.set(true))
                    .subscribe(new NetworkSubscriber<RxList<RxPostInfo>>() {
                        @Override
                        public void onFail(RetrofitUtil.APIException e) {
                            super.onFail(e);
                            adapter.loadMoreFail();
                        }

                        @Override
                        public void onSuccess(RxList<RxPostInfo> bean) {
                            List<RxPostInfo> postInfoList = bean.getList();
                            if (pageNo == 1) {
                                MyApplication.mCache.put(CacheKey.HOME_POSTS + typeId, postInfoList);
                            }
                            dealData(postInfoList);
                            adapter.setPagingData(postInfoList, pageNo);
                        }
                    });
        }
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.RELOAD) {
            getData(1);
        }
    }

    public void loadMore() {
        pageNo++;
        getData(pageNo);
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                try {
                    RxPostInfo bean = adapter.getData().get(i);
                    ActivityUtil.startLink(activity, bean.getPost());
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
                        ActivityUtil.startPostFrom(activity, bean);
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
}
