package com.puxiang.mall.module.my.viewmodel;

import android.Manifest;
import android.content.Intent;
import android.databinding.ObservableField;
import android.net.Uri;

import com.baoyz.actionsheet.ActionSheet;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxMyUserInfo;
import com.puxiang.mall.model.data.RxUploadUrl;
import com.puxiang.mall.module.my.view.ShowHeadPicActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.DraweeViewUtils;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class ShowViewModel implements ViewModel {


    private static final int REQUEST_CODE_GALLERY = 1001;
    private final LoadingWindow loadingWindow;
    private String[] tabs;
    private final ShowHeadPicActivity activity;
    private ActionSheet.Builder picSheet;
    private ActionSheet.Builder builder;
    public ObservableField<String> url = new ObservableField<>();
    public GalleryFinal.OnHanlderResultCallback resultCallback;
    public boolean isOwn = false;

    public ShowViewModel(ShowHeadPicActivity activity) {
        this.activity = activity;
        loadingWindow = new LoadingWindow(activity);
        initFunctionConfig();
        getCache();
    }

    public void getCache() {
        url.set(activity.getIntent().getStringExtra("url"));
        String userId = activity.getIntent().getStringExtra("userId");
        if (MyApplication.USER_ID.equals(userId)) {
            isOwn = true;
            tabs = new String[]{"发送", "保存到手机", "上传头像"};
        } else {
            tabs = new String[]{"发送", "保存到手机"};
        }
    }

    private void initFunctionConfig() {
        resultCallback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null && resultList.size() > 0) {
                    String path = resultList.get(0).getPhotoPath();
                    upload(path);
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                ToastUtil.toast(errorMsg);
            }
        };
    }

    public void saveImg() {
        if (builder == null) {
            builder = ActionSheet.createBuilder(activity, activity.getSupportFragmentManager())
                    .setCancelButtonTitle("取消")
                    .setOtherButtonTitles(tabs)
                    .setCancelableOnTouchOutside(true)
                    .setListener(new ActionSheet.ActionSheetListener() {
                        @Override
                        public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                        }

                        @Override
                        public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                            File newFile = DraweeViewUtils.getInstance().saveCacheDrawable(url
                                    .get());
                            switch (index) {
                                case 0:
                                    if (newFile != null && newFile.exists()) {
                                        Intent intent = new Intent(Intent
                                                .ACTION_MEDIA_SCANNER_SCAN_FILE);
                                        Uri uri = Uri.fromFile(newFile);
                                        intent.setData(uri);
                                        sendImg(uri);
                                        activity.sendBroadcast(intent);
                                    } else {
                                        ToastUtil.toast("发送失败");
                                    }
                                    break;
                                case 1:
                                    if (newFile != null && newFile.exists()) {
                                        Intent intent = new Intent(Intent
                                                .ACTION_MEDIA_SCANNER_SCAN_FILE);
                                        Uri uri = Uri.fromFile(newFile);
                                        intent.setData(uri);
                                        activity.sendBroadcast(intent);
                                        ToastUtil.toast("图片已保存到：" + newFile.getAbsolutePath());
                                    } else {
                                        ToastUtil.toast("图片保存失败");
                                    }
                                    break;

                                case 2:
                                    getPic();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
        }
        builder.show();
    }

    /**
     * 图片发送
     *
     * @param uri
     */
    private void sendImg(Uri uri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        activity.startActivity(Intent.createChooser(shareIntent, "请选择"));
    }

    /**
     * 获取手机相片
     */
    public void getPic() {
        if (picSheet == null) {
            picSheet = ActionSheet.createBuilder(activity, activity.getSupportFragmentManager())
                    .setCancelButtonTitle("取消")
                    .setOtherButtonTitles("打开相册", "拍照")
                    .setCancelableOnTouchOutside(true)
                    .setListener(new ActionSheet.ActionSheetListener() {
                        @Override
                        public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                        }

                        @Override
                        public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                            switch (index) {
                                case 0:
                                    GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY,
                                            GalleryFinal.getCoreConfig().getFunctionConfig(),
                                            resultCallback);
                                    break;
                                case 1:
                                    EasyPermission.with(activity)
                                            .rationale(StringUtil.getString(R.string
                                                    .rationale_camera))
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
        picSheet.show();
    }

    /**
     * 上传头像
     */
    private void upload(final String path) {
        loadingWindow.showWindow();
        ApiWrapper.getInstance()
                .setUpload(path)
                .subscribe(new NetworkSubscriber<RxUploadUrl>() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        GalleryFinal.cleanCacheFile();
                    }

                    @Override
                    public void onSuccess(RxUploadUrl uploadUrl) {
                        String url = uploadUrl.getRelativeUrl();
                        update(url);
                    }
                });
    }

    /**
     * 更改用户头像信息
     *
     * @param relativeUrl
     */
    private void update(final String relativeUrl) {
        ApiWrapper.getInstance()
                .modifyUserImage(relativeUrl)
                .doOnTerminate(loadingWindow::hidWindow)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {
                        ToastUtil.longToast("头像修改成功");
                        getInfo();
                    }
                });
    }

    /**
     * 请求新用户信息
     */
    private void getInfo() {
        ApiWrapper.getInstance()
                .getMyUserInfo()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxMyUserInfo>() {
                    @Override
                    public void onSuccess(RxMyUserInfo bean) {
                        setUserInfo(bean);
                    }
                });
    }

    /**
     * 更新用户信息
     */
    private void setUserInfo(RxMyUserInfo myUserInfo) {
        if (myUserInfo == null) {
            return;
        }
        EventBus.getDefault().post(myUserInfo);
        EventBus.getDefault().post(Event.RELOAD);
        MyApplication.mCache.put(CacheKey.USER_INFO, myUserInfo);
        url.set(myUserInfo.getUserImage());
    }

    @Override
    public void destroy() {
        WeakReference<GalleryFinal.OnHanlderResultCallback> wr1 = new WeakReference<>(
                resultCallback);
        WeakReference<GalleryFinal.OnHanlderResultCallback> wr2 = new WeakReference<>(
                GalleryFinal.getCallback());
        GalleryFinal.clearCallback();
        resultCallback = null;
        if (loadingWindow != null) loadingWindow.dismiss();
    }
}
