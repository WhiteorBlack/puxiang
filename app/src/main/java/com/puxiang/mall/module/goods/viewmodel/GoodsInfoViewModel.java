package com.puxiang.mall.module.goods.viewmodel;

import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.mvvm.base.ViewModel;

/**
 * Created by zhaoyong bai on 2018/3/26.
 */

public class GoodsInfoViewModel implements ViewModel {
    private BaseBindFragment fragment;
    private BaseBindActivity activity;

    public GoodsInfoViewModel(BaseBindFragment fragment) {
        this.fragment = fragment;
        this.activity = (BaseBindActivity) fragment.getActivity();
    }

    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void destroy() {

    }
}
