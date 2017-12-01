package com.puxiang.mall.module.userinfo.viewmodel;

import android.app.DatePickerDialog;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxCheck;
import com.puxiang.mall.model.data.RxMyUserInfo;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

public class InfoViewModel implements ViewModel {

    private final BaseBindActivity activity;
    public ObservableField<RxMyUserInfo> userBean = new ObservableField<>();
    public ObservableField<String> sex = new ObservableField<>("");
    public ObservableField<RxCheck> check = new ObservableField<>();
    private DatePickerDialog datePickerDialog;

    public InfoViewModel(BaseBindActivity activity) {
        EventBus.getDefault().register(this);
        this.activity = activity;
        getCacheData();
        getInfo();
    }

    /**
     * 事件订阅
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.KILL_INFO) {
            activity.finish();
        }
    }

    /**
     * 事件订阅
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            RxMyUserInfo rxMyUserInfo = userBean.get();
            rxMyUserInfo.setMobile(phone);
            userBean.set(rxMyUserInfo);
        }
    }

    /**
     * 查找缓存数据
     */
    private void getCacheData() {
        MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxMyUserInfo.class, rxMyUserInfo -> {
            if (rxMyUserInfo != null) {
                setUserInfo(rxMyUserInfo);
            }
        });
    }

    /**
     * 检测昵称
     */
    private void checkNick() {
        ApiWrapper.getInstance()
                .checkNickname()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxCheck>() {
                    @Override
                    public void onSuccess(RxCheck bean) {
                        check.set(bean);
                        ToastUtil.toast(bean.getDesc());
                    }
                });
    }

    /**
     * 请求网络，获取用户信息
     */
    public void getInfo() {
        checkNick();
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RxMyUserInfo myUserInfo) {
        if (myUserInfo == null) return;
        userBean.set(myUserInfo);
        MyApplication.mCache.put(CacheKey.USER_INFO, myUserInfo);
        String sexStr = "保密";
        switch (myUserInfo.getSex()) {
            case 0:
                sexStr = "女";
                break;
            case 1:
                sexStr = "男";
                break;
        }
        sex.set(sexStr);
    }

    /**
     * 创建时间选择器
     */
    private void createDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(activity, (view, year, monthOfYear, dayOfMonth)
                -> setBirthday(year + "-" + ++monthOfYear + "-" + dayOfMonth)
                , calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(true);
    }

    public void selectBirthday() {
        if (datePickerDialog == null) {
            createDatePickerDialog();
        }
        datePickerDialog.show();
    }

    /**
     * 修改生日
     *
     * @param birthday
     */
    private void setBirthday(final String birthday) {
        ApiWrapper.getInstance()
                .modifyBirthday(birthday)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {
                        ToastUtil.toast("修改成功");
                        userBean.get().setBirthday(birthday);
                        userBean.notifyChange();
                    }
                });
    }


    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
