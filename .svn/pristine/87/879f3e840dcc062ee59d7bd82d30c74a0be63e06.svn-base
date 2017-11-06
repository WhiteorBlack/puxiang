package com.puxiang.mall.module.bbs.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxPlate;
import com.puxiang.mall.module.bbs.adapter.SelectPlateAdapter;
import com.puxiang.mall.module.bbs.view.PublishActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.LoadingWindow;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.List;


public class SelectPlateViewModel extends BaseObservable implements ViewModel {

    private final BaseBindActivity activity;
    private final SelectPlateAdapter adapter;
    private final LoadingWindow loadingWindow;
    private int pageNo = 1;
    private String plateId;
    public ObservableBoolean isInitData = new ObservableBoolean();

    public SelectPlateViewModel(BaseBindActivity activity, SelectPlateAdapter adapter) {
        plateId = activity.getIntent().getStringExtra(Config.PLATE_ID);
        this.activity = activity;
        this.adapter = adapter;
        loadingWindow = new LoadingWindow(activity);
        loadingWindow.delayedShowWindow();
        getData(1);
    }

    /**
     * 获取圈子列表
     * @param pageNo
     */
    private void getData(final int pageNo) {
        ApiWrapper.getInstance()
                .getPlates(this.pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> {
                    loadingWindow.hidWindow();
                    isInitData.set(true);
                })
                .subscribe(new NetworkSubscriber<RxList<RxPlate>>() {
                    @Override
                    public void onSuccess(RxList<RxPlate> bean) {
                        List<RxPlate> list = bean.getList();
                        signListSelect(list);
                        adapter.setPagingData(list, pageNo);
                    }
                });
    }

    /**
     * 标记选中
     *
     * @param list
     */
    private void signListSelect(List<RxPlate> list) {
        for (RxPlate plate : list) {
            if (plate.getId().equals(plateId)) {
                plate.setSelect(true);
                break;
            }
        }
    }


    @Override
    public void destroy() {
        if (loadingWindow != null) {
            loadingWindow.dismiss();
        }
    }

    public void loadMore() {
        pageNo++;
        getData(pageNo);
    }

    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                List<RxPlate> data = adapter.getData();
                for (RxPlate bean : data) {
                    bean.setSelect(false);
                }
                data.get(i).setSelect(true);
                Intent intent = new Intent(activity, PublishActivity.class);
                intent.putExtra(Config.PLATE_NAME, data.get(i).getPlateName());
                intent.putExtra(Config.PLATE_ID, data.get(i).getId());
                activity.setResult(PublishViewModel.SELECT_PLATE_CODE, intent);
                activity.finish();
            }
        };
    }
}
