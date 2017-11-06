package com.puxiang.mall.module.userinfo.viewmodel;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static android.app.Activity.RESULT_OK;

public class SexViewModel implements ViewModel {
    private final BaseBindActivity activity;

    public SexViewModel(BaseBindActivity activity) {
        this.activity = activity;
    }

    /**
     * 修改性别
     *
     * @param sex 性别
     */
    public void modifySex(String sex) {
        ApiWrapper.getInstance()
                .modifySex(sex)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
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
