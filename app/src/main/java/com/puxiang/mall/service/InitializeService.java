package com.puxiang.mall.service;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;

import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideLeftExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.model.data.AppVersionJSON;
import com.puxiang.mall.network.FrescoImageLoader;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.utils.DateUtils;
import com.puxiang.mall.utils.NetworkUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.widget.MyMaterialDialog;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;

import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.creator.DialogCreator;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateParser;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

public class InitializeService extends IntentService {
    private boolean isConfirm;
    private boolean isUpdateConfirm;
    private String createTime = "";
    public static final String ACTION_INIT_WHEN_APP_CREATE = "com.somic.mall.service.action.init";
    private static String TAG = "InitializeService";

    public InitializeService() {
        super("InitializeService");
    }

    public static void start(Context context, String action) {
        Log.e(TAG, "start: ");
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.e(TAG, "onHandleIntent: " + action);
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                performInit();
            }
        }
    }

    private void performInit() {
        if (MyApplication.isInit) {
            return;
        }
        MyApplication.isInit = true;
        Log.e(TAG, "performInit begin: " + System.currentTimeMillis());
        //  QbSdk.initX5Environment(this, null);
        initLogger();
//        initCloudChannel();
//        initPlatformConfig();
        initGalleryFinal();
        initUpdateConfig();
//        RongIM.init(this);
//        initSpeech();
        initHotFix();
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();
//        Stetho.initialize(
//                Stetho.newInitializerBuilder(this)
//                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
//                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
//                        .build());
        Log.e(TAG, "performInit end: " + System.currentTimeMillis());
    }

    private void initHotFix() {
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFile) {
                Log.e(TAG, "onPatchReceived: " + patchFile);
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                Log.e(TAG, "onDownloadReceived: " + totalLength);
            }

            @Override
            public void onDownloadSuccess(String msg) {
                Log.e(TAG, "onDownloadSuccess: " + msg);
            }

            @Override
            public void onDownloadFailure(String msg) {
                Log.e(TAG, "onDownloadFailure: " + msg);
            }

            @Override
            public void onApplySuccess(String msg) {
                Log.e(TAG, "onApplySuccess: " + msg);
                MyApplication.isHotFix = true;
            }

            @Override
            public void onApplyFailure(String msg) {
                Log.e(TAG, "onApplyFailure: " + msg);
            }

            @Override
            public void onPatchRollback() {

            }
        };
    }

    private void initLogger() {
        Logger.init(Config.LOG_TAG).setLogLevel(Config.LOG_LEVEL);
    }

    /**
     * 暂时不集成，后续再加  2017.09.01
     */
//    private void initPlatformConfig() {
//        //微信 appid appsecret
//        PlatformConfig.setWeixin(Config.WX_APP_ID, Config.WX_APP_SECRET);
//        PlatformConfig.setQQZone(Config.QQ_APP_ID, Config.QQ_APP_SECRET);
//        PlatformConfig.setSinaWeibo(Config.SINA_APP_ID, Config.SINA_APP_SECRET);
//
//        com.umeng.socialize.Config.DEBUG = true;
//        com.umeng.socialize.Config.IsToastTip = false;
//        com.umeng.socialize.Config.dialogSwitch = false;
//        com.umeng.socialize.Config.REDIRECT_URL = "http://api.esomic.com/weibo/callback.do";
//        MobclickAgent.setCatchUncaughtExceptions(true);
//    }

    /**
     * 版本升级配置
     */
    private void initUpdateConfig() {
        UpdateConfig
                .getConfig()
                .init(MyApplication.getContext())
                // 必填：数据更新接口
                .url(URLs.GET_VERSION)
                // 必填：用于从数据更新接口获取的数据response中。解析出Update实例。以便框架内部处理
                .jsonParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) {
                        Gson gson = new Gson();
                        AppVersionJSON appVersionJSON = gson.fromJson(response, AppVersionJSON.class);
                        AppVersionJSON.ReturnObjectBean bean = appVersionJSON.getReturnObject();
                        if (appVersionJSON.getErrorCode().equals(URLs.RESPONSE_OK)) {
                            MyApplication.mCache.put(CacheKey.VERSION, response);
                        }
                        // 此处模拟一个Update对象
                        Update update = new Update(response);
                        try {
                            // 此apk包的更新时间
                            update.setUpdateTime(DateUtils.convert2long(createTime));
                            // 此apk包的下载地址
                            update.setUpdateUrl(bean.getVersionFile());
                            // 此apk包的版本号
                            update.setVersionCode(bean.getVersionCode());
                            // 此apk包的版本名称
                            update.setVersionName(bean.getVersionName());
                            // 此apk包的更新内容
                            update.setUpdateContent(bean.getIntroduce());
                            // 此apk包是否为强制更新
                            update.setForced(bean.getIsMustUpdate() == 1);
                            // 是否显示忽略此次版本更新按钮
                            update.setIgnore(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return update;
                    }
                })
