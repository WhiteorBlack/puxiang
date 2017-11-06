package com.puxiang.mall.model.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.puxiang.mall.BR;

public class RxPostInfo extends BaseObservable implements MultiItemEntity {
    public static final int POST_PLATE = 1;
    public static final int POST_OFFICIAL = 2;
    public static final int POST_VIDEO = 3;
    public static final int POST_LINK = 4;

    /**
     * linkUrl : null
     * postUserId : 433
     * likeQty : 1
     * readQty : 14
     * orderDetailId : null
     * id : 2533
     * title : null
     * postTypeId : null
     * isTop : 0
     * isRecommend : 0
     * commentQty : 1
     * plateId : 7
     * picUrl : http://somicshop.oss-cn-shenzhen.aliyuncs
     * .com/attached/images/201611/5033187d72c74044a87e057e318143ea82580.jpg?616x800
     * postType : 1
     * publishTime : 2016-11-18 10:04:41
     * videoUrl : null
     * linkType : null
     * productInfo : null
     * isHot : 0
     * isHotPost : 0
     * content : 我的九尺金背大砍刀呢…
     * linkId : null
     * source : null
     * playQty : 0
     * topTime : null
     * postTypeName : 热门话题
     */

    private RxPost post;
    private boolean isAttented;
    /**
     * createTime : 2016-08-09 17:19:18
     * plateNavigationPic : http://somicshop.oss-cn-shenzhen.aliyuncs
     * .com/attached/images/201611/ffe8c7ad6b2e4bc5912211c7ed084d3f75548.jpg
     * sort : 98
     * isAttented : null
     * postQty : 383
     * id : 7
     * plateTypeCode : huati
     * plateType : null
     * platePic : http://somicshop.oss-cn-shenzhen.aliyuncs
     * .com/attached/images/201611/7d3c8c5f5a1c4f96945717749a4d3d4079047.jpg
     * plateName : 糗事大家乐
     * description : 有什么不开心的事，说出来让大家开心下。
     * attentionQty : 14
     * plateTypeId : 46
     * plateIntroduce : 有什么不开心的事，说出来让大家开心下。
     */

    private RxPlate plate;
    private boolean isLiked;
    public ObservableBoolean isZan = new ObservableBoolean(false);
    private boolean isOwner;
    /**
     * sex : null
     * viewName : 哈哈
     * nickname : 哈哈
     * userId : 433
     * userImage : http://somicshop.oss-cn-shenzhen.aliyuncs
     * .com/attached/images/201611/23e68d3f7839492b900982f559b3343b74207.jpg
     * userName : M000000420
     */

    private RxAccount account;
    private boolean hasReply;
    private RxPlateFormReply plateformReply;
    /**
     * awardScore : 1000
     * awardRemark : 系的不错
     * postId : 683
     */

    private RxPostAward postAward;
    /**
     * postAward : {"awardScore":1000,"awardRemark":"系的不错","postId":683}
     * isAward : true
     */

    private boolean isAward;
    private int itemType;
    private int order;

    public RxPost getPost() {
        return post;
    }

    public void setPost(RxPost post) {
        this.post = post;
    }

    public boolean getIsAttented() {
        return isAttented;
    }

    public void setIsAttented(boolean isAttented) {
        this.isAttented = isAttented;
    }

    public RxPlate getPlate() {
        return plate;
    }

    public void setPlate(RxPlate plate) {
        this.plate = plate;
    }

    @Bindable
    public boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
        isZan.set(isLiked);
        notifyPropertyChanged(BR.isLiked);
    }

    public boolean getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    public RxAccount getAccount() {
        return account;
    }

    public void setAccount(RxAccount account) {
        this.account = account;
    }

    public boolean getHasReply() {
        return hasReply;
    }

    public void setHasReply(boolean hasReply) {
        this.hasReply = hasReply;
    }

    public RxPlateFormReply getPlateFormReply() {
        return plateformReply;
    }

    public void setPlateFormReply(RxPlateFormReply plateFormReply) {
        this.plateformReply = plateFormReply;
    }

    @Override
    public String toString() {
        return "{" +
                "post=" + post +
                ", isAttented=" + isAttented +
                ", plate=" + plate +
                ", isLiked=" + isLiked +
                ", isOwner=" + isOwner +
                ", account=" + account +
                ", hasReply=" + hasReply +
                '}';
    }

    public RxPostAward getPostAward() {
        return postAward;
    }

    public void setPostAward(RxPostAward postAward) {
        this.postAward = postAward;
    }

    public boolean isAward() {
        return isAward;
    }

    public void setIsAward(boolean isAward) {
        this.isAward = isAward;
    }

    @Override
    public int getItemType() {
        return getPost().getPostType();
    }

    @Bindable
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
        notifyPropertyChanged(BR.order);
    }

//    public void setItemType(int itemType) {
//        this.itemType = itemType;
//    }
}
