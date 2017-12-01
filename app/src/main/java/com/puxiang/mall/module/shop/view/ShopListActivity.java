package com.puxiang.mall.module.shop.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityShopBinding;
import com.puxiang.mall.module.mall.viewmodel.MsgCountViewModel;
import com.puxiang.mall.module.shop.adapter.ShopListAdapter;
import com.puxiang.mall.module.shop.viewModel.ShopHeadViewModel;
import com.puxiang.mall.module.shop.viewModel.ShopViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;

import java.util.List;

import static com.puxiang.mall.utils.StringUtil.getString;

/**
 * Created by zhaoyong bai on 2017/10/12.
 * 精选商家列表页面
 */

public class ShopListActivity extends BaseBindActivity implements EasyPermission.PermissionCallback, View.OnClickListener {
    private ActivityShopBinding shopBinding;
    private ShopViewModel shopViewModel;
    private ShopHeadViewModel shopHeadViewModel;
    private MsgCountViewModel msgCountViewModel;
    private ShopListAdapter shopListAdapter;
    private LocationClient locationClient;

    @Override
    protected void initBind() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermission();
        }
        shopBinding = DataBindingUtil.setContentView(this, R.layout.activity_shop);
        shopListAdapter = new ShopListAdapter(R.layout.item_shop);
        shopViewModel = new ShopViewModel(this, shopListAdapter);
        shopHeadViewModel = new ShopHeadViewModel(this);
        msgCountViewModel = new MsgCountViewModel(this);

        shopBinding.setHeadModel(shopHeadViewModel);
        shopBinding.setViewModel(shopViewModel);
        shopBinding.setMsgModel(msgCountViewModel);
        shopBinding.ivRefresh.setOnClickListener(this);
        locationClient=new LocationClient(getApplicationContext());
        mImmersionBar.keyboardEnable(false).init();
        initClient();
    }

    private void initClient() {
        LocationClientOption mOption = new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setScanSpan(60*1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mOption.setIsNeedLocationDescribe(false);//可选，设置是否需要地址描述
        mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
        mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.SetIgnoreCacheException(true);//可选，默认false，设置是否收集CRASH信息，默认收集

        mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationClient.setLocOption(mOption);
        locationClient.registerLocationListener(listener);
    }

  private   BDLocationListener listener=new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Logger.e("location----"+ bdLocation.getLatitude()+" --- "+bdLocation.getLongitude()+"--"+bdLocation.getStreet());
            if (bdLocation == null) {
                shopViewModel.getShopList(1,"","","");
            } else {
//                shopViewModel.setCurrentCity(bdLocation.getCity());
//                shopViewModel.setCurrentStreet(bdLocation.getAddress().address);
                shopViewModel.getCurrentLocation(bdLocation.getLatitude(), bdLocation.getLongitude());
            }
        }
    };



    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermission();
            return;
        }
        locationClient.start();

    }

    public void requestPermission() {
        EasyPermission.with(this)
                .rationale(getString(R.string.rationale_gps))
                .addRequestCode(PermissionCode.RG_LOCATION)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .request();
    }

    @Override
    public void initView() {
        shopListAdapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        shopListAdapter.setEnableLoadMore(true);
        shopListAdapter.setOnLoadMoreListener(() -> shopViewModel.loadMore(), shopBinding.bottomView);
        shopBinding.bottomView.setLayoutManager(new LinearLayoutManager(this));
        shopBinding.bottomView.setAdapter(shopListAdapter);
        shopBinding.bottomView.addOnItemTouchListener(shopViewModel.onItemTouchListener());
        initBanner(shopBinding.banner);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) shopBinding.banner.getLayoutParams();
        params.height = (int) (MyApplication.widthPixels *0.347);
        shopBinding.banner.setLayoutParams(params);
        shopBinding.banner.setAdapter(shopHeadViewModel);
        shopHeadViewModel.getBannerData();
        msgCountViewModel.getMsgCountData();
    }

    /**
     * 更新code信息，更新选择街道的城市信息
     *
     * @param code
     */
    public void updateCode(String code) {
        shopHeadViewModel.setAreaCode(code);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.unRegisterLocationListener(listener);
    }

    @Override
    protected void viewModelDestroy() {
        if (shopViewModel != null) {
            shopViewModel.destroy();
        }
        if (shopHeadViewModel != null) {
            shopHeadViewModel.destroy();
        }
        if (msgCountViewModel != null) msgCountViewModel.destroy();
    }

    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {
        if (requestCode == PermissionCode.RG_LOCATION) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationClient.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {
        if (requestCode == PermissionCode.RG_LOCATION) {
            shopViewModel.getShopList(1, "", "", "");
            ToastUtil.toast("需要打开您的定位权限");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_refresh:
                locationClient.requestLocation();
                break;
        }
    }

}
