package com.puxiang.mall.module.shop.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.databinding.ActivitySelectCityBinding;
import com.puxiang.mall.databinding.ViewCityHeadBinding;
import com.puxiang.mall.model.data.RxArea;
import com.puxiang.mall.model.data.RxCity;
import com.puxiang.mall.module.shop.adapter.CityAdapter;
import com.puxiang.mall.module.shop.adapter.HotCityAdapter;
import com.puxiang.mall.module.shop.viewModel.CityHeadViewModel;
import com.puxiang.mall.module.shop.viewModel.SelectCityViewModel;
import com.puxiang.mall.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2017/10/24.
 */

public class SelectCityActivity extends BaseBindActivity implements TextView.OnEditorActionListener {
    private ActivitySelectCityBinding binding;
    private SelectCityViewModel viewModel;
    private CityAdapter adapter;
    private HotCityAdapter latestAdapter;
    private HotCityAdapter hotAdapter;
    private List<RxCity> cityList = new ArrayList<>();
    private LinearLayoutManager mManager;
    private SuspensionDecoration mDecoration;
    private ViewCityHeadBinding headBinding;
    private CityHeadViewModel headViewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_city);
        headBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.view_city_head, null, false);
        adapter = new CityAdapter(R.layout.item_city);
        latestAdapter = new HotCityAdapter(R.layout.item_latest);
        hotAdapter = new HotCityAdapter(R.layout.item_latest);
        viewModel = new SelectCityViewModel(this, adapter);
        binding.setViewModel(viewModel);
        AutoUtils.auto(headBinding.getRoot());
        headViewModel = new CityHeadViewModel(this, hotAdapter, latestAdapter);
        headBinding.setHeadModel(headViewModel);
        adapter.addHeaderView(headBinding.getRoot());
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            setCurrentCity(cityList.get(position).getName());
            dealLatestData(cityList.get(position));
            RxArea rxArea=new RxArea();
            rxArea.setAreaCode(cityList.get(position).getCode());
            rxArea.setAreaName(cityList.get(position).getName());
            rxArea.setIsVisiable(true);
            postCity(rxArea);
            onBackPressed();
        });
    }

    private void postCity(RxArea rxCity) {
        EventBus.getDefault().post(rxCity);
    }


    @Override
    public void initView() {

        binding.rv.setLayoutManager(mManager = new LinearLayoutManager(this));
        binding.rv.setAdapter(adapter);
        binding.rv.addItemDecoration(mDecoration = new SuspensionDecoration(this, cityList).setLeftPadding(80));
        binding.indexBar.setmPressedShowTextView(binding.tvSideBarHint)
                .setNeedRealIndex(true)
                .setmLayoutManager(mManager);

        headBinding.rvHot.setLayoutManager(new GridLayoutManager(this, 3));
        headBinding.rvHot.setAdapter(hotAdapter);
        headBinding.rvLatest.setLayoutManager(new GridLayoutManager(this, 3));
        headBinding.rvLatest.setAdapter(latestAdapter);
        binding.toolbar.et.setOnEditorActionListener(this);
        getCityData();
    }

    /**
     * 把选择信息放到缓存中
     */
    private List<RxArea> areaList = new ArrayList<>();

    private void dealLatestData(RxCity rxCity) {

        MyApplication.mCache.getAsListBean(CacheKey.LATESTCITY, RxArea[].class, rxAreas -> {
            areaList.addAll(rxAreas);
        });
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
    }

    private void getCityData() {
        new Thread(() -> readJson()).start();
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void readJson() {
        try {
            InputStream is = getAssets().open("city.json");//此处为要加载的json文件名称
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
            binding.indexBar.getDataHelper().sortSourceDatas(cityList);
            cityList.get(0).setBaseIndexTag("↑");
            cityList.get(0).setTop(true);
            runOnUiThread(() -> {
                if (cityList != null && cityList.size() > 0) {
                    adapter.setNewData(cityList);
                    binding.indexBar.setmSourceDatas(cityList).invalidate();
                    mDecoration.setmDatas(cityList);
                }
            });

            return true;
        } catch (Exception e) {
            Log.d("handleCitiesResponse", e.toString());
        }
        return false;
    }

    public void setCurrentCity(String city) {
        viewModel.setCurrentCity(city);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
        headViewModel.destroy();
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            // 当按了搜索之后关闭软键盘
            closeInput();
            String city = binding.toolbar.et.getText().toString();
            if (!TextUtils.isEmpty(city)) {
                searchCity(city);
            }
            return true;
        }
        return false;
    }

    private void searchCity(String city) {
        if (cityList != null && cityList.size() > 0) {
            for (int i = 0; i < cityList.size(); i++) {
                if (cityList.get(i).getName().contains(city)) {
                    mManager.scrollToPositionWithOffset(i,0);
                    Logger.e("pos  " + i);
                    break;
                }
            }
        }
    }


}
