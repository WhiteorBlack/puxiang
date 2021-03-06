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
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.model.data.AppVersionJSON;
import com.puxiang.mall.network.FrescoImageLoader;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.utils.DateUtils;
import com.puxiang.mall.utils.NetworkUtil;
import com.puxiang.mall.widget.MyMaterialDialog;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.creator.DialogCreator;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateParser;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class InitializeService extends IntentService {
    private boolean isConfirm;
    private boolean isUpdateConfirm;
    private String createTime = "";
    public static final String ACTION_INIT_WHEN_APP_CREATE = "com.puxiang.mall.service.action.init";
    private static String TAG = "InitializeService";
    private static final String BUGLY_APPID = "7af15a29f3";

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
        UMShareAPI.get(this);
        Bugly.init(this.getApplicationContext(), BUGLY_APPID, false);
        initLogger();
        initPlatformConfig();
        initGalleryFinal();
        initUpdateConfig();
        initUMeng();
        initRongIM();
    }

    /**
     * 初始化友盟统计分析
     */
    private void initUMeng() {
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(true);
        UMConfigure.setEncryptEnabled(true);
    }

    private void initRongIM() {
        /**
         * 注意：
         *
         * IMKit SDK调用第一步 初始化
         *
         * context上下文
         *
         * 只有两个进程需要初始化，主进程和 push 进程
         */
//        RongIM.setServerInfo("nav.cn.ronghub.com", "up.qbox.me");
        RongIM.init(this, "cpj2xarlc19nn");
//        Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));

//        try {
//            RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
//            RongIM.registerMessageType(RongMessage.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (MyApplication.isLogin()){
            connect(MyApplication.RONG_TOKEN);
        }

    }

    private void getRongToken(String userId, String token) {
        ApiWrapper.getInstance()
                .getRongToken(userId, token)
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(String data) {
                        MyApplication.mCache.put(CacheKey.RONG_TOKEN, data);
                        connect(data);
                    }
                });
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在  init 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(String token) {

        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                getRongToken(MyApplication.USER_ID,MyApplication.TOKEN);
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e("rongClient---"+errorCode.getMessage());
            }
        });
    }


    private void initLogger() {
        Logger.init(Config.LOG_TAG).setLogLevel(Config.LOG_LEVEL);
    }

    private void initPlatformConfig() {
        //微信 appid appsecret
        PlatformConfig.setWeixin(Config.WX_APP_ID, Config.WX_APP_SECRET);
        PlatformConfig.setQQZone(Config.QQ_APP_ID, Config.QQ_APP_SECRET);
        PlatformConfig.setSinaWeibo(Config.SINA_APP_ID, Config.SINA_APP_SECRET, Config.SINA_REDICT_URL);

        com.umeng.socialize.Config.DEBUG = true;
        MobclickAgent.setCatchUncaughtExceptions(true);
    }

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
//                            update.setForced(false);
                            update.setForced(bean.getIsMustUpdate() == 1);
                            // 是否显示忽略此次版本更新按钮
                            update.setIgnore(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return update;
                    }
                })
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
                                .titleTextColor(AppUtil.getColor(R.color.white))
                                .titleTextSize(18)
                                .contentTextSize(14)
                                .cornerRadius(10)
                                .content(update.getUpdateContent()).contentGravity(Gravity.START)
                                .btnNum(isForced ? 1 : 3)
                                .btnTextColor(new int[]{R.color.white, R.color.white})
                                .btnText(isForced ? new String[]{"立即更新"}
                                        : new String[]{"取消", "", "立即更新"})
                                .showAnim(new BounceLeftEnter())
                                .dismissAnim(new SlideLeftExit())
                                .setOnBtnClickL(isForced ? new OnBtnClickL[]{() -> {
                                    isConfirm = true;
                                    dialog.dismiss();
                                }} : new OnBtnClickL[]{() -> {
                                    dialog.dismiss();
                                    sendUserCancel();
                                }, () -> {
                                }, () -> {
                                    isConfirm = true;
                                    MyApplication.mCache.remove(CacheKey.VERSION);
                                    dialog.dismiss();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}