//                .checkCB(new UpdateCheckCB() {
//
//                    @Override
//                    public void onCheckError(int code, String errorMsg) {
//                        ToastUtil.toast("更新失败：code:" + code + ",errorMsg:" + errorMsg);
//                    }
//
//                    @Override
//                    public void onUserCancel() {
//                        ToastUtil.toast("用户取消更新");
//                    }
//
//                    @Override
//                    public void onCheckIgnore(Update update) {
//                        ToastUtil.toast("用户忽略此版本更新");
//                    }
//
//                    @Override
//                    public void onCheckStart() {
//                        // 此方法的回调所处线程异于其他回调。其他回调所处线程为UI线程。
//                        // 此方法所处线程为你启动更新任务是所在线程
//                        HandlerUtil.getMainHandler().post(new Runnable() {
//                            @Override
//                            public void run() {
//                                ToastUtil.toast("启动更新任务");
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void hasUpdate(Update update) {
//                        ToastUtil.toast("检查到有更新");
//                    }
//
//                    @Override
//                    public void noUpdate() {
//                        ToastUtil.toast("无更新");
//                    }
//                })
//                // apk下载的回调
//                .downloadCB(new UpdateDownloadCB() {
//                    @Override
//                    public void onUpdateStart() {
//                        ToastUtil.toast("下载开始");
//                    }
//
//                    @Override
//                    public void onUpdateComplete(File file) {
//                        ToastUtil.toast("下载完成");
//                    }
//
//                    @Override
//                    public void onUpdateProgress(long current, long total) {
//                    }
//
//                    @Override
//                    public void onUpdateError(int code, String errorMsg) {
//                        ToastUtil.toast("下载失败：code:" + code + ",errorMsg:" + errorMsg);
//                    }
//                })
                .updateDialogCreator(new DialogCreator() {
                    @Override
                    public Dialog create(Update update, Activity context) {
                        final NormalDialog updateDialog = new NormalDialog(context);
                        updateDialog.titleTextColor(getResources().getColor(R.color.sale_price))
                                .style(NormalDialog.STYLE_TWO).titleTextSize(18).title("温馨提示")
                                .cornerRadius(0)
                                .content("检测到当前使用的是移动流量，是否确定更新？")
                                .contentGravity(Gravity.START)
                                .showAnim(new BounceLeftEnter()).dismissAnim(new SlideLeftExit())
                                .btnText("取消", "立即更新").setOnBtnClickL(() -> {
                            updateDialog.dismiss();
                            sendUserCancel();
                        }, () -> {
                            isUpdateConfirm = true;
                            updateDialog.dismiss();
                        });
                        updateDialog.setOnDismissListener(dialog1 -> {
                            if (isUpdateConfirm) {
                                isUpdateConfirm = false;
                                sendDownloadRequest(update);
                            } else {
                                sendUserCancel();
                            }
                        });
                        boolean isForced = update.isForced();
                        MyMaterialDialog dialog = new MyMaterialDialog(context, isForced);
                        dialog.title("发现新版本 " + update.getVersionName())
                                .titleTextColor(AppUtil.getColor(R.color.sale_price))
                                .titleTextSize(18)
                                .cornerRadius(10)
                                .content(update.getUpdateContent()).contentGravity(Gravity.START)
                                .btnNum(isForced ? 1 : 3)
                                .btnText(isForced ? new String[]{"立即更新"}
                                        : new String[]{"忽略此版本", "立即更新", "取消"})
                                .showAnim(new BounceLeftEnter())
                                .dismissAnim(new SlideLeftExit())
                                .setOnBtnClickL(isForced ? new OnBtnClickL[]{() -> {
                                    isConfirm = true;
                                    dialog.dismiss();
                                }} : new OnBtnClickL[]{() -> {
                                    dialog.dismiss();
                                    sendUserIgnore(update);
                                    ToastUtil.longToast(getString(R.string.version_ignore_tips));
                                }, () -> {
                                    isConfirm = true;
                                    MyApplication.mCache.remove(CacheKey.VERSION);
                                    dialog.dismiss();
                                }, () -> {
                                    dialog.dismiss();
                                    sendUserCancel();
                                }});
                        dialog.setOnDismissListener(dialog1 -> {
                            if (isConfirm) {
                                isConfirm = false;
                                if (isForced) {
                                    sendDownloadRequest(update);
                                } else {
                                    boolean wifi = NetworkUtil.isWifi(context);
                                    if (wifi) {
                                        sendDownloadRequest(update);
                                    } else {
                                        updateDialog.show();
                                    }
                                }
                            }
                        });
                        return dialog;
                    }
                });
    }

    /**
     * 图片选择器配置
     */
    private void initGalleryFinal() {
        ThemeConfig themeConfig = ThemeConfig.DEFAULT;
        FunctionConfig functionConfig = new FunctionConfig.Builder().setEnableEdit(true)
                .setEnableCrop(true).setCropSquare(true).setForceCrop(true).setEnablePreview
                        (true).setEnableCamera(false).build();

        FrescoImageLoader imageLoader = new FrescoImageLoader(this);

        CoreConfig coreConfig = new CoreConfig.Builder(this, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig).setNoAnimcation(true).build();
        GalleryFinal.init(coreConfig);
    }

    /**
     * 暂时不集成，后续再加  2017.09.01
     */
//    private void initSpeech() {
//        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=58b69a7e");
//    }

    /**
     * 初始化云推送通道
     * 暂时不集成，后续再加  2017.09.01
     */
//    private void initCloudChannel() {
//        PushServiceFactory.init(MyApplication.getContext());
//        CloudPushService pushService = PushServiceFactory.getCloudPushService();
//        pushService.register(MyApplication.getContext(), new CommonCallback() {
//            @Override
//            public void onSuccess(String response) {
//                Log.d(TAG, "init cloudchannel success");
//            }
//
//            @Override
//            public void onFailed(String errorCode, String errorMessage) {
//                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- " +
//                        "errorMessage:" + errorMessage);
//            }
//        });
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }
}