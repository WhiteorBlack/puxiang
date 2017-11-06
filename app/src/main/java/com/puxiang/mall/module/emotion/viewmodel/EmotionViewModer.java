package com.puxiang.mall.module.emotion.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.puxiang.mall.model.data.RxComment;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.module.emotion.view.fragment.EmotionMainFragment;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.trello.rxlifecycle2.android.FragmentEvent;


public class EmotionViewModer extends BaseObservable implements ViewModel {
    public ObservableBoolean bigBar = new ObservableBoolean(false);
    public ObservableField<RxPostInfo> postInfoBean;
    private EmotionMainFragment fragment;


    public EmotionViewModer(EmotionMainFragment fragment, ObservableField<RxPostInfo>
            postInfoBean) {
        this.fragment = fragment;
        this.postInfoBean = postInfoBean;
    }

    public void comment(String postId, String content) {
        ApiWrapper.getInstance().comment(postId, content)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxComment>() {
                    @Override
                    public void onSuccess(RxComment bean) {
                        fragment.loadPostComments();
                    }
                });
    }

    @Bindable
    public ObservableField<RxPostInfo> getPostInfoBean() {
        return postInfoBean;
    }

    public void setPostInfoBean(ObservableField<RxPostInfo> postInfoBean) {
        this.postInfoBean = postInfoBean;
    }

    @Override
    public void destroy() {

    }
}
