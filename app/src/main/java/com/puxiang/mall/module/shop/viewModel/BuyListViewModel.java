package com.puxiang.mall.module.shop.viewModel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.BR;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxOrder;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.model.data.RxShop;
import com.puxiang.mall.model.data.ShopCartData;
import com.puxiang.mall.module.search.OrderEvent;
import com.puxiang.mall.module.shop.view.BuyGoodsList;
import com.puxiang.mall.module.shop.view.ShopDetial;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;
import com.puxiang.mall.widget.MyBanner;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by zhaoyong bai on 2017/10/18.
 */

public class BuyListViewModel extends BaseObservable implements ViewModel, BGABanner.Adapter, BGABanner.Delegate<View, String> {
    private static BuyGoodsList activity;
    private List<String> bannerList = new ArrayList<>();
    private String shopId;
    private ObservableField<RxShop> shopDetial = new ObservableField<>();
    private ObservableInt selectPos = new ObservableInt(0);
    private ObservableBoolean isUp = new ObservableBoolean(false);
    private ObservableBoolean isInitData = new ObservableBoolean(true);
    private List<String> descList = new ArrayList<>();
    private ObservableField<Double> totalMoney = new ObservableField<>(0.0d);
    private List<RxProduct> productList = new ArrayList<>();
    private int selectedPos = -1;

    public BuyListViewModel(BuyGoodsList activity) {
        this.activity = activity;
        shopId = activity.getIntent().getStringExtra("shopId");
    }


    public void getShopDetialData() {
        ApiWrapper.getInstance()
                .getShopDetial(shopId)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxShop>() {
                    @Override
                    public void onSuccess(RxShop data) {
                        setShopDetial(data);
                        dealData(data);
                    }
                });
    }

    private void dealData(RxShop data) {
        String[] desc = data.getFeature().split(",");
        String[] banner = data.getNvgPics().split(",");
        for (int i = 0; i < desc.length; i++) {
            descList.add(desc[i]);
        }
        for (int i = 0; i < banner.length; i++) {
            bannerList.add(banner[i]);
        }
        notifyPropertyChanged(BR.bannerList);
        notifyPropertyChanged(BR.descList);
    }

    /**
     * 结算进货
     */
    public void buy() {
        if (productList.size() == 0) {
            ToastUtil.toast("请选择购买的商品");
            return;
        }

        if (TextUtils.isEmpty(MyApplication.TOKEN)){
            ActivityUtil.startLoginActivity(activity);
            return;
        }

        List<RxOrder> orders = new ArrayList<>();
        boolean isFirst = true;
        RxOrder rxOrder = null;
        String shopId = "";
        List<RxOrder.Product> products = null;
        int i = 0;
        for (RxProduct rxProduct : productList) {
            i++;

                if (TextUtils.equals(shopId, rxProduct.getShopId())) {
                    isFirst = false;

                } else {
                    shopId = rxProduct.getShopId();
                    rxOrder = new RxOrder();
                    products = new ArrayList<>();
                    if (!isFirst) {
                        rxOrder.products = products;
                        orders.add(rxOrder);
                    }
                    rxOrder.shopId = shopId;
                    rxOrder.userId= MyApplication.USER_ID;
                }
                RxOrder.Product product = new RxOrder.Product();
                product.buyQty = (rxProduct.getBuyCount()>rxProduct.getBatchStartQty()?rxProduct.getBuyCount():rxProduct.getBatchStartQty()) + "";
                product.productId = rxProduct.getProductId();
                products.add(product);
                if (i == productList.size()) {
                    rxOrder.products = products;
                    orders.add(rxOrder);
                }
        }
        String orderString = new Gson().toJson(orders);
        Logger.e(orderString);
        orderString = URLs.BATCH_COMMIT + "&products=" + orderString;
        WebUtil.jumpWeb(orderString, activity);
    }


    @Override
    public void fillBannerItem(BGABanner banner, View itemView, Object model, int position) {
        ((SimpleDraweeView) itemView).setImageURI((String) model);
    }

    @BindingAdapter("headBanner")
    public static void setBanner(MyBanner banner, List<String> list) {
        if (list == null || list.size() < 1) {
            return;
        }

        List<String> tips = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            tips.add("");
        }
        banner.setData(R.layout.viewpager_img, list, tips);
    }

    public void setSelectPos(int pos) {
        selectedPos = pos;
        this.selectPos.set(pos);
        activity.setCurrentPos(pos);
        notifyPropertyChanged(BR.selectPos);
    }

    @Bindable
    public int getSelectPos() {
        return selectPos.get();
    }

    public void setIsUp(boolean isUp) {
        this.isUp.set(isUp);
        notifyPropertyChanged(BR.isUp);
    }

    public void setTotalMoney(double money) {
        double moneyT = getTotalMoney();
        moneyT += money;

        this.totalMoney.set(moneyT < 0 ? 0 : moneyT);
        notifyPropertyChanged(BR.totalMoney);
    }

    public void setSelectGoods(boolean isSelected, RxProduct rxProduct) {
        if (isSelected) {
            productList.add(rxProduct);
        } else {
            productList.remove(rxProduct);
        }
    }

    @Bindable
    public double getTotalMoney() {
        return totalMoney.get();
    }

    @Bindable
    public boolean getIsUp() {
        return isUp.get();
    }

    @Bindable
    public List<String> getBannerList() {
        return bannerList;
    }

    public void setShopDetial(RxShop shopDetial) {
        this.shopDetial.set(shopDetial);
        notifyPropertyChanged(BR.shopDetial);
    }

    @Bindable
    public RxShop getShopDetial() {
        return shopDetial.get();
    }

    @Bindable
    public List<String> getHeadBanner() {
        return bannerList;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_sall:
                setSelectPos(0);
                break;
            case R.id.ll_price:
                setSelectPos(1);
                break;
            case R.id.ll_new:
                setSelectPos(2);
                break;
        }
    }

    @Bindable
    public boolean getIsInitData() {
        return isInitData.get();
    }

    public void setIsInitData(boolean isInitData) {
        this.isInitData.set(isInitData);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, String model, int position) {

    }
}
