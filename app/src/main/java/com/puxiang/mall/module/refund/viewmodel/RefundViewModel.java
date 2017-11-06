package com.puxiang.mall.module.refund.viewmodel;

import android.Manifest;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baoyz.actionsheet.ActionSheet;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.flyco.animation.BounceEnter.BounceBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.widget.NormalListDialog;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxRefund;
import com.puxiang.mall.model.data.RxRefundReasons;
import com.puxiang.mall.model.data.RxUploadUrl;
import com.puxiang.mall.module.bbs.adapter.PublishAdapter;
import com.puxiang.mall.module.refund.view.RefundActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

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


public class RefundViewModel implements ViewModel {
    private static final int REQUEST_CODE_CAMERA = 1000;
    private static final int REQUEST_CODE_GALLERY = 1001;
    private final RefundActivity activity;
    private final PublishAdapter adapter;
    private final LoadingWindow loadingWindow;
    private final PhotoInfo nullPhoto = new PhotoInfo();
    private FunctionConfig.Builder functionConfigBuilder;
    public List<PhotoInfo> list = new ArrayList<>();
    private GalleryFinal.OnHanlderResultCallback resultCallback;
    private boolean isMax;
    private boolean uploadOK;

    private Map<String, String> picMap = new HashMap<>();

    public String content = "";
    public ObservableBoolean isPublishing = new ObservableBoolean();
    public ObservableBoolean isPayed = new ObservableBoolean();
    public ObservableBoolean isRefundMoney = new ObservableBoolean(true);
    public ObservableField<RxRefund> refundBean = new ObservableField<>();
    public ObservableField<String> reasons = new ObservableField<>("请选择退款原因");
    private String productId;
    public int refundReasonId = -1;
    private String orderDetailId;
    public NormalListDialog dialog;
    private boolean isReady = true;
    private boolean isHasNew = false;
    private ActionSheet.Builder picDialog;

    public RefundViewModel(RefundActivity activity, PublishAdapter adapter) {
        this.activity = activity;
        loadingWindow = new LoadingWindow(activity);
        this.adapter = adapter;
        initData();
        list.add(nullPhoto);
        adapter.setNewData(list);
        initFunctionConfig();
        getRefundData();
    }

    private void initData() {
        productId = activity.getIntent().getStringExtra("productId");
        orderDetailId = activity.getIntent().getStringExtra("orderDetailId");
        activity.setPageTag("orderDetailId:", orderDetailId);
    }

