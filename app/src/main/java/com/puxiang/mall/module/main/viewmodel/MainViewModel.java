package com.puxiang.mall.module.main.viewmodel;

import android.Manifest;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.puxiang.mall.BR;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.updata.NotificationDownloadCreator;
import com.puxiang.mall.config.updata.NotificationInstallCreator;
import com.puxiang.mall.model.data.HttpResult;
import com.puxiang.mall.model.data.RxAds;
import com.puxiang.mall.model.data.RxMessageState;
import com.puxiang.mall.module.bbs.viewmodel.BbsRequest;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static android.content.ContentValues.TAG;


public class MainViewModel extends BaseObservable implements ViewModel {

//    private String[] mPermissionList = new String[]{
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//            , Manifest.permission.ACCESS_FINE_LOCATION
//            , Manifest.permission.ACCESS_WIFI_STATE
//            , Manifest.permission.CALL_PHONE
//            , Manifest.permission.READ_LOGS
//            , Manifest.permission.READ_PHONE_STATE
//            , Manifest.permission.WRITE_EXTERNAL_STORAGE
//            , Manifest.permission.SET_DEBUG_APP
//            , Manifest.permission.SYSTEM_ALERT_WINDOW
//            , Manifest.permission.GET_ACCOUNTS
//            , Manifest.permission.WRITE_APN_SETTINGS};

    private BaseBindActivity activity;
    private NetworkSubscriber<Boolean> observer;
    private ObservableInt shopCount = new ObservableInt(0);
    public ObservableBoolean isPost = new ObservableBoolean(false);

    public void setIsPost(boolean isPost) {
        this.isPost.set(isPost);
    }

    public int getShopCount() {
        return shopCount.get();
    }

    public void setShopCount(int shopCount) {
        this.shopCount.set(shopCount);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public MainViewModel(BaseBindActivity activity) {
        this.activity = activity;

        checkPermission();
        versionCheck();
//        getSplashImage();
        //签到
        BbsRequest.setSigned();
    }

    /**
     * 获取购物车数量
     */
    public void getShopData() {

    }

    /**
     * 权限检测
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void checkPermission() {
        EasyPermission.with(activity)
                .rationale("需要读取您的内存卡")
                .addRequestCode(PermissionCode.RG_READ_EXTERNAL_STORAGE)
//                .permissions(mPermissionList)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE).request();
    }

    /**
     * 获取最新欢迎启动页
     */
    private void getSplashImage() {
        observer = new NetworkSubscriber<Boolean>() {
            @Override
            public void onSuccess(Boolean bean) {
                Log.e(TAG, "getSplashImage: " + bean);
            }
        };
        ApiWrapper.getService()
                .getAds(URLs.APP_WELCOME)
                .map(HttpResult::getReturnObject)
                .flatMap(new Function<List<RxAds>, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> apply(@NonNull List<RxAds> rxAdses) throws Exception {
                        String picUrl = checkUrl(rxAdses);
                        return ApiWrapper.getInstance().downloadPicFromNet(picUrl);
                    }
                })
                .map(RetrofitUtil::writeResponseBodyToDisk)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getMessageState(){
        ApiWrapper.getInstance()
                .getMessageState()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxMessageState>() {
                    @Override
                    public void onSuccess(RxMessageState bean) {
                        MyApplication.messageState.setData(bean);
                        notifyPropertyChanged(BR.messageState);
                    }
                });
    }

    /**
     * 检测地址图片是否已缓存
     *
     * @param ads
     * @return
     */
    @Nullable
    private String checkUrl(List<RxAds> ads) {
        String picUrl = null;
        if (ads.size() > 0) {
            String url = MyApplication.mCache.getAsString("SplashImageUrl");
            picUrl = ads.get(0).getPicUrl();
            if (!StringUtil.isEmpty(url) && url.equals(picUrl)) {
                observer.mDisposable.dispose();
            } else {
                MyApplication.mCache.put("SplashImageUrl", picUrl);
            }
        } else {
            observer.mDisposable.dispose();
        }
        return picUrl;
    }

    /**
     * 版本检测
     * ps:采取延时检测提升用户体验
     */
    private void versionCheck() {
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (MyApplication.isInit) {
                        Log.e(TAG, "versionCheck: ");

                        UpdateBuilder.create().strategy(new UpdateStrategy() {
                            @Override
                            public boolean isShowUpdateDialog(Update update) {
                                return true;
                            }

                            @Override
                            public boolean isAutoInstall() {
                                return true;
                            }

                            @Override
                            public boolean isShowDownloadDialog() {
                                return true;
                            }
                        })
                                .installDialogCreator(new NotificationInstallCreator())
                                .downloadDialogCreator(new NotificationDownloadCreator())
                                .check();

                    }
                });
    }

    @Override
    public void destroy() {

    }
}
