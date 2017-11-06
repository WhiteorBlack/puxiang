package com.puxiang.mall.module.shoppingcart.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableDouble;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.flyco.dialog.widget.NormalDialog;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxCartList;
import com.puxiang.mall.model.data.RxCartProduct;
import com.puxiang.mall.model.data.RxOrder;
import com.puxiang.mall.model.data.ShopCartData;
import com.puxiang.mall.module.login.view.LoginFragment;
import com.puxiang.mall.module.shoppingcart.adapter.ShoppingAdapter;
import com.puxiang.mall.module.shoppingcart.interfacer.ShopSelectListener;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;
import com.puxiang.mall.widget.dialog.DefaultDialog;
import com.puxiang.mall.widget.dialog.OnDialogExecuteListener;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车页面viewModel，用于H5跳转
 */

public class ShoppingCartActivityViewModel extends BaseObservable implements ViewModel, OnDialogExecuteListener, ShopSelectListener {

    private final BaseBindActivity activity;
    private final ShoppingAdapter adapter;
    //    private final LoadingWindow loadingWindow;
    private final NormalDialog dialog;
    public ObservableBoolean isNone = new ObservableBoolean(false);
    public ObservableBoolean isAll = new ObservableBoolean(false);
    public ObservableDouble totalPrice = new ObservableDouble(0.00);
    private List<ShopCartData> productList = new ArrayList<>();
    private double total;
    private int count = 0;
    private int selectNum = 0;
    private int selectPosition = 0;

    public ShoppingCartActivityViewModel(BaseBindActivity activity, ShoppingAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        dialog = new DefaultDialog(activity, "是否删除该商品？", this);
        EventBus.getDefault().register(this);
//        loadingWindow = new LoadingWindow(activity.getActivity());
        this.adapter.setShopSelectListener(this);
        getData();
    }

    /**
     * 计算总价
     */
    private void setTotalPrices() {
        total = 0;

        for (ShopCartData cartBean : productList) {

            if (!cartBean.isHeader() && cartBean.getCartProduct().isSelect()) {
                total += cartBean.getCartProduct().getSalePrice() * cartBean.getCartProduct().getBuyQty();
            }
        }
        totalPrice.set(total);
    }

    /**
     * 接收事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.LOGIN_REFRESH) {
            getData();
        }
        if (i == Event.LOGOUT) {
            getData();
        }
    }


    /**
     * 是否全选
     */
    private void ifAll() {
        if (selectNum == count && selectNum > 0) {
            isAll.set(true);
        } else {
            isAll.set(false);
        }
    }

    /**
     * 全选/全取消
     *
     * @param b
     */
    public void selectAll(boolean b) {
        total = 0;
        isAll.set(b);
        selectNum = b ? count : 0;
        for (ShopCartData cartData : productList) {
            cartData.setSelected(b);
            if (!cartData.isHeader()) {
                cartData.getCartProduct().setSelect(b);
                total += cartData.getCartProduct().getSalePrice() * cartData.getCartProduct().getBuyQty();
            }
        }
        totalPrice.set(total);
        adapter.notifyDataSetChanged();
    }

    /**
     * 请求网络
     * 获取购物车商品
     */
    private void getData() {
//        loadingWindow.delayedShowWindow();
        ApiWrapper.getInstance()
                .getCarts()
                .subscribe(new NetworkSubscriber<List<RxCartList>>() {
                    @Override
                    public void onSuccess(List<RxCartList> bean) {
                        setData(bean);
                    }
                });
    }

    /**
     * 添加数据
     *
     * @param list
     */
    private void setData(List<RxCartList> list) {
        List<ShopCartData> data = adapter.getData();
        data.clear();
        for (RxCartList cartList : list) {
            ShopCartData shopcart = new ShopCartData(true, "");
            shopcart.setSelected(false);
            shopcart.setLogoUrl(cartList.getLogoUrl());
            shopcart.setShopId(cartList.getShopId());
            shopcart.setShopName(cartList.getShopName());
            data.add(shopcart);
            for (RxCartProduct product : cartList.getCartList()) {
                ShopCartData shop = new ShopCartData(false);
                shop.setSelected(false);
                shop.setLogoUrl(cartList.getLogoUrl());
                shop.setShopId(cartList.getShopId());
                shop.setShopName(cartList.getShopName());
                product.setBuyQtyObser(product.getBuyQty());
                shop.setCartProduct(product);
                data.add(shop);
            }
        }
        productList = data;
        isNone.set(data.isEmpty());
        count = data.size();
        adapter.setNewData(data);
    }

