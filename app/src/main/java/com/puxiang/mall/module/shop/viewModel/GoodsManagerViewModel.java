package com.puxiang.mall.module.shop.viewModel;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.HttpResult;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.module.shop.adapter.GoodsAdapter;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/4/25.
 */
public class GoodsManagerViewModel implements ViewModel {
    private BaseBindActivity activity;
    private int pageIndex = 1;
    private LoadingWindow loadingWindow;
    private GoodsAdapter adapter;
    private String keyword;
    private String shopId;
    private int selectPos = 0;
    private List<RxProduct> productList;

    public GoodsManagerViewModel(BaseBindActivity activity, GoodsAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        loadingWindow = new LoadingWindow(activity);
        loadingWindow.delayedShowWindow();
    }

    /**
     * 获取店铺下面所有的商品信息
     *
     * @param keywords
     * @param shopId
     * @param pageIndex
     */
    public void getShopGoods(String keywords, String shopId, int pageIndex) {
        if (pageIndex == 1) {
//            loadingWindow.showWindow();
            this.keyword = keywords;
            this.shopId = shopId;
        }
        ApiWrapper.getInstance()
                .searchProduct(keywords, shopId, pageIndex)
                .doOnTerminate(() -> loadingWindow.hidWindow())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxList<RxProduct>>() {
                    @Override
                    public void onSuccess(RxList<RxProduct> data) {
                        if (data == null || data.getList().size() == 0) {
                            return;
                        }
                        adapter.setPagingData(data.getList(), pageIndex);
                    }
                });
    }

    public void getGoodsInfo(String productId) {
        ApiWrapper.getInstance()
                .getProduct(productId)
                .doOnTerminate(() -> loadingWindow.hidWindow())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxProduct>() {
                    @Override
                    public void onSuccess(RxProduct data) {

                    }
                });
    }

    public void getTestData() {
        productList = new ArrayList<>();
        RxProduct rxProduct = new RxProduct();
        rxProduct.setProductName("测试商品1");
        rxProduct.setShopName("测试店家");
        rxProduct.setSalePrice(10.11);
        rxProduct.setMarketPrice(12.00);
        rxProduct.setSellCount("100");
        rxProduct.setSelected(false);
        rxProduct.setMainPictureUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524739596340&di=4ecd43d4791e42ededaf53143eca159b&imgtype=0&src=http%3A%2F%2Fimage.suning.cn%2Fuimg%2Fsop%2Fcommodity%2F680378003207457475167420_x.jpg");
        productList.add(rxProduct);

        RxProduct rxProduct1 = new RxProduct();
        rxProduct1.setProductName("测试商品2");
        rxProduct1.setShopName("测试店家");
        rxProduct1.setSalePrice(10.11);
        rxProduct1.setMarketPrice(12.00);
        rxProduct1.setSellCount("100");
        rxProduct1.setSelected(false);
        rxProduct1.setMainPictureUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524739596340&di=4ecd43d4791e42ededaf53143eca159b&imgtype=0&src=http%3A%2F%2Fimage.suning.cn%2Fuimg%2Fsop%2Fcommodity%2F680378003207457475167420_x.jpg");
        productList.add(rxProduct1);

        RxProduct rxProduct2 = new RxProduct();
        rxProduct2.setProductName("测试商品3");
        rxProduct2.setShopName("测试店家");
        rxProduct2.setSalePrice(10.11);
        rxProduct2.setMarketPrice(12.00);
        rxProduct2.setSellCount("100");
        rxProduct2.setSelected(false);
        rxProduct2.setMainPictureUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524739596340&di=4ecd43d4791e42ededaf53143eca159b&imgtype=0&src=http%3A%2F%2Fimage.suning.cn%2Fuimg%2Fsop%2Fcommodity%2F680378003207457475167420_x.jpg");
        productList.add(rxProduct2);

        RxProduct rxProduct3 = new RxProduct();
        rxProduct3.setProductName("测试商品4");
        rxProduct3.setShopName("测试店家");
        rxProduct3.setSalePrice(10.11);
        rxProduct3.setMarketPrice(12.00);
        rxProduct3.setSellCount("100");
        rxProduct3.setSelected(false);
        rxProduct3.setMainPictureUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524739596340&di=4ecd43d4791e42ededaf53143eca159b&imgtype=0&src=http%3A%2F%2Fimage.suning.cn%2Fuimg%2Fsop%2Fcommodity%2F680378003207457475167420_x.jpg");
        productList.add(rxProduct3);

        RxProduct rxProduct4 = new RxProduct();
        rxProduct4.setProductName("测试商品5");
        rxProduct4.setShopName("测试店家");
        rxProduct4.setSalePrice(10.11);
        rxProduct4.setMarketPrice(12.00);
        rxProduct4.setSellCount("100");
        rxProduct4.setSelected(false);
        rxProduct4.setMainPictureUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524739596340&di=4ecd43d4791e42ededaf53143eca159b&imgtype=0&src=http%3A%2F%2Fimage.suning.cn%2Fuimg%2Fsop%2Fcommodity%2F680378003207457475167420_x.jpg");
        productList.add(rxProduct4);
        adapter.setNewData(productList);
    }

    /**
     * 上架商品
     *
     * @param productId
     */
    private void upShelf(String productId) {
        ApiWrapper.getInstance()
                .upShelf(productId)
                .doOnTerminate(() -> loadingWindow.hidWindow())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                        notifyData(2);
                    }
                });
    }

    /**
     * 下架商品
     *
     * @param productId
     */
    private void downShelf(String productId) {
        ApiWrapper.getInstance()
                .downShelf(productId)
                .doOnTerminate(() -> loadingWindow.hidWindow())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                        notifyData(3);
                    }
                });
    }

    private void notifyData(int statue) {
        adapter.getData().get(selectPos).setStatus(statue);
        adapter.notifyItemChanged(selectPos);
    }

    /**
     * 删除商品
     *
     * @param productIds 商品Id合集使用逗号分隔开
     */
    private void delProduct(String productIds) {
//        testDel();
        ApiWrapper.getInstance()
                .delProduct(productIds)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> loadingWindow.hidWindow())
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                        List<RxProduct> rxProducts = adapter.getData();
                        for (int i = rxProducts.size() - 1; i >= 0; i--) {
                            if (rxProducts.get(i).manSelected) {
                                rxProducts.remove(i);
                            }
                        }
                        adapter.setNewData(rxProducts);
                    }
                });
    }

    private void testDel() {
        loadingWindow.hidWindow();
        List<RxProduct> rxProducts = adapter.getData();
        for (int i = rxProducts.size() - 1; i >= 0; i--) {
            if (rxProducts.get(i).manSelected) {
                rxProducts.remove(i);
            }
        }
        adapter.setNewData(rxProducts);
    }

    public void onLoadMore() {
        pageIndex++;
        getShopGoods(keyword, shopId, pageIndex);
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseadapter, View view, int position) {
                WebUtil.jumpGoodsWeb(activity, adapter.getData().get(position).getProductId());
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                super.onItemChildClick(baseQuickAdapter, view, position);
                selectPos=position;
                RxProduct rxProduct = adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.iv_goods_down:
                        loadingWindow.showWindow();
                        downShelf(rxProduct.getProductId());
                        break;
                    case R.id.iv_goods_up:
                        loadingWindow.showWindow();
                        upShelf(rxProduct.getProductId());
                        break;
                }
            }
        };
    }

    public void onClick(View view) {
        List<RxProduct> rxProducts = adapter.getData();
        switch (view.getId()) {
            case R.id.tv_add:
                WebUtil.jumpMyAgentWeb(URLs.HTML_ADD_GOODS_ADDRESS + "?shopId=" + MyApplication.SHOP_ID, activity);
                break;
            case R.id.tv_modify:
                String productId = "";
                for (int i = 0; i < rxProducts.size(); i++) {
                    if (rxProducts.get(i).manSelected) {
                        productId = rxProducts.get(i).getProductId();
                        break;
                    }
                }
                if (TextUtils.isEmpty(productId)) {
                    ToastUtil.toast("请选择想要修改的商品");
                    return;
                }
                WebUtil.jumpMyAgentWeb(URLs.HTML_MODIFY_GOODS_ADDRESS + "?shopId=" + MyApplication.SHOP_ID + "&productId=" + productId, activity);
                break;
            case R.id.tv_del:

                String productIds = "";
                for (int i = 0; i < rxProducts.size(); i++) {
                    if (rxProducts.get(i).manSelected) {
                        productIds += "," + rxProducts.get(i).getProductId();
                    }
                }
                if (TextUtils.isEmpty(productIds)) {
                    ToastUtil.toast("请选择要删除的商品");
                    return;
                }
                loadingWindow.showWindow();
                delProduct(productIds.substring(1));
                break;
        }
    }


    public void modifyGoods(String data) {
        RxProduct rxProduct = new Gson().fromJson(data, RxProduct.class);
        rxProduct.setMainPictureUrl(URLs.PHOTO_ROOT + rxProduct.getMainPictureUrl());
        List<RxProduct> rxProducts = adapter.getData();
        for (int i = 0; i < rxProducts.size(); i++) {
            if (rxProducts.get(i).manSelected) {
                RxProduct product = rxProducts.get(i);
                product.setMainPictureUrl(rxProduct.getMainPictureUrl());
                product.setSellCount(rxProduct.getSellCount());
                product.setMarketPrice(rxProduct.getMarketPrice());
                product.setSalePrice(rxProduct.getSalePrice());
                product.setShopName(rxProduct.getShopName());
                product.setProductName(rxProduct.getProductName());
//                adapter.setNewData(rxProducts);
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void destroy() {

    }
}
