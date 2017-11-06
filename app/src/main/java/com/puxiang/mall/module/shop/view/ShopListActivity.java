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

    private LocationManager locationManager;
    private Location location;

    @Override
    protected void initBind() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermission();
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10 * 1000, 80, locationListener);
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

    }

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
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10 * 1000, 80, locationListener);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new AlertDialog.Builder(this)
                    .setMessage("GPS未开启，是否马上设置")
                    .setNegativeButton("取消", (dialogInterface, i) -> {

                    }).setPositiveButton("去设置", (dialogInterface, i) -> {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 0);
            });

            return;
        }
        location = getLastKnownLocation();

        // 判断GPS是否正常启动
        updateLocation(location);
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

    // 位置监听
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
            Logger.e("stateChange  " + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Logger.e("oooooooo");
            if (location != null) {
                updateLocation(location);
            } else {
                location = getLastKnownLocation();
                updateLocation(location);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            new AlertDialog.Builder(ShopListActivity.this)
                    .setMessage("GPS未开启，是否马上设置")
                    .setNegativeButton("取消", (dialogInterface, i) -> {

                    }).setPositiveButton("去设置", (dialogInterface, i) -> {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 0);
            });
        }

        @Override
        public void onLocationChanged(Location location) {
            Logger.e("onChanged");
            if (location != null) {
                updateLocation(location);
            } else {
                location = getLastKnownLocation();
                updateLocation(location);
            }

        }

    };

    /**
     * 防止拿到空的 location
     *
     * @return
     */
    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        if (bestLocation != null) {
            Logger.e("location" + bestLocation.getLongitude());
        } else {
            Logger.e("retry");
        }
        return bestLocation;
    }


    /**
     * 更新定位信息
     *
     * @param location
     */
    private void updateLocation(Location location) {
        if (location == null) {
            shopViewModel.getShopList(1,"","","");
        } else {
            shopViewModel.getCurrentLocation(location.getLatitude(), location.getLongitude());
        }

        Logger.e("location:--"+location.getLatitude()+"  lng"+location.getLongitude());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10 * 000, 80, locationListener);
            location = getLastKnownLocation();
            updateLocation(location);
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
                location = getLastKnownLocation();
                updateLocation(location);
                break;
        }
    }
}