    /**
     * 跳转首页
     */
    public void startBuy() {
        if (StringUtil.isEmpty(MyApplication.TOKEN)) {
            Intent intent = new Intent(activity, LoginFragment.class);
            activity.startActivity(intent);
        } else {
            //发送事件 ， 跳转到商城
            EventBus.getDefault().post(Event.GO_MALL);
            activity.onBackPressed();
        }
    }

    /**
     * 结算购物车
     */
    public void buy() {
        if (selectNum == 0) {
            ToastUtil.toast("请选择购买的商品");
            return;
        }
//        String str = "{\"productId\":%productId,\"productNum\":%productNum,\"cartId\":%cartId," +
//                "\"skuId\":%skuId},";
//        StringBuilder sb = new StringBuilder(URLs.ORDER_COMMIT);
//        sb.append("&productList=[");
//        for (ShopCartData cartBean : productList) {
//            if (!cartBean.isHeader() && cartBean.getCartProduct().isSelect()) {
//                String json = str.replaceAll("%productId", cartBean.getCartProduct().getProductId() + "")
//                        .replaceAll("%productNum", cartBean.getCartProduct().getProductNum() + "")
//                        .replaceAll("%cartId", cartBean.getCartProduct().getCartId() + "")
//                        .replaceAll("%skuId", cartBean.getCartProduct().getSkuId() + "");
//                sb.append(json);
//            }
//        }
//        sb.deleteCharAt(sb.length() - 1);
//        sb.append("]");
        List<RxOrder> orders = new ArrayList<>();
        boolean isFirst = true;
        RxOrder rxOrder = null;
        String shopId = "";
        List<RxOrder.Product> products = null;
        int i = 0;
        for (ShopCartData cartData : productList) {
            i++;
            if (!cartData.isHeader() && cartData.getCartProduct().isSelect()) {
                if (TextUtils.equals(shopId, cartData.getShopId())) {
                    isFirst = false;

                } else {
                    shopId = cartData.getShopId();
                    rxOrder = new RxOrder();
                    products = new ArrayList<>();
                    if (!isFirst) {
                        rxOrder.products = products;
                        orders.add(rxOrder);
                    }
                    rxOrder.shopId = shopId;

                }
                RxOrder.Product product = new RxOrder.Product();
                product.buyQty = cartData.getCartProduct().getBuyQty() + "";
                product.cartId = cartData.getCartProduct().getCartId();
                product.productId = cartData.getCartProduct().getProductId();
                product.skuId = cartData.getCartProduct().getSkuId() + "";
                products.add(product);
                if (i == productList.size()) {
                    rxOrder.products = products;
                    orders.add(rxOrder);
                }
            }
        }
        String orderString = new Gson().toJson(orders);
        Logger.e(orderString);
        orderString = URLs.ORDER_COMMIT + "&products=" + orderString;
        WebUtil.jumpWeb(orderString, activity);
    }

    /**
     * 删除数据
     */
    private void deleteData() {
//        RxCartProduct itemData = adapter.getData().get(selectPosition);
        --count;
//        if (itemData.isSelect()) --selectNum;
//        del(itemData.getCartId());
        adapter.remove(selectPosition);
        isNone.set(productList.isEmpty());
        ifAll();
        setTotalPrices();
    }

    public OnItemChildClickListener itemChildClickListener() {
        return new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int
                    i) {
                if (i == -1) {
                    return;
                }
                RxCartProduct itemData = new RxCartProduct();
                int num;
                switch (view.getId()) {

                    case R.id.tv_item_add:
                        num = itemData.getProductNum();
                        int stock = itemData.getStock();
                        if (num >= stock) {
                            ToastUtil.toast("库存不足");
                            return;
                        } else {
                            itemData.setProductNum(++num);
                            setNum(view, num, itemData.getCartId(), itemData.getProductId());
                        }
                        break;
                    case R.id.tv_item_reduce:
                        num = itemData.getProductNum();
                        if (num == 1) {
                            return;
                        }
                        itemData.setProductNum(--num);
                        setNum(view, num, itemData.getCartId(), itemData.getProductId());
                        break;
                    case R.id.iv_item_close:
                        selectPosition = i;
                        dialog.show();
                        break;
                    case R.id.sdv_item_pic:

                        break;
                    case R.id.tv_item_name:
                        WebUtil.jumpGoodsWeb(activity, itemData.getProductId());
                        break;
                }
            }

