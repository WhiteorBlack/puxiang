package com.puxiang.mall.module.dealer.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.R;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxArea;
import com.puxiang.mall.model.data.RxDealer;
import com.puxiang.mall.model.data.RxDealerCheck;
import com.puxiang.mall.model.data.RxDealerList;
import com.puxiang.mall.module.dealer.adapter.DealerAdapter;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/3.
 */
public class AllViewModel implements ViewModel {
    private BaseBindFragment fragment;
    private DealerAdapter adapter;
    private int pageNo = 1;
    private String areaCode;
    private String city = "";
    private LoadingWindow loadingWindow;

    public AllViewModel(BaseBindFragment fragment, DealerAdapter adapter) {
        this.fragment = fragment;
        this.adapter = adapter;
        loadingWindow = new LoadingWindow(fragment.getActivity());
        EventBus.getDefault().register(this);
    }

    public void loadMore() {
        pageNo++;
        getDealerList(pageNo, areaCode, city);
    }

    public void getDealerList(int pageNo, String areaCode, String city) {
        if (pageNo == 1) {
            this.pageNo = 1;
            this.areaCode = areaCode;
            this.city = city;
        }
        ApiWrapper.getInstance()
                .getDealerList(city, areaCode, pageNo)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY.DESTROY))
                .subscribe(new NetworkSubscriber<RxDealerList>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(RxDealerList data) {
                        adapter.setPagingData(data.getList(), pageNo);
                    }
                });

    }

    public void selectAll() {
        List<RxDealerCheck> rxDealerChecks = adapter.getData();
        if (rxDealerChecks != null && rxDealerChecks.size() > 0) {

            for (int i = 0; i < rxDealerChecks.size(); i++) {
                rxDealerChecks.get(i).setSelected(true);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void link() {
        String ids = "";
        List<RxDealerCheck> rxDealerChecks = adapter.getData();
        if (rxDealerChecks != null && rxDealerChecks.size() > 0) {

            for (int i = 0; i < rxDealerChecks.size(); i++) {
                if (rxDealerChecks.get(i).isSelected()) {
                    ids = ids + "," + rxDealerChecks.get(i).getDealerId();
                }
            }
        }
        applyDealer(ids);
    }

    public void cancelAll() {
        List<RxDealerCheck> rxDealerChecks = adapter.getData();
        if (rxDealerChecks != null && rxDealerChecks.size() > 0) {

            for (int i = 0; i < rxDealerChecks.size(); i++) {
                rxDealerChecks.get(i).setSelected(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RxArea rxCity) {
        if (rxCity != null) {
            pageNo = 1;
            getDealerList(pageNo, rxCity.getAreaCode(), rxCity.getName());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String code) {
        if (!TextUtils.isEmpty(code)) {
            pageNo = 1;
            getDealerList(pageNo, code, city);
        }
    }

    private void cancelDealer(String ids) {
        ApiWrapper.getInstance().cancelDealer(ids)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {

                    }
                });
    }

    private void applyDealer(String ids) {
        loadingWindow.showWindow();
        ApiWrapper.getInstance().applyDealer(ids)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .doOnTerminate(loadingWindow::hidWindow)
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(String data) {
                        dealData();
                    }
                });
    }

    private void dealData() {
        List<RxDealerCheck> rxDealerChecks = adapter.getData();
        if (rxDealerChecks != null && rxDealerChecks.size() > 0) {

            for (int i = rxDealerChecks.size() - 1; i >= 0; i--) {
                if (rxDealerChecks.get(i).isSelected()) {
                    rxDealerChecks.remove(i);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapterT, View view, int position) {
                ActivityUtil.startShopDetialActivity(fragment.getActivity(), adapter.getData().get(position).getShopId());
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapterT, View view, int position) {
                super.onItemChildClick(adapterT, view, position);
                adapter.getData().get(position).setSelected(true);
                switch (view.getId()) {
                    case R.id.iv_goods_down:
                        cancelDealer("");
                        break;
                    case R.id.iv_goods_up:
                        applyDealer(adapter.getData().get(position).getDealerId());
                        break;
                }
            }
        };
    }


    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
