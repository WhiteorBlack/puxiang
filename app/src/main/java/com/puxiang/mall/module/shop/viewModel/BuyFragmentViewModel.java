package com.puxiang.mall.module.shop.viewModel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxBatchList;
import com.puxiang.mall.model.data.RxBatchTotalPrice;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.module.main.view.MainActivity;
import com.puxiang.mall.module.search.OrderEvent;
import com.puxiang.mall.module.search.adapter.SearchListAdapter;
import com.puxiang.mall.module.shop.adapter.BuyListAdapter;
import com.puxiang.mall.module.shop.view.BuyGoodsList;
import com.puxiang.mall.module.shop.view.BuyListFragment;
import com.puxiang.mall.module.shop.view.ShopGoodsFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * 商家详情底部商品列表页
 */
public class BuyFragmentViewModel extends BaseObservable implements ViewModel {


    private final BuyGoodsList activity;
    private final BuyListFragment fragment;
    private BuyListAdapter adapter;
    private String type;
    private String keyWord;
    private String order = "desc";
    private final LoadingWindow loadingWindow;
    private boolean isInitData = false;
    private int pageNo = 1, pageSize = 12;
    private int selectPos = 0;
    private boolean isLoaded = true;


    public BuyFragmentViewModel(BuyListFragment fragment, BuyListAdapter adapter) {
        this.fragment = fragment;
        this.activity = (BuyGoodsList) fragment.getActivity();
        this.adapter = adapter;
        EventBus.getDefault().register(this);
        loadingWindow = new LoadingWindow(activity);
//        loadingWindow.delayedShowWindow();
        initData();
        update(pageNo);

    }

    private void initData() {
        type = fragment.getArguments().getString("type");
        keyWord = fragment.getArguments().getString("keyword");
    }


    /**
     * 获取搜索商品数据
     *
     * @param pageNo 页码
     */
    public void update(final int pageNo) {
//        if (pageNo==1&&type.equals("1")){
//            loadingWindow.delayedShowWindow();
//        }
        ApiWrapper.getInstance().getBatchProducts("", pageNo)
                .doOnTerminate(() -> {
                    loadingWindow.hidWindow();
                    if (isInitData) return;
                    isInitData = true;
                    fragment.setIsInitData();
                })
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxBatchList>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(RxBatchList bean) {
                        adapter.setPagingData(bean.getList(), pageNo);
                    }
                });
    }

    private void getTotalPrice(int count) {
        isLoaded = false;
        RxProduct rxProduct = adapter.getData().get(selectPos);
        ApiWrapper
                .getInstance()
                .getBatchTotalPrice(rxProduct.getProductId(), count)
                .doOnTerminate(() -> isLoaded = true)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxBatchTotalPrice>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);

                    }

                    @Override
                    public void onSuccess(RxBatchTotalPrice data) {
//                        adapter.notifyItemChanged(selectPos);
                        if (rxProduct.isSelected()) {
                            if (rxProduct.getBatchTotalPrice() == 0) {
                                rxProduct.setBatchTotalPrice(rxProduct.getSalePrice() * rxProduct.getBatchStartQty());
                            }
                            activity.setTotalPrice(data.getTotalPrice() - rxProduct.getBatchTotalPrice());
                            activity.selectGoods(false, rxProduct);
                        }
                        rxProduct.setBatchTotalPrice(data.getTotalPrice());
                        adapter.notifyDataSetChanged();
                    }
                });
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

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                RxProduct rxProduct = (RxProduct) adapter.getData().get(position);
                if (!isLoaded) {
                    return;
                }
                selectPos = position;
                switch (view.getId()) {
                    case R.id.iv_add:
                        int addCount = rxProduct.getBatchStartQty() > rxProduct.getBuyCount() ? rxProduct.getBatchStartQty() : rxProduct.getBuyCount();
                        addCount++;
                        rxProduct.setBuyCount(addCount);
                        getTotalPrice(addCount);
                        break;
                    case R.id.iv_reduce:
                        int count = rxProduct.getBuyCount();
                        count--;
                        if (count < rxProduct.getBatchStartQty()) {
                            count = rxProduct.getBatchStartQty();
                        }
                        rxProduct.setBuyCount(count);
                        getTotalPrice(count);
                        break;
                    case R.id.cb_select:
                        try {
                            new Thread().sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        rxProduct.setSelected(!rxProduct.isSelected());
                        countMoney(rxProduct);
                        activity.selectGoods(rxProduct.isSelected(), rxProduct);
                        break;
                    case R.id.sdv_item_pic:
                    case R.id.tv_goods_name:
                    case R.id.tv_goods_info:
                        WebUtil.jumpGoodsWeb(activity, rxProduct.getProductId());
                        break;
                }
            }
        }

                ;
    }

    private void countMoney(RxProduct rxProduct) {
        double money = rxProduct.getBatchTotalPrice() > rxProduct.getInitTotalPrice() ? rxProduct.getBatchTotalPrice() : rxProduct.getInitTotalPrice();
        activity.setTotalPrice(rxProduct.isSelected() ? money : -money);
    }


}
