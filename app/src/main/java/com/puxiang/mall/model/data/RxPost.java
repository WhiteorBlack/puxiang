package com.puxiang.mall.model.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;

import com.puxiang.mall.BR;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class RxPost extends BaseObservable {
    private String linkUrl;
    private int postUserId;
    private int likeQty;
    private String readQty;
    private String orderDetailId;
    private String id;
    private String title;
    private int postTypeId;
    private int isTop;
    private int isRecommend;
    private int commentQty;
    private int plateId;
    private String picUrl;
    private int postType;
    private String publishTime;
    private String videoUrl;
    private String linkType;
    private String productInfo;
    private int isHot;
    private int isHotPost;
    private String content;
    private String linkId;
    private String source;
    private int playQty;
    private String topTime;
    private String postTypeName;
    private String[] picUrls;

    private ObservableField<String> picOnly = new ObservableField<>();
    private ObservableField<String> picOne = new ObservableField<>();
    private ObservableField<String> picTwo = new ObservableField<>();
    private ObservableField<String> picThree = new ObservableField<>();
    private ObservableInt likeCount = new ObservableInt(0);

    @Bindable
    public ObservableField<String> getPicOnly() {
        return picOnly;
    }

    public void setPicOnly(ObservableField<String> picOnly) {
        this.picOnly = picOnly;
    }

    @Bindable
    public ObservableField<String> getPicOne() {
        return picOne;
    }

    public void setPicOne(ObservableField<String> picOne) {
        this.picOne = picOne;
    }

    @Bindable
    public ObservableField<String> getPicTwo() {
        return picTwo;
    }


    public void setPicTwo(ObservableField<String> picTwo) {
        this.picTwo = picTwo;
    }

    @Bindable
    public ObservableField<String> getPicThree() {
        return picThree;
    }

    public void setPicThree(ObservableField<String> picThree) {
        this.picThree = picThree;
    }

    @Bindable
    public String[] getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(String[] picUrls) {
        this.picUrls = picUrls;
        dealData(picUrls);
    }

    private void dealData(String[] picUrls) {
        if (picUrls.length == 1) {
            picOnly.set(picUrls[0]);
        }
        if (picUrls.length == 2) {
            picOne.set(picUrls[0]);
            picTwo.set(picUrls[1]);
        }
        if (picUrls.length > 2) {
            picOne.set(picUrls[0]);
            picTwo.set(picUrls[1]);
            picThree.set(picUrls[2]);
        }

    }

    @Bindable
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public int getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(int postUserId) {
        this.postUserId = postUserId;
    }

    @Bindable
    public ObservableInt getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(ObservableInt likeCount) {
        this.likeCount = likeCount;
    }

    @Bindable
    public int getLikeQty() {
        return likeQty;
    }

    public void setLikeQty(int likeQty) {
        this.likeQty = likeQty;
        likeCount.set(likeQty);
        notifyPropertyChanged(BR.likeCount);
        notifyPropertyChanged(BR.likeQty);
    }

    public String getReadQty() {
        return readQty;
    }

    public void setReadQty(String readQty) {
        this.readQty = readQty;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPostTypeId() {
        return postTypeId;
    }

    public void setPostTypeId(int postTypeId) {
        this.postTypeId = postTypeId;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public int getCommentQty() {
        return commentQty;
    }

    public void setCommentQty(int commentQty) {
        this.commentQty = commentQty;
    }

    public int getPlateId() {
        return plateId;
    }

    public void setPlateId(int plateId) {
        this.plateId = plateId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;

        if (TextUtils.isEmpty(picUrl)) {
            setPicUrls(null);
        } else {
            setPicUrls(picUrl.split(","));
        }
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    @Bindable
    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    @Bindable
    public String getVideoUrl() {
        return videoUrl;
    }

    @BindingAdapter("videoData")
    public void setVideoData(JCVideoPlayerStandard playerStandard, RxPost post) {
        playerStandard.setUp(post.videoUrl, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, post.title);
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    public int getIsHotPost() {
        return isHotPost;
    }

    public void setIsHotPost(int isHotPost) {
        this.isHotPost = isHotPost;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getPlayQty() {
        return playQty;
    }

    public void setPlayQty(int playQty) {
        this.playQty = playQty;
    }

    public String getTopTime() {
        return topTime;
    }

    public void setTopTime(String topTime) {
        this.topTime = topTime;
    }

    public String getPostTypeName() {
        return postTypeName;
    }

    public void setPostTypeName(String postTypeName) {
        this.postTypeName = postTypeName;
    }


}
