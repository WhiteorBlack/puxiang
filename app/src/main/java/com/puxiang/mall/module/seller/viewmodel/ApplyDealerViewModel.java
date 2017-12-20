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
import android.net.Uri;
import android.view.View;

import com.baoyz.actionsheet.ActionSheet;
import com.bigkoo.pickerview.OptionsPickerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.RxPostAddress;
import com.puxiang.mall.model.data.RxPostCity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.utils.DraweeViewUtils;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import static com.puxiang.mall.utils.StringUtil.getString;

/**
 * Created by zhaoyong bai on 2017/12/19.
 */

public class ApplyDealerViewModel extends BaseObservable implements ViewModel, OptionsPickerView.OnOptionsSelectListener {
    private ObservableBoolean stepTwo = new ObservableBoolean(false);
    private ObservableBoolean stepThree = new ObservableBoolean(false);
    private ObservableBoolean btnStepEnable = new ObservableBoolean(true);
    private ObservableBoolean isInit = new ObservableBoolean(true);
    private ObservableField<String> faceUri = new ObservableField<>();
    private ObservableField<String> backUri = new ObservableField<>();
    private BaseBindActivity activity;
    private List<RxPostCity> cityList = new ArrayList<>();
    private OptionsPickerView pvOptions;
    private final PhotoInfo nullPhoto = new PhotoInfo();
    private FunctionConfig.Builder functionConfigBuilder;
    private List<PhotoInfo> listFace = new ArrayList<>();
    private List<PhotoInfo> listBack = new ArrayList<>();
    private static final int REQUEST_CODE_CAMERA = 1000;
    private static final int REQUEST_CODE_GALLERY = 1001;
    private int num = 0;

    private GalleryFinal.OnHanlderResultCallback resultCallback;

    public ApplyDealerViewModel(BaseBindActivity activity) {
        this.activity = activity;
        initCityData();
        initFunctionConfig();
    }

    @Bindable
    public String getFaceUri() {
        return faceUri.get();
    }

    public void setFaceUri(String faceUri) {
        this.faceUri.set(faceUri);
        notifyPropertyChanged(BR.faceUri);
    }

    @Bindable
    public String getBackUri() {
        return backUri.get();
    }

    public void setBackUri(String backUri) {
        this.backUri.set(backUri);
        notifyPropertyChanged(BR.backUri);
    }

    @Bindable
    public boolean getBtnStepEnable() {
        return btnStepEnable.get();
    }

    public void setBtnStepEnable(boolean btnStepEnable) {
        this.btnStepEnable.set(btnStepEnable);
        notifyPropertyChanged(BR.btnStepEnable);
    }

    @Bindable
    public boolean getStepTwo() {
        return stepTwo.get();
    }

    public void setStepTwo(boolean stepTwo) {
        this.stepTwo.set(stepTwo);
        notifyPropertyChanged(BR.stepTwo);
    }

    @Bindable
    public boolean getStepThree() {
        return stepThree.get();
    }

    public void setStepThree(boolean stepThree) {
        this.stepThree.set(stepThree);
        notifyPropertyChanged(BR.stepThree);
    }

    public boolean getIsInit() {
        return isInit.get();
    }

    public void setIsInit(boolean isInit) {
        this.isInit.set(isInit);
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

    /**
     * 获取相册图片
     * num=0 为正面照片
     */
    public void getPic(int num) {
        this.num = num;
        ActionSheet.createBuilder(activity, activity.getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        FunctionConfig functionConfig;
                        if (num == 0) {
                            functionConfigBuilder.setSelected(listFace);
                        } else {
                            functionConfigBuilder.setSelected(listBack);
                        }
                        functionConfig = functionConfigBuilder.build();
                        switch (index) {
                            case 0:
                                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY,
                                        functionConfig, resultCallback);
                                break;
                            case 1:
                                EasyPermission.with(activity)
                                        .rationale(getString(R.string.rationale_camera))
                                        .addRequestCode(PermissionCode.RG_CAMERA_PERM)
                                        .permissions(Manifest.permission.CAMERA)
                                        .request();
//                                 GalleryFinal.openCamera(REQUEST_CODE_CAMERA, GalleryFinal
//                                 .getCoreConfig().getFunctionConfig());
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        FunctionConfig functionConfig = functionConfigBuilder.build();
        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, resultCallback);
    }

    /**
     * 初始化图片选择器
     */
    private void initFunctionConfig() {
        if (functionConfigBuilder == null) {
            functionConfigBuilder = new FunctionConfig.Builder()
                    .setMutiSelectMaxSize(1)
                    .setEnableCamera(false)
                    .setEnablePreview(true);
        }
        resultCallback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null) {
                    if (num == 0) {
                        listFace.clear();
                        listFace.addAll(resultList);
                        setFaceUri(listFace.get(0).getPhotoPath());
                    } else {
                        listBack.clear();
                        listBack.addAll(resultList);
                        setBackUri(listBack.get(0).getPhotoPath());
                    }
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                ToastUtil.toast(errorMsg);
            }
        };
    }

    @BindingAdapter("face")
    public static void setFace(SimpleDraweeView faceView, String uri) {

        faceView.setImageURI(DraweeViewUtils.getUriPath(uri));
    }

    @BindingAdapter("back")
    public static void setBack(SimpleDraweeView faceView, String uri) {
        faceView.setImageURI(DraweeViewUtils.getUriPath(uri));
    }

    public void delFace(){
        listFace.clear();
        setFaceUri("");
    }

    public void delBack(){
        listBack.clear();
        setBackUri("");
    }
    @Override
    public void destroy() {
        resultCallback = null;
        GalleryFinal.clearCallback();
        GalleryFinal.cleanCacheFile();
    }

    @Override
    public void onOptionsSelect(int options1, int options2, int options3, View v) {

    }
}
