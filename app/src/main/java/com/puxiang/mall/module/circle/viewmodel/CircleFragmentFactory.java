package com.puxiang.mall.module.circle.viewmodel;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.puxiang.mall.model.data.RxChannel;
import com.puxiang.mall.module.bbs.view.VideoFragment;
import com.puxiang.mall.module.circle.view.CircleFragment;
import com.puxiang.mall.module.plate.view.PlateClassifyFragment;

/**
 * Desc : 首页页面的Fragment工厂类
 * version : v1.0
 */

public class CircleFragmentFactory {
    public static Fragment create(RxChannel channel) {
        Fragment fragment;
        int contentType = channel.getContentType();
        Bundle bundle = null;
        switch (contentType) {
            case RxChannel.CONTENT_TYPE_HOME:
                fragment = new CircleFragment();
                bundle = new Bundle();
                bundle.putString("typeId", channel.getTypeId() + "");
                break;
            case RxChannel.CONTENT_TYPE_VIDEO:
                fragment = new VideoFragment();
                bundle = new Bundle();
                bundle.putParcelableArrayList("list", channel.getChildren());
                bundle.putString("typeId", channel.getTypeId() + "");
                break;
            case RxChannel.CONTENT_TYPE_PLATE:
                fragment = new PlateClassifyFragment();
                break;
            default:
                return new Fragment();
        }
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }
}
