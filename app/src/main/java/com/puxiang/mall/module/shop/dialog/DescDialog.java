package com.puxiang.mall.module.shop.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.flyco.animation.SlideEnter.SlideTopEnter;
import com.flyco.animation.SlideExit.SlideTopExit;
import com.flyco.dialog.widget.base.BaseDialog;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.DialogCityAreaBinding;
import com.puxiang.mall.databinding.DialogDescBinding;
import com.puxiang.mall.module.shop.adapter.CityListAdapter;
import com.puxiang.mall.module.shop.adapter.CityListLeftAdapter;
import com.puxiang.mall.module.shop.adapter.DescListAdapter;
import com.puxiang.mall.module.shop.viewModel.CityViewModel;
import com.puxiang.mall.module.shop.viewModel.DescViewModel;
import com.puxiang.mall.utils.AutoUtils;

/**
 * Created by zhaoyong bai on 2017/10/13.
 */

public class DescDialog extends BaseDialog<DescDialog> {

    private DialogDescBinding binding;
    private Context context;
    private DescViewModel viewModel;
    private DescListAdapter adapter;

    public DescDialog(Context context) {
        super(context);
        this.context = context;
    }

    public DescDialog(Context context, boolean isPopupStyle) {
        super(context, isPopupStyle);
        this.context = context;
    }

    public void resizeHeight(int height){
        heightScale((MyApplication.heightPixels-height*1.0f)/MyApplication.heightPixels);
    }

    @Override
    public View onCreateView() {
//        heightScale(0f);
        showAnim(new SlideTopEnter());
        dismissAnim(new SlideTopExit());
        getWindow().setDimAmount(0f);
        adapter = new DescListAdapter(R.layout.item_shop_desc, null);
        viewModel = new DescViewModel(adapter, this);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_desc, null, false);
        binding.setViewModel(viewModel);
        AutoUtils.auto(binding.getRoot());
        binding.rvDesc.setLayoutManager(new LinearLayoutManager(context));
        binding.rvDesc.setAdapter(adapter);
        binding.rvDesc.addOnItemTouchListener(viewModel.onItemTouchListener());
        return binding.getRoot();
    }

    @Override
    public void setUiBeforShow() {
        viewModel.getDescData();
    }


}