            private void setNum(View view, int num, String cartId, String proudctId) {
                TextView numView;
                ViewGroup v = (ViewGroup) view.getParent();
                numView = (TextView) v.findViewById(R.id.tv_item_num);
                numView.setText(String.valueOf(num));
                modifyNum(cartId, proudctId, num);
                setTotalPrices();
            }

        };
    }

    /**
     * 修改商品数量网络请求
     *
     * @param cartId     购物车id
     * @param proudctId  商品id
     * @param productNum 修改的数量
     */
    private void modifyNum(final String cartId, final String proudctId, final int productNum) {
        ApiWrapper.getInstance()
                .modifyCart(cartId, proudctId, productNum)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {

                    }
                });
    }

    /**
     * 删除购物车商品
     *
     * @param cartId 购物Id
     */
    private void del(final String cartId) {
        ApiWrapper.getInstance().deleteCart(cartId)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {
                    }
                });
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void execute() {
        deleteData();
    }

    @Override
    public void cancel() {

    }

    @Override
    public void onShopSelectListener(String shopId, boolean isSelected) {
        setShopSelect(shopId, isSelected);
    }

    private void setShopSelect(String shopId, boolean isSelected) {
        for (ShopCartData shopData : productList) {
            if (TextUtils.equals(shopId, shopData.getShopId())) {
                shopData.setSelected(isSelected);
                if (!shopData.isHeader()) {
                    shopData.getCartProduct().setSelect(isSelected);
                }
                if (isSelected) {
                    selectNum++;
                } else {
                    selectNum--;
                }
            }
        }
        ifAll();
        setTotalPrices();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onGoodsSelectListener(String shopId, String goodsId, int pos, boolean isSelected) {
        setGoodsSelect(shopId, goodsId, pos, isSelected);
    }

    private void setGoodsSelect(String shopId, String goodsId, int pos, boolean isSelected) {

        productList.get(pos).getCartProduct().setSelect(isSelected);
        if (isSelected) {
            selectNum++;
            setShopAllSelect(shopId, checkShopSelect(shopId));
        } else {
            setShopAllSelect(shopId, false);
            selectNum--;
            selectNum--;
        }

        setTotalPrices();
        ifAll();
        adapter.notifyDataSetChanged();
    }

    /**
     * 检查是否该店铺下的所有商品都已选中
     *
     * @param shopId
     * @return
     */
    private boolean checkShopSelect(String shopId) {
        boolean isAllSelect = true;
        for (ShopCartData cartData : productList) {
            if (TextUtils.equals(cartData.getShopId(), shopId) && !cartData.isHeader()) {
                if (!cartData.getCartProduct().isSelect()) {
                    isAllSelect = false;
                    break;
                }
            }
        }
        return isAllSelect;
    }

    /**
     * 更改商铺的选中状态
     *
     * @param shopId
     * @param isSelect
     */
    private void setShopAllSelect(String shopId, boolean isSelect) {
        if (isSelect) {
            selectNum++;
        }
        for (ShopCartData cartData : productList) {
            if (TextUtils.equals(cartData.getShopId(), shopId) && cartData.isHeader()) {
                cartData.setSelected(isSelect);
                break;
            }
        }

    }

    @Override
    public void onShopClickListener(String shopId) {
        WebUtil.jumpShopWeb(activity, shopId);
    }

    @Override
    public void onGoodsClickListener(String goodsId) {
        WebUtil.jumpGoodsWeb(activity, goodsId);
    }

    @Override
    public void onAddClickListener(int pos) {
        productList.get(pos).getCartProduct().setBuyQty(productList.get(pos).getCartProduct().getBuyQty() + 1);
        if (productList.get(pos).getCartProduct().isSelect()) {
            total += productList.get(pos).getCartProduct().getSalePrice();
            totalPrice.set(total);
        }
    }

    @Override
    public void onReduceClickListener(int pos) {
        int count = productList.get(pos).getCartProduct().getBuyQty();
        count--;
        if (count < 1) {
            count = 1;
        } else {
            if (productList.get(pos).getCartProduct().isSelect()) {
                total -= productList.get(pos).getCartProduct().getSalePrice();
                totalPrice.set(total);
            }
        }
        productList.get(pos).getCartProduct().setBuyQty(count);
    }

    @Override
    public void onDeleteClickListener(int pos) {
        if (productList.get(pos).getCartProduct().isSelect()) {
            selectNum--;
        }
        del(productList.get(pos).getCartProduct().getCartId());
        productList.remove(pos);
        checkShopVoid();
        adapter.notifyDataSetChanged();
        ifAll();
    }

    /**
     * 检查店铺下面有没有货物，如果有则不删除，如果没有则把该选项删除
     */
    private void checkShopVoid() {
        if (productList.size() > 1) {
            String shopId = productList.get(0).getShopId();
            int count = 0;
            for (int i = 1; i < productList.size(); i++) {
                if (TextUtils.equals(shopId, productList.get(i).getShopId()) && !productList.get(i).isHeader()) {
                } else {
                    productList.remove(i - 1);
                    break;
                }
            }

        } else {
            productList.clear();
            isAll.set(false);
        }
    }


}
