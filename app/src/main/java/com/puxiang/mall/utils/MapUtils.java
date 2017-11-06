package com.puxiang.mall.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.orhanobut.logger.Logger;

import java.net.URISyntaxException;

/**
 * Created by zhaoyong bai on 2017/11/1.
 */

public class MapUtils {
    /**
     * 检测是否安装该应用
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalledApp(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

        return packageInfo != null;
    }

    /**
     * 拉起百度地图导航功能
     *
     * @param activity
     * @param disName
     * @param lat
     * @param lng
     */
    public static void openBaiDuMap(Activity activity, String disName, String lat, String lng) {
//        Intent intent = new Intent();
//        try {
//           intent.setData(Uri.parse("baidumap://map/direction?region="+city+"&origin="+lat+","+lng +"&destination="+disName
//                   +"&mode=driviing"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            StringBuilder loc = new StringBuilder();
            loc.append("intent://map/direction?origin=latlng:");
            loc.append(lat);
            loc.append(",");
            loc.append(lng);
            loc.append("|name:");
            loc.append("我的位置");
            loc.append("&destination=latlng:");
            loc.append(lat);
            loc.append(",");
            loc.append(lng);
            loc.append("|name:");
            loc.append(disName);
            loc.append("&mode=driving");
            loc.append("&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            Intent intent = Intent.getIntent(loc.toString());
            activity.startActivity(intent); //启动调用
        } catch (Exception e) {

        }
    }

    /**
     * 拉起高德地图
     *
     * @param lng
     * @param lat
     * @param describle
     * @param context
     */
    public static void openGaode(Context context, String describle, String lat, String lng) {
        // TODO Auto-generated method stub

        try {
            Uri uri = Uri.parse("androidamap://navi?sourceApplication=蒲象&poiname="+describle+"&lat=" + lat + "&lon=" + lng + "&dev=1&style=2");
            Logger.e("gaode==="+uri);
            context.startActivity(new Intent("android.intent.action.VIEW",uri).setPackage("com.autonavi.minimap"));
        } catch (Exception e) {

        }
//        try {
//            StringBuilder loc = new StringBuilder();
//            loc.append("androidamap://viewMap?sourceApplication=XX");
//            loc.append("&poiname=");
//            loc.append(describle);
//            loc.append("&lat=");
//            loc.append(lng);
//            loc.append("&lon=");
//            loc.append(lat);
//            loc.append("&dev=0");
//            Intent intent = Intent.getIntent(loc.toString());
//            context.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
