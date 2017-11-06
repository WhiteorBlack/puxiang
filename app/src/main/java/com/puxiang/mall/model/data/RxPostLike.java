package com.puxiang.mall.model.data;

public class RxPostLike {

    /**
     * likeTime : 2016-12-01 09:23:19
     * likeUser : {"birthday":"","sex":null,"viewName":"本子","nickname":"本子","userId":44,"userImage":"http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201611/f23c317883e34d0a92eaeb456e0b4d5802517.jpg","userName":"M000000034"}
     * isAttented : true
     * postUserId : 101
     * isOwner : false
     * likeUserId : 44
     * commentUserId : 0
     * commentId : 0
     * postId : 2928
     */

    private String likeTime;
    /**
     * birthday :
     * sex : null
     * viewName : 本子
     * nickname : 本子
     * userId : 44
     * userImage : http://somicshop.oss-cn-shenzhen.aliyuncs.com/attached/images/201611/f23c317883e34d0a92eaeb456e0b4d5802517.jpg
     * userName : M000000034
     */

    private RxUserInfo likeUser;
    private String isAttented;
    private int postUserId;
    private String isOwner;
    private String likeUserId;
    private int commentUserId;
    private int commentId;
    private int postId;

    public String getLikeTime() {
        return likeTime;
    }

    public void setLikeTime(String likeTime) {
        this.likeTime = likeTime;
    }

    public RxUserInfo getLikeUser() {
        return likeUser;
    }

    public void setLikeUser(RxUserInfo likeUser) {
        this.likeUser = likeUser;
    }

    public String getIsAttented() {
        return isAttented;
    }

    public void setIsAttented(String isAttented) {
        this.isAttented = isAttented;
    }

    public int getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(int postUserId) {
        this.postUserId = postUserId;
    }

    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

    public String getLikeUserId() {
        return likeUserId;
    }

    public void setLikeUserId(String likeUserId) {
        this.likeUserId = likeUserId;
    }

    public int getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(int commentUserId) {
        this.commentUserId = commentUserId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

}
