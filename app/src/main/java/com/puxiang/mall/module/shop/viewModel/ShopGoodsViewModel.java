package com.puxiang.mall.module.shop.viewModel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.module.search.OrderEvent;
import com.puxiang.mall.module.search.adapter.SearchListAdapter;
import com.puxiang.mall.module.search.view.SearchListFragment;
import com.puxiang.mall.module.shop.view.ShopGoodsFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.WebUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 商家详情底部商品列表页
 */
public class ShopGoodsViewModel extends BaseObservable implements ViewModel {


    private final Activity activity;
    private final ShopGoodsFragment fragment;
    private SearchListAdapter adapter;
    private String type;
    private String shopId;
    private String order = "desc";
    //    private final LoadingWindow loadingWindow;
    private boolean isInitData = false;
    private int pageNo = 1, pageSize = 12;
    private String keyword = "";


    public ShopGoodsViewModel(ShopGoodsFragment fragment, SearchListAdapter adapter) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
        this.adapter = adapter;
        EventBus.getDefault().register(this);
//        loadingWindow = new LoadingWindow(activity);
//        loadingWindow.delayedShowWindow();
        initData();
        update(pageNo);

    }

    private void initData() {
        type = fragment.getArguments().getString("type");
        shopId = fragment.getArguments().getString("shopId");
    }


    /**
     * 获取搜索商品数据
     *
     * @param pageNo 页码
     */
    public void update(final int pageNo) {
        ApiWrapper.getInstance().getShopGoods(keyword, shopId, type, order, pageSize, pageNo)
                .doOnTerminate(() -> {

                    if (isInitData) return;
                    isInitData = true;
                    fragment.setIsInitData();
                })
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxList<RxProduct>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(RxList<RxProduct> bean) {
                        if (bean.getList() != null && bean.getList().size() > 2) {
                            EventBus.getDefault().post(Event.ENABLE_SCROLL);
                        }
                        adapter.setPagingData(bean.getList(), pageNo);
                    }
                });
    }

    /**
     * 事件订阅
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String keyword) {
        pageNo = 1;
        this.keyword = keyword;
        update(pageNo);
    }

    /**
     * 事件订阅
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OrderEvent orderEvent) {
        if (orderEvent != null && type.equals(orderEvent.getType())) {
            pageNo = 1;
            order = orderEvent.getOrder();
            update(pageNo);
        }
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        pageNo++;
        update(pageNo);
    }

    @Override
    public void destroy() {
//        if (loadingWindow != null) {
//            loadingWindow.dismiss();
//        }
        EventBus.getDefault().unregister(this);
    }

    /**
     * 点击响应
     */
    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                SearchListAdapter adapter = (SearchListAdapter) baseQuickAdapter;
                String productId = adapter.getData().get(i).getProductId();
                WebUtil.jumpGoodsWeb(activity, productId);
            }
        };
    }
}
