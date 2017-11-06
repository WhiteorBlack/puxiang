package com.puxiang.mall.module.shop.viewModel;

import android.Manifest;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.facebook.drawee.view.SimpleDraweeView;
import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxShop;
import com.puxiang.mall.module.search.OrderEvent;
import com.puxiang.mall.module.shop.view.ShopDetial;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.MapUtils;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;
import com.puxiang.mall.widget.MyBanner;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;

import static com.puxiang.mall.utils.StringUtil.getString;

/**
 * Created by zhaoyong bai on 2017/10/18.
 */

public class ShopDetialViewModel extends BaseObservable implements ViewModel, BGABanner.Adapter, BGABanner.Delegate<View, String> {
    private static ShopDetial activity;
    private List<String> bannerList = new ArrayList<>();
    private String shopId;
    private ObservableField<RxShop> shopDetial = new ObservableField<>();
    private ObservableInt selectPos = new ObservableInt(0);
    private ObservableBoolean isUp = new ObservableBoolean(false);
    private List<String> descList = new ArrayList<>();
    private int selectedPos = -1;
    private RxShop rxShop;

    public ShopDetialViewModel(ShopDetial activity) {
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
                        rxShop = data;
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

    @BindingAdapter("descList")
    public static void setDesc(LinearLayout layout, List<String> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                View view = LayoutInflater.from(activity).inflate(R.layout.item_textview, layout, false);
                TextView tvName = view.findViewById(R.id.tv_name);
                tvName.setText(list.get(i));
                layout.addView(view);
            }
        }
    }

    @Bindable
    public List<String> getDescList() {
        return descList;
    }

    public void detialClick(View v) {
        switch (v.getId()) {
            case R.id.iv_call:
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:" + shopDetial.get().getLinkPhone()));
                activity.startActivity(intent1);
                break;
            case R.id.ll_sall:
                setSelectPos(0);
                break;
            case R.id.ll_price:
                if (selectedPos == 1) {
                    setIsUp(!isUp.get());
                    EventBus.getDefault().post(new OrderEvent(isUp.get() ? "asc" : "desc", "2"));
                    return;
                }
                setSelectPos(1);
                break;
            case R.id.ll_new:
                setSelectPos(2);
                break;
            case R.id.tv_address:
                if (MapUtils.isInstalledApp(activity, "com.autonavi.minimap") && MapUtils.isInstalledApp(activity, "com.baidu.BaiduMap")) {
                    showDialog();
                    return;
                }
                if (MapUtils.isInstalledApp(activity, "com.autonavi.minimap") && !MapUtils.isInstalledApp(activity, "com.baidu.BaiduMap")) {
                    MapUtils.openGaode(activity, rxShop.getShopName(), rxShop.getLat(), rxShop.getLng());
                    return;
                }
                if (!MapUtils.isInstalledApp(activity, "com.autonavi.minimap") && MapUtils.isInstalledApp(activity, "com.baidu.BaiduMap")) {
                    MapUtils.openBaiDuMap(activity, rxShop.getShopName(), rxShop.getLat(), rxShop.getLng());
                    return;
                }
                break;
        }
    }

    /**
     * 创建选择地图弹窗
     */
    private void showDialog() {
        ActionSheet.createBuilder(activity, activity.getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("百度地图", "高德地图")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                MapUtils.openBaiDuMap(activity, rxShop.getShopName(), rxShop.getLat(), rxShop.getLng());
                                break;
                            case 1:
                                MapUtils.openGaode(activity, rxShop.getShopName(), rxShop.getLat(), rxShop.getLng());
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    @Override
    public void destroy() {
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, String model, int position) {

    }
}
