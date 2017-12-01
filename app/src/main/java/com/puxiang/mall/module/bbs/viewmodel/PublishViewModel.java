package com.puxiang.mall.module.bbs.viewmodel;

import android.Manifest;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.baoyz.actionsheet.ActionSheet;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxUploadUrl;
import com.puxiang.mall.module.bbs.adapter.PublishAdapter;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.FileUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;
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
import io.rong.imkit.plugin.image.PictureSelectorActivity;

import static com.puxiang.mall.utils.StringUtil.getString;


public class PublishViewModel implements ViewModel {
    private static final int REQUEST_CODE_CAMERA = 1000;
    private static final int REQUEST_CODE_GALLERY = 1001;
    private final BaseBindActivity activity;
    private final PublishAdapter adapter;
    private final LoadingWindow loadingWindow;
    private final PhotoInfo nullPhoto = new PhotoInfo();
    private FunctionConfig.Builder functionConfigBuilder;
    private List<PhotoInfo> list = new ArrayList<>();
    private GalleryFinal.OnHanlderResultCallback resultCallback;
    private boolean isMax;
    private boolean uploadOK = true;

    public String plateId;
    private String detailId = "";
    private Map<String, String> picMap = new HashMap<>();
    public static final int SELECT_PLATE_CODE = 5000;

    public String content = "";
    public ObservableBoolean isPublishing = new ObservableBoolean();
    private boolean isHasNew = false;
    public ObservableField<String> plateName = new ObservableField<>(getString(R.string
            .publish_to_select_plate));
    private boolean isReady = true;

    public PublishViewModel(BaseBindActivity activity, PublishAdapter adapter) {
        initData(activity);
        this.activity = activity;
        this.adapter = adapter;
        initAdapterData(adapter);
        initFunctionConfig();
        loadingWindow = new LoadingWindow(activity);
    }

    /**
     * 初始化图片展示Adapter
     *
     * @param adapter
     */
    private void initAdapterData(PublishAdapter adapter) {
        list.add(nullPhoto);
        adapter.setNewData(list);
    }

    /**
     * 初始化 选择的圈子
     *
     * @param activity
     */
    private void initData(BaseBindActivity activity) {
        plateId = activity.getIntent().getStringExtra(Config.PLATE_ID);
        detailId = activity.getIntent().getStringExtra(Config.DETAIL_ID);
        String plateNameStr = activity.getIntent().getStringExtra(Config.PLATE_NAME);
        if (!StringUtil.isEmpty(plateNameStr)) {
            plateName.set("发布到：" + plateNameStr);
        }
    }

