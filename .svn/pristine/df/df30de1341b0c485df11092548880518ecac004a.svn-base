package com.puxiang.mall.module.bbs.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.module.bbs.adapter.VideoClassifyLeftAdapter;
import com.puxiang.mall.module.bbs.adapter.VideoRightAdapter;
import com.puxiang.mall.module.bbs.view.VideoFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.widget.verticaltablayout.VTabLayout;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;


public class VideoViewModel extends BaseObservable implements ViewModel {

    private final Activity activity;
    private final VideoRightAdapter adapter;
    private final VideoClassifyLeftAdapter tabAdapter;
    private final VideoFragment fragment;
    private SparseArray<List<RxPostInfo>> sa = new SparseArray<>();

    public VideoViewModel(VideoFragment fragment, VideoRightAdapter adapter, VideoClassifyLeftAdapter tabAdapter) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
        this.adapter = adapter;
        this.tabAdapter = tabAdapter;
    }

    /**
     * 获取频道内视频列表
     * @param i
     */
    public void getRightData(final int i) {
        int typeId = tabAdapter.getItemData(i).getTypeId();
        List<RxPostInfo> list = sa.get(typeId);
        if (list != null && list.size() != 0) {
            adapter.setNewData(list);
        } else {
            getData(typeId);
        }
    }

    /**
     * 获取视频频道目录
     * @param typeId
     */
    private void getData(int typeId) {
        ApiWrapper.getInstance()
                .officialVideos(typeId, 1)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxList<RxPostInfo>>() {
                    @Override
                    public void onSuccess(RxList<RxPostInfo> bean) {
                        setData(bean);
                    }
                });
    }

    private void setData(RxList<RxPostInfo> rxPostsData) {
        List<RxPostInfo> list = rxPostsData.getList();
        if (list != null) {
            adapter.setNewData(list);
            if (!list.isEmpty()) {
                int postTypeId = list.get(0).getPost().getPostTypeId();
                sa.put(postTypeId, list);
            }
        }
    }

    @Override
    public void destroy() {
    }

    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String postId = adapter.getData().get(i).getPost().getId();
                ActivityUtil.startPostDetailActivity(activity, postId);
            }
        };
    }

    public VTabLayout.OnItemOnClickListener tabItemClickListener() {
        return this::getRightData;
    }
}
