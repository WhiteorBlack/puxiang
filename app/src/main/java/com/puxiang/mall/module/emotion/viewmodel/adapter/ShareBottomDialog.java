package com.puxiang.mall.module.emotion.viewmodel.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.View;

import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.widget.base.BottomBaseDialog;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.databinding.DialogShareBinding;
import com.puxiang.mall.module.post.model.ShareInfo;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.StringUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

public class ShareBottomDialog extends BottomBaseDialog<ShareBottomDialog> {

    private ShareInfo shareInfo;
    private Context context;
    private UMShareListener umShareListener;
    private DialogShareBinding binding;

    public ShareBottomDialog(Context context, View animateView) {
        super(context, animateView);
    }

    public ShareBottomDialog(Context context, ShareInfo info) {
        super(context);
        this.context = context;
        this.shareInfo = info;

        Log.e("2222", "ShareInfo: " + info);
        this.umShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
//                ToastUtil.toast("分享成功");
                if (StringUtil.isEmpty(MyApplication.TOKEN)) {
                    return;
                }

                Log.e("2222", "onResult: " + shareInfo.getRawUrl());
                ApiWrapper.getInstance().shareOK(shareInfo.getRawUrl())
                        .subscribe(new NetworkSubscriber<String>() {
                            @Override
                            public void onSuccess(String bean) {
                                if (shareInfo.getRawUrl().contains("Everyday_lottery.html")) {
                                    EventBus.getDefault().post(Event.RELOAD_WEB);
                                }
                            }
                        });
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Log.e("2222", "onError: ");
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Log.e("2222", "onCancel: ");
            }
        };
    }

    public void setShareInfo(ShareInfo info) {
        this.shareInfo = info;
    }

    @Override
    public View onCreateView() {
        showAnim(new FlipVerticalSwingEnter());
        dismissAnim(null);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_share, null, false);
        AutoUtils.auto(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void setUiBeforShow() {
        binding.llWechatCircle.setOnClickListener(v -> sharePlatform(SHARE_MEDIA.WEIXIN_CIRCLE));
        binding.llWechat.setOnClickListener(v -> sharePlatform(SHARE_MEDIA.WEIXIN));
        binding.llQq.setOnClickListener(v -> sharePlatform(SHARE_MEDIA.QQ));
        binding.llQzone.setOnClickListener(v -> sharePlatform(SHARE_MEDIA.QZONE));
    }

    private void sharePlatform(SHARE_MEDIA media) {
        dismiss();
        ShareAction shareAction = new ShareAction((Activity) context);
        if (!StringUtil.isEmpty(shareInfo.getImgUrl())) {
            shareAction.withMedia(new UMImage(context, shareInfo.getImgUrl()));
        }
        shareAction
                .withText(shareInfo.getTitle())
                .setPlatform(media)
                .setCallback(umShareListener)
                .share();
        WeakReference<ShareAction> weakReference = new WeakReference<>(shareAction);
    }
}
