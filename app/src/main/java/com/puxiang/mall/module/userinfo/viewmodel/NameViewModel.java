package com.puxiang.mall.module.userinfo.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static android.app.Activity.RESULT_OK;

public class NameViewModel extends BaseObservable implements ViewModel {

    private final BaseBindActivity activity;
    public ObservableField<String> info = new ObservableField<>();
    public ObservableBoolean checkSelection = new ObservableBoolean();

    public NameViewModel(BaseBindActivity activity) {
        this.activity = activity;
        initCache();
    }

    private void initCache() {
        String data = activity.getIntent().getStringExtra("info");
        if (!StringUtil.isEmpty(data)) {
            info.set(data);
            checkSelection.set(true);
        }
    }

    public ObservableBoolean getCheckSelection() {
        return checkSelection;
    }

    public void setCheckSelection(ObservableBoolean checkSelection) {
        this.checkSelection = checkSelection;
    }

    /**
     * 修改姓名
     */
    public void modifyName() {
        String realName = info.get();
        if (StringUtil.isEmpty(realName)) {
            ToastUtil.toast("姓名不能为空");
            return;
        }
        ApiWrapper.getInstance()
                .modifyRealName(realName)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(String bean) {
                        ToastUtil.toast("修改成功");
                        activity.setResult(RESULT_OK);
                        activity.finish();
                    }
                });
    }

    /**
     * 修改昵称
     */
    public void modifyNick() {
        String nickName = info.get();
        if (StringUtil.isEmpty(nickName)) {
            ToastUtil.toast("昵称不能为空");
            return;
        }
        ApiWrapper.getInstance().modifyNickname(nickName)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onSuccess(String bean) {
                        ToastUtil.toast("修改成功");
                        activity.setResult(RESULT_OK);
                        activity.finish();
                    }
                });
    }

    @Override
    public void destroy() {
    }
}
