package com.puxiang.mall.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.flyco.animation.SlideEnter.SlideTopEnter;
import com.flyco.animation.ZoomExit.ZoomOutExit;
import com.flyco.dialog.widget.base.BaseDialog;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.DialogMapBinding;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.location.LocationUtil;

import static com.puxiang.mall.utils.location.LocationUtil.isAvilible;

public class MapDialog extends BaseDialog<MapDialog> {


    private DialogMapBinding binding;
    private static final int MAP_TYPE_BAIDU = 1000;
    private static final int MAP_TYPE_GAODE = 1001;
    private int mapTyep = 0;
    private final String lat;
    private final String lng;
    private final String name;

    public MapDialog(Context context, String lat, String lng, String name) {
        super(context, true);
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        showAnim(new SlideTopEnter());
        dismissAnim(new ZoomOutExit());

        binding = DataBindingUtil
                .inflate(LayoutInflater.from(mContext), R.layout.dialog_map, null, false);
        AutoUtils.auto(binding.getRoot());
        binding.setBaidu(isAvilible("com.baidu.BaiduMap"));
        binding.setGaode(isAvilible("com.autonavi.minimap"));

        return binding.getRoot();
    }

    @Override
    public void setUiBeforShow() {
        binding.btnBaidu.setOnClickListener(v -> {
            mapTyep = MAP_TYPE_BAIDU;
            dismiss();
        });
        binding.btnGaode.setOnClickListener(v -> {
            mapTyep = MAP_TYPE_GAODE;
            dismiss();
        });
        binding.btnNone.setOnClickListener(v -> {
            mapTyep = 0;
            dismiss();
        });
    }

    @Override
    public void superDismiss() {
        super.superDismiss();
        switch (mapTyep) {
            case MAP_TYPE_BAIDU:
                LocationUtil.openBaiduMap(((Activity) mContext), lat, lng, name);
                break;
            case MAP_TYPE_GAODE:
                LocationUtil.openGaoDeMap(((Activity) mContext), lat, lng, name);
                break;
        }
    }
}