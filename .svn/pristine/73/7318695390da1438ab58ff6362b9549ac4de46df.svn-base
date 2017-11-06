package com.puxiang.mall.utils.location;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.widget.dialog.DefaultDialog;
import com.puxiang.mall.widget.dialog.OnDialogExecuteListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class LocationUtil {

//    public static Location getLocation() {
//        //You do not instantiate this class directly;
//        //instead, retrieve it through:
//        //Context.getSystemService(Context.LOCATION_SERVICE).
//        LocationManager locationManager = (LocationManager) MyApplication.getContext()
//                .getSystemService(MyApplication.getContext().LOCATION_SERVICE);
//        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
//        //获取GPS支持
////        if (ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
////                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyApplication
/// .getContext()
////                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            // TODO: Consider calling
////            //    ActivityCompat#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for ActivityCompat#requestPermissions for more details.
////            return location;
////        }
//
//        if (location == null) {
//            //获取NETWORK支持
//            location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
//        }
//        return location;
//    }

    public static double LATITUDE = 0;
    public static double LONGITUDE = 0;

    public static Location getLocation() {
        if (ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ToastUtil.toast("请允许APP使用定位权限");
            return null;
        }
        //1.获取位置管理器
        LocationManager locationManager = (LocationManager) MyApplication.getContext().getSystemService(Context
                .LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders(true);
        String locationProvider = LocationManager.GPS_PROVIDER;
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            ToastUtil.toast("没有可用的位置提供器");
        }


        Location location = locationManager.getLastKnownLocation(locationProvider);
        //3.获取上次的位置，一般第一次运行，此值为null
        if (location != null) {
            showLocation(location);
        } else {
            // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
            locationManager.requestLocationUpdates(locationProvider, 0, 0, new LocationListener() {
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }

                // 如果位置发生变化，重新显示
                @Override
                public void onLocationChanged(Location location) {
                    showLocation(location);
                    locationManager.removeUpdates(this);
                }
            });
        }
        return location;
    }

    private static void showLocation(Location location) {
        LATITUDE = location.getLatitude();
        LONGITUDE = location.getLongitude();
    }

    /**
     * 监听GPS
     */
    public static boolean checkGPS(Activity activity) {
        LocationManager locationManager = (LocationManager) MyApplication.getContext()
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        boolean isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isEnabled) {
            new DefaultDialog(activity, "需要开启GPS定位功能,点击开启去设置", new OnDialogExecuteListener() {
                @Override
                public void execute() {
                    openSettingsGPS(activity);
                }

                @Override
                public void cancel() {
                }
            }).show();
        }
        return isEnabled;
    }

    /**
     * 打开GPS设置
     */
    private static void openSettingsGPS(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                activity.startActivity(intent);
            } catch (Exception ignored) {

            }
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(String packageName) {
        //获取packagemanager
        final PackageManager packageManager = MyApplication.getContext().getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 调起百度导航
     *
     * @param lat 目的地lat
     * @param lng 目的地lng
     */
    public static void openBaiduMap(Activity activity, String lat, String lng, String name) {
        Intent intent;
        if (isAvilible("com.baidu.BaiduMap")) {//传入指定应用包名

            //                          intent = Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving®ion=西安&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");

//            intent = new Intent("intent://map/direction?" +
//                    //"origin=latlng:"+"34.264642646862,108.95108518068&" +   //起点  此处不传值默认选择当前位置
//                    "destination=latlng:" + lat + "," + lng + "|name:我的目的地" +        //终点
//                    "&mode=driving&" +          //导航路线方式
//                    "region=广州" +           //
//                    "&src=硕美科#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            intent = new Intent();
            String uri = String.format("baidumap://map/marker?location=%s,%s&title=硕美科&content=%s&traffic=on", lat, lng, name);
            intent.setData(Uri.parse(uri));
            activity.startActivity(intent); //启动调用

        } else {//未安装
            //market为路径，id为包名
            //显示手机上所有的market商店
//            Toast.makeText(context, "您尚未安装百度地图", Toast.LENGTH_LONG).show();
//            Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
//            intent = new Intent(Intent.ACTION_VIEW, uri);
//            context.startActivity(intent);
            ToastUtil.toast("您尚未安装百度地图");
        }
    }

    /**
     * 调起百度导航
     *
     * @param lat 目的地lat
     * @param lng 目的地lng
     */
    public static void openGaoDeMap(Activity activity, String lat, String lng, String name) {
        Intent intent;
        if (isAvilible("com.autonavi.minimap")) {//传入指定应用包名
            intent = new Intent();
            double[] doubles = bdToGaoDe(lat, lng);
            lat = doubles[1] + "";
            lng = doubles[0] + "";

            Log.e(TAG, "openGaoDeMap: " + lat + "---" + lng);
            String uri = String.format("androidamap://navi?sourceApplication=puxiang&poiname=%s&lat=%s&lon=%s&dev=1&style=2", name, lat, lng);
            intent.setPackage("com.autonavi.minimap");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse(uri));
            activity.startActivity(intent); //启动调用

        } else {//未安装
            //market为路径，id为包名
            //显示手机上所有的market商店
//            Toast.makeText(context, "您尚未安装百度地图", Toast.LENGTH_LONG).show();
//            Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
//            intent = new Intent(Intent.ACTION_VIEW, uri);
//            context.startActivity(intent);
            ToastUtil.toast("您尚未安装高德地图");
        }
    }

    static double PI = 3.14159265358979324 * 3000.0 / 180.0;

    /**
     * 百度地图定位经纬度转高德经纬度
     */
    public static double[] bdToGaoDe(String lat, String lon) {
        double bd_lat = Double.valueOf(lat);
        double bd_lon = Double.valueOf(lon);
        double[] gd_lat_lon = new double[2];

        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = sqrt(x * x + y * y) - 0.00002 * sin(y * PI);
        double theta = atan2(y, x) - 0.000003 * cos(x * PI);
        gd_lat_lon[0] = z * cos(theta);
        gd_lat_lon[1] = z * sin(theta);
        return gd_lat_lon;
    }

    //BD坐标转火星坐标
    public static double bdLatTogdLat(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = sqrt(x * x + y * y) - 0.00002 * sin(y * PI);
        double theta = atan2(y, x) - 0.000003 * cos(x * PI);
        return z * sin(theta);
    }

    public static double gdLongTogdLong(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = sqrt(x * x + y * y) - 0.00002 * sin(y * PI);
        double theta = atan2(y, x) - 0.000003 * cos(x * PI);
        return z * cos(theta);
    }
}
