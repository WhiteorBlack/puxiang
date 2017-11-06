package com.puxiang.mall.module.shop.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.flyco.animation.SlideEnter.SlideTopEnter;
import com.flyco.animation.SlideExit.SlideTopExit;
import com.flyco.dialog.widget.base.BaseDialog;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.DialogCityAreaBinding;
import com.puxiang.mall.module.shop.adapter.CityListAdapter;
import com.puxiang.mall.module.shop.adapter.CityListLeftAdapter;
import com.puxiang.mall.module.shop.viewModel.CityViewModel;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.ScreenUtil;

/**
 * Created by zhaoyong bai on 2017/10/13.
 */

public class CityDialog extends BaseDialog<CityDialog> {

    private DialogCityAreaBinding binding;
    private Context context;
    private CityViewModel viewModel;
    private CityListAdapter adapter;
    private CityListLeftAdapter leftAdapter;
    private String areaCode = "";

    public CityDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CityDialog(Context context, boolean isPopupStyle) {
        super(context, isPopupStyle);
        this.context = context;
    }

    public void resizeHeight(int height){
        heightScale((MyApplication.heightPixels-height*1.0f)/MyApplication.heightPixels);
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public View onCreateView() {
//        heightScale(ScreenUtil.dip2px(456.0f)*0.5f / MyApplication.heightPixels);
        showAnim(new SlideTopEnter());
        dismissAnim(new SlideTopExit());
        getWindow().setDimAmount(0f);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_city_area, null, false);
        adapter = new CityListAdapter(R.layout.city_list_item, null);
        leftAdapter = new CityListLeftAdapter(R.layout.city_list_left_item, null);
        viewModel = new CityViewModel(adapter, leftAdapter, this);
        binding.setViewModel(viewModel);
        AutoUtils.auto(binding.getRoot());

        return binding.getRoot();
    }

    @Override
    public void setUiBeforShow() {
        initRecyView();
        viewModel.getLocation(areaCode);
    }

    private void initRecyView() {
        binding.rvShop.setLayoutManager(new LinearLayoutManager(context));
        binding.rvShop.setAdapter(adapter);
        binding.rvShop.addOnItemTouchListener(viewModel.areaListener());
        binding.rvLeft.setLayoutManager(new LinearLayoutManager(context));
        binding.rvLeft.setAdapter(leftAdapter);
        binding.rvLeft.addOnItemTouchListener(viewModel.cityListener());
    }

}
