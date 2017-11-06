package com.puxiang.mall.module.welcome.viewmodel;

import android.databinding.ObservableBoolean;

import com.puxiang.mall.mvvm.base.ViewModel;

/**
 * Created by zhaoyong bai on 2017/9/30.
 */

public class GuideViewModel implements ViewModel {

    public ObservableBoolean isButton = new ObservableBoolean(false);

    public void setIsButton(boolean isButton) {
        this.isButton.set(isButton);
    }

    @Override
    public void destroy() {

    }
}