    public void setPicList(List<Uri> picList) {
        if (picList != null && picList.size() > 0) {
            for (int i = 0; i < picList.size(); i++) {
                PhotoInfo photoInfo = new PhotoInfo();
                photoInfo.setPhotoUri(picList.get(i));
                list.add(0,photoInfo);
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取相册图片
     */
    private void getPic() {
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
                        functionConfigBuilder.setSelected(list);
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
        functionConfigBuilder.setSelected(list);
        FunctionConfig functionConfig = functionConfigBuilder.build();
        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, resultCallback);
    }

    /**
     * 初始化图片选择器
     */
    private void initFunctionConfig() {
        if (functionConfigBuilder == null) {
            functionConfigBuilder = new FunctionConfig.Builder()
                    .setMutiSelectMaxSize(9)
                    .setSelected(list)
                    .setEnableCamera(false)
                    .setEnablePreview(true);
        }
        resultCallback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null) {
                    if (reqeustCode == REQUEST_CODE_CAMERA) {
                        int index = list.size() - 1;
                        list.add(index > 0 ? index : 0, resultList.get(0));
                        if (list.size() < 10) {
                            isMax = false;
                        } else {
                            list.remove(list.size() - 1);
                            isMax = true;
                        }
                    } else {
                        list.clear();
                        list.addAll(resultList);
                        if (resultList.size() < 9) {
                            list.add(nullPhoto);
                            isMax = false;
                        } else {
                            isMax = true;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    isHasNew = true;
                    if (isReady) {
//                        uploadImage();
                    }
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                ToastUtil.toast(errorMsg);
            }
        };
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
        if (!isHasNew) return null;
        uploadOK = false;
        isReady = false;
        isHasNew = false;
        List<String> photoPathList = new ArrayList<>();
        int size = isMax ? list.size() : list.size() - 1;
        for (int i = 0; i < size; i++) {
            String photoPath = list.get(i).getPhotoPath();
            if (!TextUtils.isEmpty(photoPath)) {
                photoPathList.add(photoPath);
            }
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
            publish();
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
                    isReady = true;
                    uploadImage();
                })
                .subscribe(new NetworkSubscriber<RxUploadUrl>() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (!isHasNew) {
                            uploadOK = true;
                            if (isPublishing.get()) {
                                publish();
                            }
                        }
                    }

                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        uploadOK = false;
                        isPublishing.set(false);
                        loadingWindow.hidWindow();
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(RxUploadUrl uploadUrl) {
                        if (uploadUrl.getRelativeUrl() != null) {
                            String filePath = uploadUrl.getFilePath();
                            if (StringUtil.isEmpty(picMap.get(filePath))) {
                                picMap.put(filePath, uploadUrl.getRelativeUrl());
                            }
                        }
                    }
                });
    }

    private StringBuilder urlSb = new StringBuilder("");

    /**
     * 发帖请求
     */
    public void publish() {
        isPublishing.set(true);


        if (!uploadOK) {
            return;
        }

        if (!StringUtil.isEmpty(detailId)) {
            EventBus.getDefault().post(Event.WEB_BACK);
        }

        if (list.size() > 1) {
            if (isMax) {
                for (PhotoInfo photoInfo : list) {
                    String url = picMap.get(photoInfo.getPhotoPath());
                    urlSb.append(url).append(",");
                }
            } else {
                int photoCount = list.size() - 1;
                for (int i = 0; i < photoCount; i++) {
                    String url = picMap.get(list.get(i).getPhotoPath());
                    urlSb.append(url).append(",");
                }
            }
            urlSb.deleteCharAt(urlSb.length() - 1);
        }

        String picUrls = urlSb.toString();

        ApiWrapper.getInstance()
                .publish(picUrls, detailId, plateId, content)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> {
                    loadingWindow.hidWindow();
                    isPublishing.set(false);
                })
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {
                        EventBus.getDefault().post(Event.RELOAD_PLATE);
                        if (!StringUtil.isEmpty(detailId)) {
                            EventBus.getDefault().post(Event.KILL_WEB);
                        }
                        ActivityUtil.startPlatePostActivity(activity, plateId);
                        activity.onBackPressed();
                    }
                });
    }


    /**
     * 分派任务
     */
    private void allot() {
        if (!isHasNew) return;
        uploadOK = false;
        isReady = false;
        isHasNew = false;
        final List<Observable<RxUploadUrl>> obList = new ArrayList<>();
        for (PhotoInfo path : list) {
            Observable<RxUploadUrl> observable = rxUpload(path.getPhotoPath());
            if (observable != null) {
                obList.add(observable);
            }
        }
        merge(obList);
    }

    /**
     * 合并任务
     *
     * @param observables
     */
    private void merge(List<Observable<RxUploadUrl>> observables) {
        Observable.merge(observables)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    isReady = true;
                    allot();
                })
                .subscribe(new NetworkSubscriber<RxUploadUrl>() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (!isHasNew) {
                            uploadOK = true;
                            if (isPublishing.get()) {
                                publish();
                            }
                        }
                    }

                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        uploadOK = false;
                        isPublishing.set(false);
                        loadingWindow.hidWindow();
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(RxUploadUrl uploadUrl) {
                        if (uploadUrl.getRelativeUrl() != null) {
                            String filePath = uploadUrl.getFilePath();
                            if (picMap.get(filePath) == null) {
                                picMap.put(filePath, uploadUrl.getRelativeUrl());
                            }
                        }
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

    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int
                    i) {
                switch (view.getId()) {
                    case R.id.publish_item_lose:
                        if (isMax) {
                            list.add(nullPhoto);
                            isMax = false;
                        }
                        baseQuickAdapter.remove(i);
                        break;
                    case R.id.sdv_item_pic:
                        String photoPath = ((PhotoInfo) baseQuickAdapter.getData().get(i)).getPhotoPath();
                        if (TextUtils.isEmpty(photoPath)) {
                            getPic();
//                            Intent intent = new Intent(activity, PictureSelectorActivity.class);
//                            activity.startActivityForResult(intent, Config.PIC_REQUEST);
                        }
                        break;
                }
            }
        };
    }

    @Override
    public void destroy() {
        WeakReference<GalleryFinal.OnHanlderResultCallback> wrf = new WeakReference<>(
                resultCallback);
        WeakReference<GalleryFinal.OnHanlderResultCallback> wr = new WeakReference<>(
                GalleryFinal.getCallback());
        resultCallback = null;
        loadingWindow.dismiss();
        GalleryFinal.clearCallback();
        GalleryFinal.cleanCacheFile();
    }
}
