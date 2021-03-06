package com.puxiang.mall.network;

import android.databinding.ObservableField;

import com.puxiang.mall.model.data.HttpResult;
import com.puxiang.mall.model.data.LocationInfo;
import com.puxiang.mall.model.data.RxAds;
import com.puxiang.mall.model.data.RxAttentionUserData;
import com.puxiang.mall.model.data.RxAuthorizeUserInfo;
import com.puxiang.mall.model.data.RxBatchList;
import com.puxiang.mall.model.data.RxBatchTotalPrice;
import com.puxiang.mall.model.data.RxCartList;
import com.puxiang.mall.model.data.RxCatalog;
import com.puxiang.mall.model.data.RxChannel;
import com.puxiang.mall.model.data.RxCheck;
import com.puxiang.mall.model.data.RxCityArea;
import com.puxiang.mall.model.data.RxClassfy;
import com.puxiang.mall.model.data.RxComment;
import com.puxiang.mall.model.data.RxCommentInfo;
import com.puxiang.mall.model.data.RxCommentReply;
import com.puxiang.mall.model.data.RxDealer;
import com.puxiang.mall.model.data.RxDealerCheck;
import com.puxiang.mall.model.data.RxDealerList;
import com.puxiang.mall.model.data.RxEsportList;
import com.puxiang.mall.model.data.RxFans;
import com.puxiang.mall.model.data.RxIntegral;
import com.puxiang.mall.model.data.RxIntegralAccount;
import com.puxiang.mall.model.data.RxIntegralProduct;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxLocation;
import com.puxiang.mall.model.data.RxMallInfo;
import com.puxiang.mall.model.data.RxMessageState;
import com.puxiang.mall.model.data.RxMyUserInfo;
import com.puxiang.mall.model.data.RxOrderState;
import com.puxiang.mall.model.data.RxPayChannel;
import com.puxiang.mall.model.data.RxPayPrice;
import com.puxiang.mall.model.data.RxPlate;
import com.puxiang.mall.model.data.RxPlateType;
import com.puxiang.mall.model.data.RxPostAddress;
import com.puxiang.mall.model.data.RxPostComment;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.model.data.RxPostLike;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.model.data.RxRefund;
import com.puxiang.mall.model.data.RxReply;
import com.puxiang.mall.model.data.RxReplyPostComment;
import com.puxiang.mall.model.data.RxShop;
import com.puxiang.mall.model.data.RxShopList;
import com.puxiang.mall.model.data.RxShuohuInfo;
import com.puxiang.mall.model.data.RxSing;
import com.puxiang.mall.model.data.RxTicket;
import com.puxiang.mall.model.data.RxUnreadMessage;
import com.puxiang.mall.model.data.RxUploadUrl;
import com.puxiang.mall.model.data.RxUserCommunity;
import com.puxiang.mall.model.data.RxUserInfo;
import com.puxiang.mall.model.data.RxWXPayInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {
    //获取地理位置
    @GET("http://maps.google.cn/maps/api/geocode/json")
    Observable<LocationInfo> getLocationInfo(@Query("latlng") String latlng,
                                             @Query("language") String language,
                                             @Query("sensor") boolean sensor);

    //多文件流上传
    @Multipart
    @POST("alibaba/oss/appUploadImages.do")
    Observable<HttpResult<List<RxUploadUrl>>> upload(@PartMap Map<String, RequestBody> params);

    //多文件流上传
    @Multipart
    @POST("alibaba/oss/appUploadImages.do")
    Observable<HttpResult<List<RxUploadUrl>>> upload(@Part MultipartBody.Part file);

    //获取商品详情
    @GET("mall/product/getProduct.do")
    Observable<HttpResult<RxProduct>> getProduct(@Query("productId") String id);

    //获取下载图片
    @Streaming
    @GET
    Observable<ResponseBody> downloadPicFromNet(@Url String fileUrl);

    //获取积分商品详情
    @GET("mall/integral/product/getProduct.do")
    Observable<HttpResult<RxIntegralProduct>> getIntegralProduct(@Query("productId") String id);

    //获取硕虎信息
    @GET("shuohu/getShuohuInfo.do ")
    Observable<HttpResult<RxShuohuInfo>> getShuohuInfo();

    //获取分享连接
    @GET("weixin/getOauthUrlForApp.do")
    Observable<HttpResult<String>> getShareUrl(@Query("url") String url);

//    //上传设备信息
//    @FormUrlEncoded
//    @POST("http://open2.lingju.tech:8999/httpapi/ljchat.do")
//    Observable<String> getLiju(@Field("accessToken") String accessToken,
//                               @Field("userId") String userId);

    //上传设备信息
    @FormUrlEncoded
    @POST("analysis/appInstall/uplaodAppInstallDeviceInfo.do")
    Observable<HttpResult<String>> uploadDeviceInfo(@Field("type") String type,
                                                    @Field("version") String version,
                                                    @Field("machineId") String machineId);

    //检测手机号码
    @GET("mall/member/mobile/checkMobile.do")
    Observable<HttpResult<String>> checkMobile(@Query("mobile") String mobile);

    //获取朋友圈帖子
    @FormUrlEncoded
    @POST("community/post/friendPosts.do")
    Observable<HttpResult<RxList<RxPostInfo>>> friendPosts(@Field("userId") String userId,
                                                           @Field("token") String token,
                                                           @Field("pageSize") int pageSize,
                                                           @Field("pageNo") int pageNo);

    //获取多个圈子
    @FormUrlEncoded
    @POST("community/plate/getPlates.do")
    Observable<HttpResult<RxList<RxPlate>>> getPlates(@Field("pageSize") int pageSize,
                                                      @Field("pageNo") int pageNo);

    //获取融云Token
    @FormUrlEncoded
    @POST("rongCloud/getToken.do")
    Observable<HttpResult<String>> getRYToken(@Field("userId") String userId,
                                              @Field("token") String token);

    //刷新融云Token
    @FormUrlEncoded
    @POST("rongCloud/refreshToken.do")
    Observable<HttpResult<String>> refreshToken(@Field("userId") String userId,
                                                @Field("token") String token);

    //登录
    @FormUrlEncoded
    @POST("mall/member/loginAndroid.do")
    Observable<HttpResult<RxMyUserInfo>> login(@Field("userName") String userName,
                                               @Field("password") String password);

    //登录
    @FormUrlEncoded
    @POST("mall/member/loginAndroid.do")
    Observable<HttpResult<RxMyUserInfo>> login(@Field("userName") String userName,
                                               @Field("password") String password,
                                               @Field("") String invateCode);

//    //检测是否是代理人
//    @FormUrlEncoded
//    @POST("agent/user/checkUserIsAgent.do")
//    Observable<HttpResult<RxIsAgent>> checkUserIsAgent(@Field("userId") String userId,
//                                                       @Field("token") String token);

    //注册
    @FormUrlEncoded
    @POST("mall/member/registerAndroid.do")
    Observable<HttpResult<String>> register(@Field("mobile") String mobile,
                                            @Field("code") String code,
                                            @Field("password") String password,
                                            @Field("macAddress") String macAddress);

    //注册
    @FormUrlEncoded
    @POST("mall/member/registerAndroid.do")
    Observable<HttpResult<String>> register(@Field("mobile") String mobile,
                                            @Field("code") String code,
                                            @Field("password") String password,
                                            @Field("macAddress") String macAddress,
                                            @Field("refCode") String invateCode);

    //微信第三方登录
    @FormUrlEncoded
    @POST("third/weixin/authorize.do")
    Observable<HttpResult<RxAuthorizeUserInfo>> wechatAuthorize(@Field("code") String code,
                                                                @Field("machineType") int
                                                                        machineType);

    //qq第三方登录
    @FormUrlEncoded
    @POST("third/qq/authorize.do")
    Observable<HttpResult<RxAuthorizeUserInfo>> qqAuthorize(@FieldMap Map<String, String> params);

    //新浪微博第三方登录
    @FormUrlEncoded
    @POST("third/weibo/authorize.do")
    Observable<HttpResult<RxAuthorizeUserInfo>> weiboAuthorize(@FieldMap Map<String, String>
                                                                       params);

    //绑定手机号
    @FormUrlEncoded
    @POST("third/bindingMobile.do")
    Observable<HttpResult<RxAuthorizeUserInfo>> bindingMobile(@Field("code") String code,
                                                              @Field("mobile") String mobile);

    //获取我的信息
    @FormUrlEncoded
    @POST("mall/member/userInfo/getUserInfo.do")
    Observable<HttpResult<RxMyUserInfo>> getMyUserInfo(@Field("userId") String userId,
                                                       @Field("token") String token);

    //获取积分任务
    @FormUrlEncoded
    @POST("mall/integral/task/getIntegralTask.do")
    Observable<HttpResult<List<RxIntegral>>> getIntegralTask(@Field("userId") String userId,
                                                             @Field("token") String token);

    //获取账户积分信息
    @FormUrlEncoded
    @POST("mall/integral/IntegralAccount/getIntegralAccount.do")
    Observable<HttpResult<RxIntegralAccount>> getIntegralAccount(@Field("userId") String userId,
                                                                 @Field("token") String token);

    //获取视频列表
    @GET("community/post/officialVideos.do")
    Observable<HttpResult<RxList<RxPostInfo>>> officialVideos(@Query("typeId") int typeId,
                                                              @Query("pageSize") int pageSize,
                                                              @Query("pageNo") int pageNo);

    //获取商品目录列表
    @GET("mall/product/catalog/getCatalogs.do")
    Observable<HttpResult<List<RxCatalog>>> getCatalogs(@Query("parentTypeId") int parentTypeId);

//    //获取目录商品
//    @GET("mall/search/searchProduct.do")
//    Observable<HttpResult<RxList<RxProduct>>> searchProduct(@Query("catalogId") int catalogId,
//                                                            @Query("pageSize") int pageSize);

    //获取来源帖子
    @GET("community/post/officialPosts.do")
    Observable<HttpResult<RxList<RxPostInfo>>> officialPosts(@Query("typeId") String typeId,
                                                             @Query("pageSize") int pageSize,
                                                             @Query("pageNo") int pageNo);

    //获取最新帖子列表(首页)
    @GET("community/post/newestPosts.do")
    Observable<HttpResult<RxList<RxPostInfo>>> newestPosts(@Query("typeId") String typeId,
                                                           @Query("pageSize") int pageSize,
                                                           @Query("pageNo") int pageNo);

    //搜索商品
    @GET("mall/search/searchProduct.do")
    Observable<HttpResult<RxList<RxProduct>>> searchProduct(@Query("keyword") String keyword,
                                                            @Query("categoryId") String catalogId,
                                                            @Query("orderBy") String orderBy,
                                                            @Query("order") String order,
                                                            @Query("pageSize") int pageSize,
                                                            @Query("pageNo") int pageNo,
                                                            @Query("userId") String userid);

    //获取商家商品
    @GET("mall/search/searchProduct.do")
    Observable<HttpResult<RxList<RxProduct>>> searchShopProduct(
            @Query("keyword") String keyword,
            @Query("shopId") String shopId,
            @Query("orderBy") String orderBy,
            @Query("order") String order,
            @Query("pageSize") int pageSize,
            @Query("pageNo") int pageNo);

    //搜索帖子
    @GET("community/search/searchPost.do")
    Observable<HttpResult<RxList<RxPostInfo>>> searchPost(@Query("keyword") String keyword,
                                                          @Query("pageSize") int pageSize,
                                                          @Query("pageNo") int pageNo);

    //搜索圈子
    @GET("community/search/searchPlate.do")
    Observable<HttpResult<RxList<RxPlate>>> searchPlate(@Query("keyword") String keyword,
                                                        @Query("pageSize") int pageSize,
                                                        @Query("pageNo") int pageNo);

    //搜索用户
    @GET("mall/search/member/searchMember.do")
    Observable<HttpResult<RxList<RxUserInfo>>> searchMember(@Query("keyword") String keyword,
                                                            @Query("pageSize") int pageSize,
                                                            @Query("pageNo") int pageNo);

    //获取购物车商品列表
    @FormUrlEncoded
    @POST("mall/member/cart/getCarts.do")
    Observable<HttpResult<List<RxCartList>>> getCarts(@Field("userId") String userId,
                                                      @Field("token") String token);

    //修改购物车商品信息
    @FormUrlEncoded
    @POST("mall/member/cart/modifyCart.do")
    Observable<HttpResult<String>> modifyCart(@Field("cartId") String cartId,
                                              @Field("proudctId") String proudctId,
                                              @Field("buyQty") int productNum,
                                              @Field("userId") String userId,
                                              @Field("token") String token);

    //删除购物车商品
    @FormUrlEncoded
    @POST("mall/member/cart/deleteCart.do")
    Observable<HttpResult<String>> deleteCart(@Field("cartIds") String cartId,
                                              @Field("userId") String userId,
                                              @Field("token") String token);

    //删除购物车商品
    @FormUrlEncoded
    @POST("mall/member/cart/deleteCart.do")
    Observable<HttpResult<String>> deleteCartShop(@Field("cartId") String cartId,
                                                  @Field("userId") String userId,
                                                  @Field("token") String token);

    //获取广告信息数据
    @GET("getAds.do")
    Observable<HttpResult<List<RxAds>>> getAds(@Query("channelCode") String channelCode);

    //查询用户信息
    @GET("community/userCommunity/getCommunityUserInfo.do")
    Observable<HttpResult<RxUserInfo>> getCommunityUserInfo(@Query("userId") String userId);

    //获取手机验证码
    @GET("sms/getSmsCode.do")
    Observable<HttpResult<String>> getSmsCode(@Query("mobile") String mobile);


    //获取商城推荐商品
    @GET("mall/product/index/getRecommendProducts.do")
    Observable<HttpResult<RxMallInfo>> getRecommendProducts(@Query("recommendType") String recommendType);

    //获取商城推荐商品
    @GET("mall/product/index/getRecommendProducts.do")
    Observable<HttpResult<List<RxProduct>>> getRecommendProducts();

    //热门搜索关键字
    @GET("mall/search/getHotKeywords.do")
    Observable<HttpResult<List<String>>> getHotKeywords();

    //获取所有的圈子以及分类信息
    @GET("community/plate/allPlates.do")
    Observable<HttpResult<List<RxPlateType>>> allPlates();

    //获取社区频道列表
    @GET("community/channel/getChannels.do")
    Observable<HttpResult<List<RxChannel>>> getChannels();

    //获取购物车数量
    @FormUrlEncoded
    @POST("mall/member/cart/getCartNum.do")
    Observable<HttpResult<Integer>> getCartNum(@Field("userId") String userId,
                                               @Field("token") String token);

    //签到
    @FormUrlEncoded
    @POST("mall/integral/IntegralAccount/userSigned.do")
    Observable<HttpResult<String>> userSigned(@Field("userId") String userId,
                                              @Field("token") String token);

    //查询用户昵称是否可以修改
    @FormUrlEncoded
    @POST("mall/member/userInfo/checkNickname.do")
    Observable<HttpResult<RxCheck>> checkNickname(@Field("userId") String userId,
                                                  @Field("token") String token);

    //帖子详情
    @FormUrlEncoded
    @POST("community/post/postDetail.do")
    Observable<HttpResult<RxPostInfo>> postDetail(@Field("postId") String plateId,
                                                  @Field("userId") String userId,
                                                  @Field("token") String token);

    //帖子点赞
    @FormUrlEncoded
    @POST("community/postLike/praise.do")
    Observable<HttpResult<String>> postLike(@Field("postId") String plateId,
                                            @Field("userId") String userId,
                                            @Field("token") String token);

    //取消帖子点赞
    @FormUrlEncoded
    @POST("community/postLike/cancelPraise.do")
    Observable<HttpResult<String>> cancelPostLike(@Field("postId") String plateId,
                                                  @Field("userId") String userId,
                                                  @Field("token") String token);

    //评论点赞
    @FormUrlEncoded
    @POST("community/postCommentLike/praise.do")
    Observable<HttpResult<String>> commentLike(@Field("commentId") String commentId,
                                               @Field("userId") String userId,
                                               @Field("token") String token);

    //取消评论点赞
    @FormUrlEncoded
    @POST("community/postCommentLike/cancelPraise.do")
    Observable<HttpResult<String>> cancelCommentLike(@Field("commentId") String commentId,
                                                     @Field("userId") String userId,
                                                     @Field("token") String token);

    //修改登录密码
    @FormUrlEncoded
    @POST("mall/member/password/modifyLoginPassword.do")
    Observable<HttpResult<String>> modifyLoginPassword(@Field("mobile") String mobile,
                                                       @Field("code") String code,
                                                       @Field("newPassword") String newPassword);

    //修改生日
    @FormUrlEncoded
    @POST("mall/member/userInfo/modifyUserInfo.do")
    Observable<HttpResult<String>> modifyBirthday(@Field("birthday") String birthday,
                                                  @Field("userId") String userId,
                                                  @Field("token") String token);

    //修改真实姓名
    @FormUrlEncoded
    @POST("mall/member/userInfo/modifyUserInfo.do")
    Observable<HttpResult<String>> modifyRealName(@Field("realName") String realName,
                                                  @Field("userId") String userId,
                                                  @Field("token") String token);

    //修改邮箱地址
    @FormUrlEncoded
    @POST("mall/member/userInfo/modifyUserInfo.do")
    Observable<HttpResult<String>> modifyEmail(@Field("email") String email,
                                               @Field("userId") String userId,
                                               @Field("token") String token);

    //修改昵称
    @FormUrlEncoded
    @POST("mall/member/userInfo/modifyNickname.do")
    Observable<HttpResult<String>> modifyNickname(@Field("nickname") String nickname,
                                                  @Field("userId") String userId,
                                                  @Field("token") String token);

    //修改性别
    @FormUrlEncoded
    @POST("mall/member/userInfo/modifyUserInfo.do")
    Observable<HttpResult<String>> modifySex(@Field("sex") String sex,
                                             @Field("userId") String userId,
                                             @Field("token") String token);

    //修改用户头像
    @FormUrlEncoded
    @POST("mall/member/userInfo/modifyUserInfo.do")
    Observable<HttpResult<String>> modifyUserImage(@Field("userImage") String sex,
                                                   @Field("userId") String userId,
                                                   @Field("token") String token);

    //删除收货地址
    @FormUrlEncoded
    @POST("mall/member/deliveryAddr/deleteAddress.do")
    Observable<HttpResult<String>> deleteAddress(@Field("addressId") String addressId,
                                                 @Field("userId") String userId,
                                                 @Field("token") String token);

    //获取消息状态
    @FormUrlEncoded
    @POST("message/getMessageState.do")
    Observable<HttpResult<RxMessageState>> getMessageState(@Field("userId") String userId,
                                                           @Field("token") String token);

    //获取未读消息
    @FormUrlEncoded
    @POST("sysMessage/getUnreadMessageCount.do")
    Observable<HttpResult<List<RxUnreadMessage>>> getUnreadMessage(@Field("userId") String userId,
                                                                   @Field("token") String token);

    //分享成功回调
    @FormUrlEncoded
    @POST("mall/member/share/share.do")
    Observable<HttpResult<String>> shareOK(@Field("shareLink") String shareLink,
                                           @Field("userId") String userId,
                                           @Field("token") String token);

    //获取支付信息
    @GET("payChannel/getPayChannel.do")
    Observable<HttpResult<List<RxPayChannel>>> getPayChannel();

    //获取支付信息
    @FormUrlEncoded
    @POST("pay/getPayInfo.do")
    Observable<HttpResult<RxPayPrice>> getPayInfo(@Field("orderType") String orderType,
                                                  @Field("orderIds") String shareLink,
                                                  @Field("userId") String userId,
                                                  @Field("token") String token);

    //获取银联支付签名
    @FormUrlEncoded
    @POST("pay/unionpay/app/pay.do")
    Observable<HttpResult<String>> unionpaySign(@Field("orderType") int orderType,
                                                @Field("orderIds") String orderId,
                                                @Field("userId") String userId,
                                                @Field("token") String token);

    //获取支付宝支付签名
    @FormUrlEncoded
    @POST("pay/alipay/app/pay.do")
    Observable<HttpResult<String>> alipaySign(@Field("orderType") String orderType,
                                              @Field("orderIds") String orderId,
                                              @Field("userId") String userId,
                                              @Field("token") String token);


    //获取微信支付签名
    @FormUrlEncoded
    @POST("pay/weixin/app/pay.do")
    Observable<HttpResult<RxWXPayInfo>> weixinPaySign(@Field("orderType") String orderType,
                                                      @Field("appType") int appType,
                                                      @Field("orderIds") String orderId,
                                                      @Field("userId") String userId,
                                                      @Field("token") String token);

    //圈子详情
    @FormUrlEncoded
    @POST("community/plate/getPlateDetail.do")
    Observable<HttpResult<RxPlate>> getPlateDetail(@Field("plateId") String plateId,
                                                   @Field("userId") String userId,
                                                   @Field("token") String token);

    //获取圈子帖子列表
    @FormUrlEncoded
    @POST("community/post/platePosts.do")
    Observable<HttpResult<RxList<RxPostInfo>>> platePosts(@Field("userId") String userId,
                                                          @Field("token") String token,
                                                          @Field("plateId") String plateId,
                                                          @Field("pageSize") int pageSize,
                                                          @Field("pageNo") int pageNo);

    //获取单个评论
    @FormUrlEncoded
    @POST("community/postComment/getPostComment.do")
    Observable<HttpResult<RxPostComment>> getPostComment(@Field("userId") String userId,
                                                         @Field("token") String token,
                                                         @Field("commentId") String commentId);

    //评论
    @FormUrlEncoded
    @POST("community/postComment/comment.do")
    Observable<HttpResult<RxComment>> comment(@Field("postId") String postId,
                                              @Field("comment") String comment,
                                              @Field("userId") String userId,
                                              @Field("token") String token);

    //回复评论
    @FormUrlEncoded
    @POST("community/postComment/reply.do")
    Observable<HttpResult<RxCommentReply>> reply(@Field("commentId") String commentId,
                                                 @Field("byReplyUserId") String byReplyUserId,
                                                 @Field("reply") String reply,
                                                 @Field("userId") String userId,
                                                 @Field("token") String token);

    //获取评论回复列表
    @FormUrlEncoded
    @POST("community/postComment/getCommentReplys.do")
    Observable<HttpResult<RxList<RxReply>>> getCommentReplys(@Field("commentId") String plateId,
                                                             @Field("pageSize") int pageSize,
                                                             @Field("pageNo") int pageNo);

    //获取点赞列表
    @GET("community/postLike/getPraisePost.do")
    Observable<HttpResult<RxList<RxPostLike>>> getPraisePost(@Query("postId") String postId,
                                                             @Query("pageSize") int pageSize,
                                                             @Query("pageNo") int pageNo);

    //获取帖子评论列表
    @FormUrlEncoded
    @POST("community/postComment/getPostComments.do")
    Observable<HttpResult<RxList<RxCommentInfo>>> getPostComments(@Field("postId") String postId,
                                                                  @Field("userId") String userId,
                                                                  @Field("token") String token,
                                                                  @Field("orderBy") int orderBy,
                                                                  @Field("pageSize") int pageSize,
                                                                  @Field("pageNo") int pageNo);

    //查阅信息
    @FormUrlEncoded
    @POST("message/setMessageReadTime.do")
    Observable<HttpResult<String>> setMessageReadTime(@Field("messageCode") String messageCode,
                                                      @Field("userId") String userId,
                                                      @Field("token") String token);

    //发帖
    @FormUrlEncoded
    @POST("community/post/publish.do")
    Observable<HttpResult<String>> publish(@Field("userId") String userId,
                                           @Field("token") String token,
                                           @Field("picUrl") String picUrl,
                                           @Field("orderDetailId") String orderDetailId,
                                           @Field("plateId") String plateId,
                                           @Field("content") String content);

    //关注的人
    @FormUrlEncoded
    @POST("community/attentionUser/getAttentionUsers.do")
    Observable<HttpResult<RxList<RxAttentionUserData>>> getAttentionUsers(@Field("myUserId")
                                                                                  String myUserId,
                                                                          @Field("token") String
                                                                                  token,
                                                                          @Field("otherUserId")
                                                                                  String
                                                                                  otherUserId,
                                                                          @Field("pageSize") int
                                                                                  pageSize,
                                                                          @Field("pageNo") int
                                                                                  pageNo);

    //用户粉丝
    @FormUrlEncoded
    @POST("community/myFans/userFans.do")
    Observable<HttpResult<RxList<RxFans>>> userFans(@Field("myUserId") String myUserId,
                                                    @Field("token") String token,
                                                    @Field("otherUserId") String otherUserId,
                                                    @Field("pageSize") int pageSize,
                                                    @Field("pageNo") int pageNo);

    //用户加入的圈子
    @FormUrlEncoded
    @POST("community/plate/userPlates.do")
    Observable<HttpResult<RxList<RxPlate>>> userPlates(@Field("myUserId") String myUserId,
                                                       @Field("token") String token,
                                                       @Field("otherUserId") String otherUserId,
                                                       @Field("pageSize") int pageSize,
                                                       @Field("pageNo") int pageNo);


    //用户发的帖子
    @FormUrlEncoded
    @POST("community/post/myPosts.do")
    Observable<HttpResult<RxList<RxPostInfo>>> myPosts(@Field("myUserId") String myUserId,
                                                       @Field("token") String token,
                                                       @Field("otherUserId") String otherUserId,
                                                       @Field("pageSize") int pageSize,
                                                       @Field("pageNo") int pageNo);

    //回复的帖子评论
    @FormUrlEncoded
    @POST("community/postComment/getReplyPostComments.do")
    Observable<HttpResult<RxList<RxReplyPostComment>>> getReplyPostComments(@Field("myUserId")
                                                                                    String myUserId,
                                                                            @Field("token")
                                                                                    String token,
                                                                            @Field("otherUserId")
                                                                                    String
                                                                                    otherUserId,
                                                                            @Field("pageSize")
                                                                                    int pageSize,
                                                                            @Field("pageNo") int
                                                                                    pageNo);

    //用户社区
    @FormUrlEncoded
    @POST("community/userCommunity/getUserCommunity.do")
    Observable<HttpResult<RxUserCommunity>> getUserCommunity(@Field("userId") String userId,
                                                             @Field("token") String token,
                                                             @Field("friendId") String otherUserId);

    //微信第三方登录
    @FormUrlEncoded
    @POST("weixin/app/authorizeAndroid.do")
    Observable<HttpResult<RxMyUserInfo>> authorizeAndroid(@Field("code") String code);

    //关注
    @FormUrlEncoded
    @POST("community/attentionUser/attent.do")
    Observable<HttpResult<String>> attent(@Field("userId") String userId,
                                          @Field("token") String token,
                                          @Field("myAttentionUserId") String myAttentionUserId);

    //取消关注
    @FormUrlEncoded
    @POST("community/attentionUser/cancelAttent.do")
    Observable<HttpResult<String>> cancelAttent(@Field("userId") String userId,
                                                @Field("token") String token,
                                                @Field("myAttentionUserId") String
                                                        myAttentionUserId);

    //加入圈子
    @FormUrlEncoded
    @POST("community/plate/joinPlate.do")
    Observable<HttpResult<String>> joinPlate(@Field("userId") String userId,
                                             @Field("token") String token,
                                             @Field("plateId") String plateId);

    //退出圈子
    @FormUrlEncoded
    @POST("community/plate/exitPlate.do")
    Observable<HttpResult<String>> exitPlate(@Field("userId") String userId,
                                             @Field("token") String token,
                                             @Field("plateId") String plateId);

    //用户收藏帖子
    @FormUrlEncoded
    @POST("community/post/collectPosts.do")
    Observable<HttpResult<RxList<RxPostInfo>>> collectPosts(@Field("myUserId") String myUserId,
                                                            @Field("token") String token,
                                                            @Field("otherUserId") String
                                                                    otherUserId,
                                                            @Field("pageSize") int pageSize,
                                                            @Field("pageNo") int pageNo);

    //取消收藏帖子
    @FormUrlEncoded
    @POST("community/postCollection/cancelCollect.do")
    Observable<HttpResult<String>> cancelCollect(@Field("userId") String userId,
                                                 @Field("token") String token,
                                                 @Field("postId") String postId);

    //收藏帖子
    @FormUrlEncoded
    @POST("community/postCollection/collect.do")
    Observable<HttpResult<String>> collect(@Field("userId") String userId,
                                           @Field("token") String token,
                                           @Field("postId") String postId);

    //是否收藏该帖子
    @FormUrlEncoded
    @POST("community/postCollection/isCollected.do")
    Observable<HttpResult<Integer>> isCollected(@Field("userId") String userId,
                                                @Field("token") String token,
                                                @Field("postId") String postId);

    //获取订单详情
    @FormUrlEncoded
    @POST("mall/trade/order/getOrderDetail.do")
    Observable<HttpResult<Integer>> getOrderDetail(@Field("detailId") String detailId,
                                                   @Field("token") String token,
                                                   @Field("postId") String postId);

    //删除帖子
    @FormUrlEncoded
    @POST("community/post/deletePost.do")
    Observable<HttpResult<String>> deletePost(@Field("userId") String userId,
                                              @Field("token") String token,
                                              @Field("postId") String postId);

    //删除帖子
    @FormUrlEncoded
    @POST("community/postComment/deleteComment.do")
    Observable<HttpResult<String>> deleteComment(@Field("userId") String userId,
                                                 @Field("token") String token,
                                                 @Field("commentId") String commentId);

    //退款申请
    @FormUrlEncoded
    @POST("mall/refund/refundApplication/goForRefunding.do")
    Observable<HttpResult<RxRefund>> refund(@Field("userId") String userId,
                                            @Field("token") String token,
                                            @Field("orderDetailId") String orderDetailId);

    //提交退款申请
    @FormUrlEncoded
    @POST("mall/refund/refundApplication/submitRefundApplication.do")
    Observable<HttpResult<String>> submitRefund(@Field("userId") String userId,
                                                @Field("token") String token,
                                                @Field("refundReasonId") int refundReasonId,
                                                @Field("productId") String productId,
                                                @Field("orderId") String orderId,
                                                @Field("orderDetailId") String orderDetailId,
                                                @Field("refundAmount") String refundAmount,
                                                @Field("refundRemark") String refundRemark,
                                                @Field("refundType") String refundType,
                                                @Field("refundVoucherPic") String refundVoucherPic);

    //检测打卡区域
    @FormUrlEncoded
    @POST("esport/checkCard/checkUserInShop.do")
    Observable<HttpResult<RxSing>> checkUserInShop(@Field("userId") String userId,
                                                   @Field("token") String token,
                                                   @Field("esportShopId") String esportShopId,
                                                   @Field("latitude") double latitude,
                                                   @Field("longitude") double longitude);

    //电竞馆签到
    @FormUrlEncoded
    @POST("esport/checkCard/checkCard.do")
    Observable<HttpResult<RxSing>> checkCard(@Field("userId") String userId,
                                             @Field("token") String token,
                                             @Field("esportShopId") String esportShopId,
                                             @Field("latitude") double latitude,
                                             @Field("longitude") double longitude,
                                             @Field("qrCode") String qrCode);

    //电竞馆列表
    @GET("esport/shop/search.do")
    Observable<HttpResult<RxEsportList>> getEsportList(@Query("latitude") double latitude,
                                                       @Query("longitude") double longitude,
                                                       @Query("pageNo") int pageNo,
                                                       @Query("pageSize") int pageSize);

    //已关注的电竞馆列表
    @FormUrlEncoded
    @POST("esport/shop/attentedShop.do")
    Observable<HttpResult<RxEsportList>> getAttentEsportList(@Field("userId") String userId,
                                                             @Field("token") String token);

    //获取商铺列表
    @FormUrlEncoded
    @POST("mall/shop/getShops.do")
    Observable<HttpResult<RxShopList>> getShopList(@Field("pageNo") String pageNo,
                                                   @Field("pageSize") String pageSize,
                                                   @Field("keyword") String keyword,
                                                   @Field("areaCode") String areaCode,
                                                   @Field("areaName") String areaName,
                                                   @Field("orderBy") String orderBy);

    //获取定位信息
    @FormUrlEncoded
    @POST("location/getLocation.do")
    Observable<HttpResult<RxLocation>> getLocation(@Field("lat") double lat,
                                                   @Field("lng") double lng);

    //获取商家详情
    @FormUrlEncoded
    @POST("mall/shop/getShop.do")
    Observable<HttpResult<RxShop>> getShopDetial(@Field("shopId") String shopId);

    //获取进货商品列表
    @FormUrlEncoded
    @POST("mall/product/getBatchProducts.do")
    Observable<HttpResult<RxBatchList>> getBatchProducts(@Field("pageNo") String pageNo,
                                                         @Field("pageSize") String pageSize,
                                                         @Field("keyword") String keyword,
                                                         @Field("order") String areaCode,
                                                         @Field("orderBy") String orderBy,
                                                         @Field("userId") String userId,
                                                         @Field("token") String token);

    //获取进货商品列表
    @FormUrlEncoded
    @POST("mall/product/countBatchSingleTotalPrice.do")
    Observable<HttpResult<RxBatchTotalPrice>> countBatchSingleTotalPrice(@Field("productId") String productId,
                                                                         @Field("buyQty") int buyQty,
                                                                         @Field("userId") String userId,
                                                                         @Field("token") String token);

    //获取城市列表
    @FormUrlEncoded
    @POST("mall/area/getCityChild.do")
    Observable<HttpResult<List<RxCityArea>>> getAllChildArea(@Field("areaCode") String areaCode);

    //获取融云TOKEN
    @FormUrlEncoded
    @POST("rongCloud/getToken.do")
    Observable<HttpResult<String>> getRongToken(@Field("userId") String userId,
                                                @Field("token") String token);

    //获取收货地址
    @FormUrlEncoded
    @POST("mall/member/deliveryAddr/getAddresses.do")
    Observable<HttpResult<List<RxPostAddress>>> getAddresses(@Field("userId") String userId,
                                                             @Field("token") String token);


    //修改收货地址
    @FormUrlEncoded
    @POST("mall/member/deliveryAddr/modifyAddress.do")
    Observable<HttpResult<String>> modifyAddress(@Field("userId") String userId,
                                                 @Field("token") String token,
                                                 @Field("addressId") String addressId,
                                                 @Field("shipName") String shipName,
                                                 @Field("shipAddress") String shipAddress,
                                                 @Field("phone") String phone,
                                                 @Field("province") String province,
                                                 @Field("provinceCode") String provinceCode,
                                                 @Field("city") String city,
                                                 @Field("cityCode") String cityCode,
                                                 @Field("area") String area,
                                                 @Field("areaCode") String areaCode,
                                                 @Field("tel") String tel,
                                                 @Field("isDefault") String isDefault);

    //添加收货地址
    @FormUrlEncoded
    @POST("mall/member/deliveryAddr/addAddress.do")
    Observable<HttpResult<String>> addAddress(@Field("userId") String userId,
                                              @Field("token") String token,
                                              @Field("shipName") String shipName,
                                              @Field("detailAddress") String shipAddress,
                                              @Field("phone") String phone,
                                              @Field("province") String province,
                                              @Field("provinceCode") String provinceCode,
                                              @Field("city") String city,
                                              @Field("cityCode") String cityCode,
                                              @Field("area") String area,
                                              @Field("areaCode") String areaCode,
                                              @Field("tel") String tel,
                                              @Field("isDefault") String isDefault);

    @FormUrlEncoded
    @POST("mall/member/mobile/checkOldMobile.do")
    Observable<HttpResult<RxTicket>> checkOldMobile(@Field("userId") String userId,
                                                    @Field("code") String code,
                                                    @Field("token") String token);

    @FormUrlEncoded
    @POST("mall/member/mobile/checkOldMobile.do")
    Observable<HttpResult<String>> changeNewMobile(@Field("userId") String userId,
                                                   @Field("newCode") String newCode,
                                                   @Field("token") String token,
                                                   @Field("ticket") String ticket,
                                                   @Field("newMobile") String newMobile);

    @FormUrlEncoded
    @POST("mall/trade/orderState/getOrderStatusNum.do")
    Observable<HttpResult<RxOrderState>> getOrderStatusNum(@Field("userId") String userId,
                                                           @Field("token") String token);

    @FormUrlEncoded
    @POST("mall/product/collect/getCollectProducts.do")
    Observable<HttpResult<RxList<RxProduct>>> getCollectProducts(@Field("userId") String userId,
                                                                 @Field("token") String token,
                                                                 @Field("pageNo") int pageNo,
                                                                 @Field("pageSize") int pageSize);

    @FormUrlEncoded
    @POST("mall/dealer/getDealer.do")
    Observable<HttpResult<RxDealer>> getDealer(@Field("userId") String userId,
                                               @Field("token") String token);

    //申请成为经销商
    @FormUrlEncoded
    @POST("mall/dealer/becomeDealer.do")
    Observable<HttpResult<RxDealer>> becomeDealer(@Field("userId") String userId,
                                                  @Field("dealerId") String dealerId,
                                                  @Field("token") String token,
                                                  @Field("name") String name,
                                                  @Field("linkMan") String linkMan,
                                                  @Field("linkPhone") String linkPhone,
                                                  @Field("idcardFront") String idcardFront,
                                                  @Field("idcardBack") String idcardBack,
                                                  @Field("provinceName") String provinceName,
                                                  @Field("provinceCode") String provinceCode,
                                                  @Field("cityName") String cityName,
                                                  @Field("cityCode") String cityCode,
                                                  @Field("countyName") String countyName,
                                                  @Field("countyCode") String countyCode,
                                                  @Field("streetName") String streetName,
                                                  @Field("streetCode") String streetCode,
                                                  @Field("detailAddress") String detailAddress);

    @GET("mall/dealer/becomeDealer.do")
    Observable<HttpResult<RxDealer>> becomeDealer(@Query("userId") String userId,
                                                  @Query("token") String token,
                                                  @Query("name") String name,
                                                  @Query("linkMan") String linkMan,
                                                  @Query("linkPhone") String linkPhone,
                                                  @Query("idcardFront") String idcardFront,
                                                  @Query("idcardBack") String idcardBack,
                                                  @Query("provinceName") String provinceName,
                                                  @Query("provinceCode") String provinceCode,
                                                  @Query("cityName") String cityName,
                                                  @Query("cityCode") String cityCode,
                                                  @Query("countyName") String countyName,
                                                  @Query("countyCode") String countyCode,
                                                  @Query("streetName") String streetName,
                                                  @Query("streetCode") String streetCode,
                                                  @Query("detailAddress") String detailAddress);

    //修改经销商信息
    @FormUrlEncoded
    @POST("mall/dealer/modifyDealer.do")
    Observable<HttpResult<String>> modifyDealer(@Field("userId") String userId,
                                                @Field("dealerId") String dealerId,
                                                @Field("token") String token,
                                                @Field("name") String name,
                                                @Field("linkMan") String linkMan,
                                                @Field("linkPhone") String linkPhone,
                                                @Field("idcardFront") String idcardFront,
                                                @Field("idcardBack") String idcardBack,
                                                @Field("provinceName") String provinceName,
                                                @Field("provinceCode") String provinceCode,
                                                @Field("cityName") String cityName,
                                                @Field("cityCode") String cityCode,
                                                @Field("countyName") String countyName,
                                                @Field("countyCode") String countyCode,
                                                @Field("streetName") String streetName,
                                                @Field("streetCode") String streetCode,
                                                @Field("detailAddress") String detailAddress);

    @FormUrlEncoded
    @POST("mall/refund/refundApplication/addLogistics.do")
    Observable<HttpResult<String>> addLogistics(@Field("userId") String userId,
                                                @Field("token") String token,
                                                @Field("refundApplicationId") String refundApplicationId,
                                                @Field("companyName") String companyName,
                                                @Field("expressCode") String expressCode,
                                                @Field("freight") String freight,
                                                @Field("deliverGoodsTime") String deliverGoodsTime);

    @POST("mall/category/getAll.do ")
    Observable<HttpResult<List<RxClassfy>>> getClassfy();


    //获取商家商品
    @GET("mall/product/getShopProducts.do")
    Observable<HttpResult<RxList<RxProduct>>> shopProduct(@Query("userId") String userId,
                                                          @Query("token") String token,
                                                          @Query("keyword") String keyword,
                                                          @Query("shopId") String shopId,
                                                          @Query("pageSize") int pageSize,
                                                          @Query("pageNo") int pageNo);

    //获取经销商
    @GET("mall/dealer/getUserNotBind.do")
    Observable<HttpResult<RxDealerList>> getDealerList(@Query("userId") String userId,
                                                       @Query("token") String token,
                                                       @Query("areaCode") String areaCode,
                                                       @Query("areaName") String areaName,
                                                       @Query("pageSize") int pageSize,
                                                       @Query("pageNo") int pageNo);

    //获取我的经销商
    @GET("mall/dealer/user/getPassed.do")
    Observable<HttpResult<List<RxDealerCheck>>> getDealerMine(@Query("userId") String userId,
                                                              @Query("token") String token);

    //获取经销商
    @GET("mall/dealer/user/getNotPass.do")
    Observable<HttpResult<List<RxDealerCheck>>> getDealerNotPass(@Query("userId") String userId,
                                                                 @Query("token") String token);

    //关联代理商
    @GET("mall/dealer/user/apply.do")
    Observable<HttpResult<String>> applyDealer(@Query("userId") String userId,
                                               @Query("token") String token,
                                               @Query("dealerIds") String dealerIds
    );

    //取关代理商
    @GET("mall/dealer/user/cancel.do")
    Observable<HttpResult<String>> cancelDealer(@Query("userId") String userId,
                                                @Query("token") String token,
                                                @Query("dealerIds") String dealerIds
    );

    //商品上架
    @POST("mall/product/upshelf.do")
    @FormUrlEncoded
    Observable<HttpResult<String>> upShelf(@Field("userId") String userId,
                                           @Field("token") String token,
                                           @Field("productId") String productId);

    //商品上架
    @POST("mall/product/unshelf.do")
    @FormUrlEncoded
    Observable<HttpResult<String>> downShelf(@Field("userId") String userId,
                                             @Field("token") String token,
                                             @Field("productId") String productId);

    //删除商品
    @POST("mall/product/updateInvalid.do")
    @FormUrlEncoded
    Observable<HttpResult<String>> delProduct(@Field("userId") String userId,
                                              @Field("token") String token,
                                              @Field("productIds") String productIds);

}
