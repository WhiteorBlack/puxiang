package com.puxiang.mall.module.seller.viewmodel;

import android.Manifest;
import android.content.Context;
import android.content.res.AssetManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.baoyz.actionsheet.ActionSheet;
import com.bigkoo.pickerview.OptionsPickerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxDealer;
import com.puxiang.mall.model.data.RxPostCity;
import com.puxiang.mall.model.data.RxUploadUrl;
import com.puxiang.mall.module.seller.view.ApplyDealerActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.DraweeViewUtils;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

import static com.puxiang.mall.utils.StringUtil.getString;

/**
 * Created by zhaoyong bai on 2017/12/19.
 */

public class DealerManagerViewModel extends BaseObservable implements ViewModel, OptionsPickerView.OnOptionsSelectListener {
    private BaseBindActivity activity;
    private List<RxPostCity> cityList = new ArrayList<>();
    private OptionsPickerView pvOptions;
    private ObservableField<RxDealer> rxDealer = new ObservableField<>(new RxDealer());
    private LoadingWindow loadingWindow;

    public int status = -1;

    public DealerManagerViewModel(BaseBindActivity activity) {
        this.activity = activity;
        initCityData();
        loadingWindow = new LoadingWindow(activity);
        loadingWindow.showWindow();
        getDealer();
    }

    /**
     * 获取单个经销商数据
     */
    private void getDealer() {
        ApiWrapper.getInstance()
                .getDealer()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> loadingWindow.hidWindow())
                .subscribe(new NetworkSubscriber<RxDealer>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(RxDealer data) {

                        /**
                         * 如果之前申请过经销商则显示审核状态
                         */
                        setRxDealer(data);
                    }
                });
    }

    private void publish() {
        verifyData();
        loadingWindow.showWindow();
        RxDealer rxDealer = getRxDealer();
        ApiWrapper.getInstance().modifyDealer(rxDealer.getDealerId(), rxDealer.getName(), rxDealer.getLinkMan(), rxDealer.getLinkPhone(), rxDealer.getIdcardFront(), rxDealer.getIdcardBack(),
                rxDealer.getProvinceCode(), rxDealer.getProvinceName(), rxDealer.getCityCode(), rxDealer.getCityName(), rxDealer.getCountyCode(), rxDealer.getCountyName(),
                rxDealer.getStreetCode(), rxDealer.getStreetName(), rxDealer.getDetailAddress())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(loadingWindow::hidWindow)
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        ToastUtil.toast("网络错误请重试");
                        loadingWindow.hidWindow();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(String data) {
                        ToastUtil.toast("信息修改成功");
                    }
                });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                activity.onBackPressed();
                break;
            case R.id.btn_step:
                publish();
                break;
            case R.id.tv_city:
                showPicker();
                break;
        }
    }

    /**
     * 判断填写的数据是否有错
     */
    private void verifyData() {
        RxDealer rxDealer = getRxDealer();
        if (TextUtils.isEmpty(rxDealer.getName())) {
            ToastUtil.toast("请输入经销商名称");
            return;
        }
        if (TextUtils.isEmpty(rxDealer.getLinkMan())) {
            ToastUtil.toast("请输入联系人名称");
            return;
        }
        if (TextUtils.isEmpty(rxDealer.getLinkPhone())) {
            ToastUtil.toast("请输入联系人电话号码");
            return;
        }

        if (TextUtils.isEmpty(rxDealer.getProvinceName())) {
            ToastUtil.toast("请选择所在城市");
            return;
        }
        if (TextUtils.isEmpty(rxDealer.getDetailAddress())) {
            ToastUtil.toast("请输入详细地址信息");
            return;
        }
    }


    @Bindable
    public RxDealer getRxDealer() {
        return rxDealer.get();
    }

    public void setRxDealer(RxDealer rxDealer) {
        this.rxDealer.set(rxDealer);
        notifyPropertyChanged(BR.rxDealer);
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
            if (cityList == null) {
                return;
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
                RxDealer rxDealer = getRxDealer();
                if (TextUtils.isEmpty(rxDealer.getProvinceName())) {
                    rxDealer.setProvinceCode(cityList.get(0).getValue());
                    rxDealer.setProvinceName(cityList.get(0).getText());
                    rxDealer.setCityCode(cityList.get(0).getChildren().get(0).getValue());
                    rxDealer.setCityName(cityList.get(0).getChildren().get(0).getText());
                    rxDealer.setCountyCode(cityList.get(0).getChildren().get(0).getChildren().get(0).getValue());
                    rxDealer.setCountyName(cityList.get(0).getChildren().get(0).getChildren().get(0).getText());
                    setRxDealer(rxDealer);
                }
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
     *
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


    @Override
    public void destroy() {
        pvOptions = null;
    }

    @Override
    public void onOptionsSelect(int options1, int options2, int options3, View v) {
        RxDealer rxDealer = getRxDealer();
        rxDealer.setProvinceCode(cityList.get(options1).getValue());
        rxDealer.setProvinceName(cityList.get(options1).getText());
        rxDealer.setCityCode(cityList.get(options1).getChildren().get(options2).getValue());
        rxDealer.setCityName(cityList.get(options1).getChildren().get(options2).getText());
        rxDealer.setCountyCode(cityList.get(options1).getChildren().get(options2).getChildren().get(options3).getValue());
        rxDealer.setCountyName(cityList.get(options1).getChildren().get(options2).getChildren().get(options3).getText());
        setRxDealer(rxDealer);
    }
}
