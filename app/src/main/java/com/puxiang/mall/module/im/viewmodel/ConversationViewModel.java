package com.puxiang.mall.module.im.viewmodel;

import android.app.Activity;
import android.databinding.ObservableField;
import android.util.Log;

import com.puxiang.mall.module.im.model.IMRequest;
import com.puxiang.mall.module.im.model.IMTitle;
import com.puxiang.mall.module.im.model.IMUserInfoProvider;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.rong.imlib.model.UserInfo;


public class ConversationViewModel implements ViewModel {
    private final Activity activity;
    public ObservableField<String> title = new ObservableField<>();
    public String targetId;

    public ConversationViewModel(Activity activity) {
        EventBus.getDefault().register(this);
        this.activity = activity;
        IMRequest.IMConnect();
        initData();
    }

    private void initData() {
        String titleStr = activity.getIntent().getData().getQueryParameter("title");
        targetId = activity.getIntent().getData().getQueryParameter("targetId");
        if (IMUserInfoProvider.NULL.equals(titleStr)) {
            UserInfo userInfo = IMUserInfoProvider.getInstance().getUserInfo(targetId);
            if (userInfo != null) {
                titleStr = StringUtil.isEmpty(userInfo.getName()) ? "" : userInfo.getName();
            } else {
                titleStr = "";
            }
            Log.e("ConversationViewModel", "initData: " + titleStr);
        }
        title.set(titleStr);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IMTitle IMtitle) {
        if (IMtitle != null) {
            title.set(IMtitle.getTitle());
        }
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }
}
