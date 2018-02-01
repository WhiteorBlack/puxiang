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

public class ApplyDealerViewModel extends BaseObservable implements ViewModel, OptionsPickerView.OnOptionsSelectListener {
    private ObservableBoolean stepTwo = new ObservableBoolean(false);
    private ObservableBoolean stepThree = new ObservableBoolean(false);
    private ObservableBoolean btnStepEnable = new ObservableBoolean(true);
    private ObservableBoolean btnBackVis = new ObservableBoolean(true);
    private ObservableBoolean isInit = new ObservableBoolean(true);
    private ObservableField<String> faceUri = new ObservableField<>();
    private ObservableField<String> backUri = new ObservableField<>();
    private ObservableField<String> stepText = new ObservableField<>("下一步");
    private ObservableField<String> backText = new ObservableField<>("上一步");
    private ApplyDealerActivity activity;
    private List<RxPostCity> cityList = new ArrayList<>();
    private OptionsPickerView pvOptions;
    private final PhotoInfo nullPhoto = new PhotoInfo();
    private FunctionConfig.Builder functionConfigBuilder;
    private List<PhotoInfo> listFace = new ArrayList<>();
    private List<PhotoInfo> listBack = new ArrayList<>();
    private static final int REQUEST_CODE_CAMERA = 1000;
    private static final int REQUEST_CODE_GALLERY = 1001;
    private int num = 0;
    private ObservableField<RxDealer> rxDealer = new ObservableField<>(new RxDealer());
    private GalleryFinal.OnHanlderResultCallback resultCallback;
    private LoadingWindow loadingWindow;

    private Map<String, String> picMap = new HashMap<>();
    private boolean uploadOK = true;
    private boolean isReady = true;
    public int status = -1;

    public ApplyDealerViewModel(ApplyDealerActivity activity) {
        this.activity = activity;
        initCityData();
        loadingWindow = new LoadingWindow(activity);
        initFunctionConfig();
        getDealer();
    }