    /**
     * 获取退款数据
     */
    private void getRefundData() {
        ApiWrapper.getInstance()
                .refund(orderDetailId)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxRefund>() {
                    @Override
                    public void onSuccess(RxRefund bean) {
                        setRefundData(bean);
                    }
                });
    }

    /**
     * 设置退款数据
     *
     * @param bean 退款数据
     */
    private void setRefundData(RxRefund bean) {
        refundBean.set(bean);
        if (bean.getOrderStatus().equals("payed")) {
            isPayed.set(true);
        } else {
            isPayed.set(false);
        }
        ArrayList<DialogMenuItem> menuItems = new ArrayList<>();
        List<RxRefundReasons> refundReasons = bean.getRefundReasons();
        for (RxRefundReasons refundReasonsBean : refundReasons) {
            String des = refundReasonsBean.getRefundReasonDes();
            DialogMenuItem customBaseItem = new DialogMenuItem(des, 0);
            menuItems.add(customBaseItem);
        }
        dialog = new NormalListDialog(activity, menuItems);
        dialog.title("请选择")//
                .titleBgColor(AppUtil.getColor(R.color.theme))
                .showAnim(new BounceBottomEnter())//
                .dismissAnim(new SlideBottomExit());//
        dialog.setOnOperItemClickL((parent, view, position, id) -> {
            RxRefundReasons reasonsBean = refundReasons.get(position);
            reasons.set(reasonsBean.getRefundReasonDes());
            refundReasonId = reasonsBean.getRefundReasonId();
            dialog.dismiss();
        });
    }

    /**
     * 获取手机图片
     */
    private void getPic() {
        if (picDialog == null) {
            picDialog = ActionSheet.createBuilder(activity, activity.getSupportFragmentManager())
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
                                case 0: //获取相册图片
                                    GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY,
                                            functionConfig, resultCallback);
                                    break;
                                case 1: //检测打开相机权限
                                    EasyPermission.with(activity)
                                            .rationale(StringUtil.getString(R.string.rationale_camera))
                                            .addRequestCode(PermissionCode.RG_CAMERA_PERM)
                                            .permissions(Manifest.permission.CAMERA)
                                            .request();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
        }
        picDialog.show();
    }

    /**
     * 打开手机摄像头
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
                        uploadImg();
                        //allot();
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
    private void uploadImg() {
        upload(checkPhotoUrl());
    }

    /**
     * 过滤已上传的图片
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
            if (StringUtil.isEmpty(picMap.get(photoPath))) {
                photoPathList.add(photoPath);
            }
        }
        return photoPathList;
    }

    /**
     * 开始上传图片
     *
     * @param photoPathList 图片路径集合
     */
    private void upload(List<String> photoPathList) {
        if (photoPathList == null) return;
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
                    uploadImg();
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
     * 发帖
     */
    public void publish() {
        isPublishing.set(true);
        loadingWindow.showWindow();
        if (!uploadOK) {
            return;
        }

        if (list.size() > 1) {
            if (isMax) {
                for (PhotoInfo photoInfo : list) {
                    String url = picMap.get(photoInfo.getPhotoPath());
                    urlSb.append(url).append(",");
                }
            } else {
                for (int i = 0; i < list.size() - 1; i++) {
                    String url = picMap.get(list.get(i).getPhotoPath());
                    urlSb.append(url).append(",");
                }
            }
            urlSb.deleteCharAt(urlSb.length() - 1);
        }
        String picUrl = urlSb.toString();
        String refundType = isRefundMoney.get() ? "2" : "1";
        String orderId = refundBean.get().getOrderId();
        String money = activity.getInputMoney();
        String explain = activity.getInputExplain();
        ApiWrapper.getInstance()
                .submitRefund(refundReasonId, productId, orderId, orderDetailId,
                        money, explain, refundType, picUrl)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> {
                    loadingWindow.hidWindow();
                    isPublishing.set(false);
                })
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {
                        EventBus.getDefault().post(Event.RELOAD);
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
                .doOnTerminate(() -> {
                    isReady = true;
                    allot();
                })
                .subscribe(new NetworkSubscriber<RxUploadUrl>() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        uploadOK = true;
                        if (isPublishing.get()) {
                            publish();
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
                            if (picMap.get(uploadUrl.getFilePath()) == null) {
                                picMap.put(uploadUrl.getFilePath(), uploadUrl.getRelativeUrl());
                            }
                        }
                    }
                });
    }

    /**
     * 图片压缩上传
     *
     * @param path 图片路劲
     * @return 获取被观察者
     */
    @Nullable
    private Observable<RxUploadUrl> rxUpload(final String path) {
        if (picMap.get(path) != null) return null;
        return ApiWrapper.getInstance().setUpload(path);
    }

    /**
     * 点击响应
     */
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
                        if (((PhotoInfo) baseQuickAdapter.getData().get(i)).getPhotoPath() ==
                                null) {
                            getPic();
                        }
                        break;
                }
            }
        };
    }

    @Override
    public void destroy() {
        WeakReference<GalleryFinal.OnHanlderResultCallback> wr = new WeakReference<>(resultCallback);
        resultCallback = null;
        loadingWindow.dismiss();
        GalleryFinal.clearCallback();
        GalleryFinal.cleanCacheFile();
    }
}
