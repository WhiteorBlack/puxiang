package com.puxiang.mall.module.shop.viewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.model.data.RxArea;
import com.puxiang.mall.model.data.RxCity;
import com.puxiang.mall.module.shop.adapter.CityAdapter;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.utils.ACache;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2017/10/24.
 */

public class SelectCityViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private CityAdapter adapter;
    private List<RxCity> cityList = new ArrayList<>();
    private ObservableField<String> currentCity = new ObservableField<>("");

    public SelectCityViewModel(BaseBindActivity activity, CityAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        String city = activity.getIntent().getStringExtra("city");
        setCurrentCity(city);
        this.adapter.setOnItemClickListener((adapter1, view, position) -> {
            RxCity rxCity = (RxCity) adapter1.getData().get(position);
            setCurrentCity(rxCity.getName());
            dealLatestData(rxCity);
        });
    }

    /**
     * 把选择信息放到缓存中
     */
    private List<RxArea> areaList = new ArrayList<>();

    private void dealLatestData(RxCity rxCity) {

        MyApplication.mCache.getAsListBean(CacheKey.LATESTCITY, RxArea[].class, rxAreas -> {
            if (rxAreas!=null)
            {
                areaList.addAll(rxAreas);
            }
            RxArea rxArea = new RxArea();
            rxArea.setIsVisiable(true);
            rxArea.setAreaName(rxCity.getName());
            rxArea.setAreaCode(rxCity.getCode());
            if (areaList.size() > 0) {
                for (int i = 0; i < areaList.size(); i++) {
                    areaList.get(i).setIsVisiable(false);
                    if (TextUtils.equals(rxCity.getName(), areaList.get(i).getName())) {
                        areaList.remove(i);
                        break;
                    }
                }
            }

            areaList.add(0, rxArea);

            if (areaList.size() > 3) {
                areaList = areaList.subList(0, 3);

            }
            MyApplication.mCache.put(CacheKey.LATESTCITY, areaList);
        });

    }


    private void readJson() {
        try {
            InputStream is = activity.getAssets().open("city.json");//此处为要加载的json文件名称
            String text = readTextFromSDcard(is);
            handleCitiesResponse(text);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("readFromAssets", e.toString());
        }
    }

    //将传入的is一行一行解析读取出来出来
    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is, "GB2312");
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();//把读取的数据返回
    }

    //把读取出来的json数据进行解析
    public boolean handleCitiesResponse(String response) {
        try {
            cityList = new Gson().fromJson(response, new TypeToken<List<RxCity>>() {
            }.getType());
            if (cityList != null && cityList.size() > 0) {
                adapter.setNewData(cityList);
            }
            return true;
        } catch (Exception e) {
            Log.d("handleCitiesResponse", e.toString());
        }
        return false;
    }

    public void setCurrentCity(String city) {
        this.currentCity.set(city);
        notifyPropertyChanged(BR.currentCity);
    }

    @Bindable
    public String getCurrentCity() {
        return currentCity.get();
    }

    @Override
    public void destroy() {

    }
}
