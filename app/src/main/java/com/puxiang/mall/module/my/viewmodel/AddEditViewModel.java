package com.puxiang.mall.module.my.viewmodel;

import android.content.Context;
import android.content.res.AssetManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.model.data.RxPostAddress;
import com.puxiang.mall.model.data.RxPostCity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2017/11/15.
 */

public class AddEditViewModel extends BaseObservable implements ViewModel, OptionsPickerView.OnOptionsSelectListener {
    private BaseBindActivity activity;
    private ObservableBoolean isAdd = new ObservableBoolean(true);
    private ObservableField<RxPostAddress> postAddress = new ObservableField<>(new RxPostAddress());
    private List<RxPostCity> cityList = new ArrayList<>();
    private OptionsPickerView pvOptions;

    public AddEditViewModel(BaseBindActivity activity) {
        this.activity = activity;
        initData();
        initCityData();
    }

    /**
     * 初始化城市选择数据
     */
    private void initCityData() {
        pvOptions = new OptionsPickerView.Builder(activity, this)
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(16)
                .build();
        new Thread(() -> {
            String JsonData = getJson(activity, "thirdcity.json");
            cityList = new Gson().fromJson(JsonData, new TypeToken<List<RxPostCity>>() {
            }.getType());
            if (cityList==null){
                return;
            }
            if (isAdd()){
                RxPostAddress rxPostAddress=new RxPostAddress();
                rxPostAddress.setProvince(cityList.get(0).getText());
                rxPostAddress.setProvinceCode(cityList.get(0).getValue());
                rxPostAddress.setCity(cityList.get(0).getChildren().get(0).getText());
                rxPostAddress.setCityCode(cityList.get(0).getChildren().get(0).getValue());
                rxPostAddress.setArea(cityList.get(0).getChildren().get(0).getChildren().get(0).getText());
                rxPostAddress.setAreaCode(cityList.get(0).getChildren().get(0).getChildren().get(0).getValue());
               setPostAddress(rxPostAddress);
            }
            List<List<RxPostCity>> citys = new ArrayList<>();
            List<List<List<RxPostCity>>> areas = new ArrayList<>();
            for (int i = 0; i < cityList.size(); i++) {
                citys.add(cityList.get(i).getChildren());
                List<List<RxPostCity>> areasChild = new ArrayList<>();
                for (int j = 0; j < cityList.get(i).getChildren().size(); j++) {
                    areasChild.add(cityList.get(i).getChildren().get(j).getChildren());

                }
                areas.add(areasChild);
            }
            if (pvOptions != null) {
                pvOptions.setPicker(cityList, citys, areas);
            }
        }).start();
    }

    public void showPicker() {
        if (pvOptions != null) {
            pvOptions.show();
        }
    }

    /**
     * 读取本地三级城市数据
     * @param context
     * @param fileName
     * @return
     */
    private String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 获取收货列表页面传过来的数据，以此判断是新增还是修改
     */
    private void initData() {
        Bundle bundle = activity.getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            setPostAddress(new Gson().fromJson(bundle.getString("json"), RxPostAddress.class));
            setAdd(false);
        }
    }

    /**
     * 删除收货地址
     */
    public void deleteAddress() {
        ApiWrapper.getInstance()
                .deleteAddress(getPostAddress().getAddressId())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                        activity.onBackPressed();
                    }
                });
    }

    /**
     * 如果当前为新增，则调用新增地址接口，如果为修改，则调用修改接口
     * @param isChecked
     */
    public void saveAddress(boolean isChecked) {
        RxPostAddress postAddress = getPostAddress();
        postAddress.setDefault(isChecked ? 1 : 0);
        varifyData(postAddress);

    }

    /**
     * 判断几个关键值是否为空
     * @param postAddress
     */
    private void varifyData(RxPostAddress postAddress) {
        if (TextUtils.isEmpty(postAddress.getShipName())) {
            ToastUtil.toast("请输入收货人姓名");
            return;
        }
        if (TextUtils.isEmpty(postAddress.getPhone())) {
            ToastUtil.toast("请输入收货人手机号");
            return;
        }
        if (TextUtils.isEmpty(postAddress.getDetailAddress())) {
            ToastUtil.toast("请输入收货地址详情");
            return;
        }
        if (isAdd()) {
            ApiWrapper.getInstance()
                    .addAddress(postAddress.getShipName(), postAddress.getDetailAddress(), postAddress.getPhone(), postAddress.getProvince(), postAddress.getProvinceCode(),
                            postAddress.getCity(), postAddress.getCityCode(), postAddress.getArea(), postAddress.getAreaCode(), postAddress.getTel(), postAddress.isDefault() + "")
                    .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new NetworkSubscriber<String>() {
                        @Override
                        public void onSuccess(String data) {
                            activity.onBackPressed();
                        }
                    });
        } else {
            ApiWrapper.getInstance()
                    .modifyAddress(postAddress.getAddressId(), postAddress.getShipName(), postAddress.getShipAddress(), postAddress.getPhone(), postAddress.getProvince(), postAddress.getProvinceCode(),
                            postAddress.getCity(), postAddress.getCityCode(), postAddress.getArea(), postAddress.getAreaCode(), postAddress.getTel(), postAddress.isDefault() + "")
                    .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new NetworkSubscriber<String>() {
                        @Override
                        public void onSuccess(String data) {
                            activity.onBackPressed();
                        }
                    });
        }
    }

    public boolean isAdd() {
        return isAdd.get();
    }

    public void setAdd(boolean add) {
        isAdd.set(add);
    }

    public void setPostAddress(RxPostAddress rxPostAddress) {
        this.postAddress.set(rxPostAddress);
        notifyPropertyChanged(BR.postAddress);
    }

    @Bindable
    public RxPostAddress getPostAddress() {
        return this.postAddress.get();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onOptionsSelect(int options1, int options2, int options3, View v) {
        postAddress.get().setProvince(cityList.get(options1).getText());
        postAddress.get().setProvinceCode(cityList.get(options1).getValue());
        postAddress.get().setCity(cityList.get(options1).getChildren().get(options2).getText());
        postAddress.get().setCityCode(cityList.get(options1).getChildren().get(options2).getValue());
        postAddress.get().setArea(cityList.get(options1).getChildren().get(options2).getChildren().get(options3).getText());
        postAddress.get().setAreaCode(cityList.get(options1).getChildren().get(options2).getChildren().get(options3).getValue());
        notifyPropertyChanged(BR.postAddress);
    }
}
