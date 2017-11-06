package com.puxiang.mall.module.search.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxPlate;
import com.puxiang.mall.module.search.adapter.PlateQuickAdapter;
import com.puxiang.mall.module.search.view.SearchPlateFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SearchPlateViewModel extends BaseObservable implements ViewModel {

    private final Activity activity;
    private final LoadingWindow loadingWindow;
    private final PlateQuickAdapter adapter;
    private final SearchPlateFragment fragment;
    public String keyword = "";
    private int pageNo = 1;
    private boolean isInitData = false;

    public SearchPlateViewModel(SearchPlateFragment fragment, PlateQuickAdapter adapter) {
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
     * 获取搜索的圈子数据
     *
     * @param pageNo 页码
     */
    private void update(final int pageNo) {
        ApiWrapper.getInstance()
                .searchPlate(keyword, pageNo)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .doOnTerminate(() -> {
                    loadingWindow.hidWindow();
                    if (isInitData) return;
                    isInitData = true;
                    fragment.setIsInitData();
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
                RxPlate plate = adapter.getData().get(i);
                String plateId = plate.getId();
//                Intent plateIntent = new Intent(activity, PlatePostActivityNew.class);
//                plateIntent.putExtra("plateId", plateId);
//                activity.startActivity(plateIntent);
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
