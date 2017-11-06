package com.puxiang.mall.module.userinfo.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.model.data.AppVersionJSON;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;
import org.lzh.framework.updatepluginlib.util.UpdatePreference;

import java.util.Set;

public class VersionViewModel implements ViewModel {

    private BaseBindActivity activity;
    public Intent mIntent;
    public ObservableField<String> versionName = new ObservableField<>();
    public ObservableBoolean hasNewVersion = new ObservableBoolean();
    private int versionCode = -1;

    public VersionViewModel(BaseBindActivity activity) {
        this.activity = activity;
        initData();

    }

    public void initData() {
        String currentVersionName = ActivityUtil.getVersionName(activity);
        mIntent = activity.getIntent();
        activity.setPageTag("currentVersionName:", currentVersionName);
        boolean isNewestVersion = mIntent.getBooleanExtra(CacheKey.IS_NEWEST_VERSION, false);
        if (isNewestVersion) {
            String versionName = mIntent.getStringExtra("versionName");
            if (!StringUtil.isEmpty(versionName)) {
                currentVersionName = versionName;
            }
            hasNewVersion.set(false);
        }
        MyApplication.mCache.getAsJSONBean(CacheKey.VERSION, AppVersionJSON.class, appVersionJSON ->
                versionCode = appVersionJSON.getReturnObject().getVersionCode());

        versionName.set(currentVersionName);
    }

    /**
     * 检测有无版本更新
     */
    public void checkUpdate() {
        if (hasNewVersion.get()) {
            ToastUtil.toast("已经是最新版本");
            return;
        }
        Set<String> ignoreVersions = UpdatePreference.getIgnoreVersions();
        String versionCodeStr = String.valueOf(versionCode);
        if (ignoreVersions.contains(versionCodeStr)) {
            ignoreVersions.remove(versionCodeStr);
            UpdateConfig
                    .getConfig()
                    .getContext()
                    .getSharedPreferences("update_preference", Context.MODE_PRIVATE)
                    .edit()
                    .putStringSet("ignoreVersions", ignoreVersions).apply();
        }
        UpdateBuilder updateBuilder = UpdateBuilder.create().strategy(new UpdateStrategy() {
            @Override
            public boolean isShowUpdateDialog(Update update) {
                // 有新更新直接展示
                return true;
            }

            @Override
            public boolean isAutoInstall() {
                return true;
            }

            @Override
            public boolean isShowDownloadDialog() {
                // 展示下载进度
                return true;
            }
        });
        updateBuilder.check();
    }

    /**
     * 应用市场评论
     */
    public void marketComment() {
        try {
            Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.toast("Couldn't launch the market !");
        }
    }

    @Override
    public void destroy() {

    }
}
