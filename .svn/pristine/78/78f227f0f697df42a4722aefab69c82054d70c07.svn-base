package com.puxiang.mall.module.bbs.viewmodel;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.model.data.RxPost;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;

public class BbsRequest {

    public interface RefreshListener {
        void refreshOK();

        void refreshFail();
    }

    public static void setPostLike(final RxPostInfo postInfo) {
        boolean isLiked = !postInfo.getIsLiked();
        RxPost post = postInfo.getPost();
        int likeQty = post.getLikeQty();
        if (isLiked) {
            post.setLikeQty(likeQty + 1);
        } else {
            post.setLikeQty(likeQty - 1);
        }
        postInfo.setIsLiked(isLiked);
        ApiWrapper.getInstance().postLike(post.getId(), isLiked)
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {

                    }
                });
    }

    /**
     * 签到
     */
    public static void setSigned() {
        if (!MyApplication.isLogin()) {
            return;
        }
        ApiWrapper.getInstance().userSigned()
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {

                    }
                });
    }
}