    /**
     * 获取单个经销商数据
     */
    private void getDealer() {
        ApiWrapper.getInstance()
                .getDealer()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> setIsInit(true))
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
                        if (data != null) {
                            MyApplication.mCache.put("dealer" + MyApplication.USER_ID, "1");
                            status = data.getAuditStatus();
                            activity.setCurrentItem(2);
//                            setBtnStepEnable(data.getAuditStatus() == 3);
                            setStepTwo(true);
                            if (data.getAuditStatus() == 3) {
                                setStepText("重新申请");
                                setBtnBackVis(true);
                                setBackText("取消");
                            } else if (data.getAuditStatus() == 2) {
                                setStepText("立即进货");
                                setBackText("查看详情");
                                setBtnBackVis(true);
                            } else {
                                setStepText("返回");
                            }
                        }
                        setRxDealer(data);
                    }
                });
    }

    public void onStepClick(int num) {
        switch (num) {
            case 0:
                verifyData();
                break;
            case 1:
                verifyPicData();
                break;
            case 2:
                if (status == 2) {
                    ActivityUtil.startStockListActivity(activity);
                    activity.onBackPressed();
                    return;
                }
                if (status < 3) {
                    activity.onBackPressed();
                } else {
                    activity.setCurrentItem(0);
                }
                break;
        }
    }

    /**
     * 验证照是否为空
     */
    private void verifyPicData() {
        if (TextUtils.isEmpty(getFaceUri()) && TextUtils.isEmpty(rxDealer.get().getIdcardFront())) {
            ToastUtil.toast("请选择身份证正面照片");
            return;
        }
        if (TextUtils.isEmpty(getBackUri()) && TextUtils.isEmpty(rxDealer.get().getIdcardBack())) {
            ToastUtil.toast("请选择身份证反面照片");
            return;
        }
//        uploadImage();
        List<String> photoPathList = new ArrayList<>();
        photoPathList.add(getBackUri());
        photoPathList.add(getFaceUri());
        upload(photoPathList);
    }

    /**
     * 上传图片
     */
    public void uploadImage() {
        loadingWindow.showWindow();
        upload(checkPhotoUrl());
    }

    /**
     * 过滤已上传的图片
     *
     * @return
     */
    private List<String> checkPhotoUrl() {
        if (picMap.size() == 2) {
            uploadOK = true;
            return null;
        }
        uploadOK = false;
        List<String> photoPathList = new ArrayList<>();
        if (TextUtils.isEmpty(picMap.get(getBackUri()))) {
            photoPathList.add(getBackUri());
        }
        if (TextUtils.isEmpty(picMap.get(getFaceUri()))) {
            photoPathList.add(getFaceUri());
        }
        return photoPathList;
    }

    /**
     * 图片上传
     *
     * @param photoPathList 图片路径集合
     */
    private void upload(List<String> photoPathList) {
        if (photoPathList == null) {
            return;
        }
        Observable.fromIterable(photoPathList)
                .flatMap(new Function<String, Observable<RxUploadUrl>>() {
                    @Override
                    public Observable<RxUploadUrl> apply(@NonNull String photoPath) throws
                            Exception {
                        return rxUpload(photoPath);
                    }
                })
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    Logger.e("doFinally");
                    publish();
                })
                .subscribe(new NetworkSubscriber<RxUploadUrl>() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }

                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        uploadOK = false;
                        loadingWindow.hidWindow();
                    }

                    @Override
                    public void onSuccess(RxUploadUrl uploadUrl) {
                        Logger.e("onSuccess");
                        if (uploadUrl.getRelativeUrl() != null) {
                            String filePath = uploadUrl.getFilePath();
                            if (StringUtil.isEmpty(picMap.get(filePath))) {
                                picMap.put(filePath, uploadUrl.getRelativeUrl());
                            }
                        }
                    }
                });
    }

    private void publish() {
        if (!uploadOK) {
            return;
        }
        RxDealer rxDealer = getRxDealer();
        ApiWrapper.getInstance().becomeDealer(rxDealer.getDealerId(), rxDealer.getName(), rxDealer.getLinkMan(), rxDealer.getLinkPhone(), picMap.get(getFaceUri()), picMap.get(getBackUri()),
                rxDealer.getProvinceCode(), rxDealer.getProvinceName(), rxDealer.getCityCode(), rxDealer.getCityName(), rxDealer.getCountyCode(), rxDealer.getCountyName(),
                rxDealer.getStreetCode(), rxDealer.getStreetName(), rxDealer.getDetailAddress())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(loadingWindow::hidWindow)
                .subscribe(new NetworkSubscriber<RxDealer>() {
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
                    public void onSuccess(RxDealer data) {
                        MyApplication.mCache.put("dealer" + MyApplication.USER_ID, "1");
                        status = data.getAuditStatus();
//                        setBtnStepEnable(data.getAuditStatus() == 3);
                        setStepTwo(true);
                        if (data.getAuditStatus() == 3) {
                            setStepText("重新申请");
                            setBtnBackVis(true);
                            setBackText("取消");
                        } else if (data.getAuditStatus() == 0) {
                            setStepText("立即进货");
                            setBackText("查看详情");
                            setBtnBackVis(true);
                        } else {
                            setStepText("返回");
                        }
                        setBtnBackVis(false);
                        setRxDealer(data);
                        activity.setCurrentItem(2);
                    }
                });
    }

    /**
     * 图片进行压缩并进行请求包装
     *
     * @param path
     * @return
     */
    @Nullable
    private Observable<RxUploadUrl> rxUpload(final String path) {
        if (StringUtil.isEmpty(path) || picMap.get(path) != null) {
            return null;
        }
        return ApiWrapper.getInstance().setUpload(path);
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
        activity.setCurrentItem(1);
    }

    @Bindable
    public String getBackText() {
        return backText.get();
    }

    public void setBackText(String backText) {
        this.backText.set(backText);
        notifyPropertyChanged(BR.backText);
    }

    @Bindable
    public String getStepText() {
        return stepText.get();
    }

    public void setStepText(String stepText) {
        this.stepText.set(stepText);
        notifyPropertyChanged(BR.stepText);
    }

    @Bindable
    public boolean getBtnBackVis() {
        return btnBackVis.get();
    }

    public void setBtnBackVis(boolean btnBackVis) {
        this.btnBackVis.set(btnBackVis);
        notifyPropertyChanged(BR.btnBackVis);
    }

    @Bindable
    public RxDealer getRxDealer() {
        return rxDealer.get();
    }

    public void setRxDealer(RxDealer rxDealer) {
        this.rxDealer.set(rxDealer);
        notifyPropertyChanged(BR.rxDealer);
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

    public void delFace() {
        listFace.clear();
        setFaceUri("");
    }

    public void delBack() {
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
