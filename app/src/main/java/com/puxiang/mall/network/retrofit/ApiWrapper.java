package com.puxiang.mall.network.retrofit;

import android.support.annotation.NonNull;

import com.puxiang.mall.MyApplication;
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
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.luban.Luban;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by ChenHengQuan on 2016/11/23.
 * Email nullpointerchan@163.com
 */
public class ApiWrapper extends RetrofitUtil {

    private volatile static ApiWrapper apiWrapper;

    private ApiWrapper() {
    }

    public static ApiWrapper getInstance() {
        if (apiWrapper == null) {
            synchronized (ApiWrapper.class) {
                if (apiWrapper == null) {
                    apiWrapper = new ApiWrapper();
                }
            }
        }
        return apiWrapper;
    }

    public synchronized Observable<RxUploadUrl> setUpload(final String path) {
        final File file = new File(path);
        return Luban.get(MyApplication.getContext())
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .asObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(Throwable::printStackTrace)
                .onErrorResumeNext(throwable -> {
                    return Observable.empty();
                })
                .flatMap(bytes -> {
                    RequestBody requestFile = RequestBody.create(MediaType.parse
                            ("multipart/form-data"), bytes);
                    String fileName = file.getName().replaceFirst("gif$", "png");
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("file", fileName, requestFile);
                    return ApiWrapper.getInstance().upload(body);
                })
                .map(uploadUrls -> {
                    RxUploadUrl uploadUrl = null;
                    if (uploadUrls.size() > 0) {
                        uploadUrl = uploadUrls.get(0);
                        uploadUrl.setFilePath(path);
                    }
                    return uploadUrl;
                });
    }


    private final int pageSize = 10;

//    public Observable<String> getLiju() {
//        return getService().getLiju("169647d8e141b915188211131edb9", "500");
//    }

    public Observable<LocationInfo> getLocationInfo(String latlng) {
        return getService().getLocationInfo(latlng, "zh-CN", true);
    }

    /**
     * 上传设备信息
     *
     * @param type      设备系统
     * @param version   app版本号
     * @param machineId 设备唯一ID
     */
    public Observable<String> uploadDeviceInfo(String type,
                                               String version,
                                               String machineId) {
        return getService()
                .uploadDeviceInfo(type, version, machineId)
                .compose(this.applySchedulers());
    }

    /**
     * 验证手机号码是否已经存在
     *
     * @param mobile 手机号码
     */
    public Observable<String> checkMobile(String mobile) {
        return getService()
                .checkMobile(mobile)
                .compose(this.applySchedulers());
    }

    /**
     * 获取朋友圈帖子
     *
     * @param pageNo 页码
     */
    public Observable<RxList<RxPostInfo>> friendPosts(int pageNo) {
        return getService()
                .friendPosts(MyApplication.USER_ID, MyApplication.TOKEN, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 获取多个圈子
     *
     * @param pageNo 页码
     */
    public Observable<RxList<RxPlate>> getPlates(int pageNo) {
        return getService()
                .getPlates(pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 获取视频列表
     *
     * @param typeId 类型
     * @param pageNo 页码
     */
    public Observable<RxList<RxPostInfo>> officialVideos(int typeId, int pageNo) {
        return getService()
                .officialVideos(typeId, 100, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 获取视频列表
     */
    public Observable<List<RxCatalog>> getCatalogs() {
        return getService()
                .getCatalogs(1)
                .compose(this.applySchedulers());
    }


    /**
     * 获取目录商品
     *
     * @param catalogId 商品目录Id
     */
    public Observable<RxList<RxProduct>> searchProduct(String catalogId) {
        return getService()
                .searchProduct("", catalogId, "", "", 100, 1,MyApplication.USER_ID)
                .compose(this.applySchedulers());
    }

    /**
     * 获取搜索商品
     */
    public Observable<RxList<RxProduct>> searchProduct(String keyword, String orderBy, String
            order, int pageNo) {
        return getService()
                .searchProduct(keyword, "", orderBy, order, pageSize, pageNo,MyApplication.USER_ID)
                .compose(this.applySchedulers());
    }

    /**
     * 获取搜索商品
     */
    public Observable<RxList<RxProduct>> searchProduct(String keyword, String orderBy, String
            order, String catagroyId, int pageNo) {
        return getService()
                .searchProduct(keyword, catagroyId, orderBy, order, pageSize, pageNo,MyApplication.USER_ID)
                .compose(this.applySchedulers());
    }

    /**
     * 获取商家商品
     */
    public Observable<RxList<RxProduct>> getShopGoods(String keyword, String shopId, String orderBy, String
            order, int pageSize, int pageNo) {
        return getService()
                .searchShopProduct(keyword, shopId, orderBy, order, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 搜索帖子
     *
     * @param keyword 关键字
     */
    public Observable<RxList<RxPostInfo>> searchPost(String keyword, int pageNo) {
        return getService()
                .searchPost(keyword, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 搜索圈子
     *
     * @param keyword 关键字
     */
    public Observable<RxList<RxPlate>> searchPlate(String keyword, int pageNo) {
        return getService()
                .searchPlate(keyword, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 搜索用户
     *
     * @param keyword 关键字
     */
    public Observable<RxList<RxUserInfo>> searchMember(String keyword, int pageNo) {
        return getService()
                .searchMember(keyword, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 获取购物车商品列表
     */
    public Observable<List<RxCartList>> getCarts() {
        return getService()
                .getCarts(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 获取购物车商品列表
     */
    public Observable<String> modifyCart(String cartId, String proudctId, int productNum) {
        return getService()
                .modifyCart(cartId, proudctId, productNum, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 删除购物车商品
     *
     * @param cartId 商品ID
     */
    public Observable<String> deleteCart(String cartId) {
        return getService()
                .deleteCart(cartId, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 删除购物车商品
     *
     * @param cartId
     * @param shopId
     * @return
     */
    public Observable<String> deleteCart(String cartId, String shopId) {
        return getService()
                .deleteCart(cartId, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }


    /**
     * 修改登录密码
     *
     * @param mobile      手机号码
     * @param smsCode     短信验证码
     * @param newPassword 新密码
     * @return
     */
    public Observable<String> modifyLoginPassword(String mobile, String smsCode, String newPassword) {
        return getService()
                .modifyLoginPassword(mobile, smsCode, newPassword)
                .compose(this.applySchedulers());
    }

    /**
     * 修改生日
     *
     * @param birthday 日期 yyyy-MM-dd
     */
    public Observable<String> modifyBirthday(String birthday) {
        return getService()
                .modifyBirthday(birthday, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 修改真实姓名
     *
     * @param realName 真实姓名
     */
    public Observable<String> modifyRealName(String realName) {
        return getService().modifyRealName(realName, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 修改邮箱地址
     */
    public Observable<String> modifyEmail(String email) {
        return getService().modifyEmail(email, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }


    /**
     * 修改昵称
     *
     * @param nickName 昵称
     */
    public Observable<String> modifyNickname(String nickName) {
        return getService().modifyNickname(nickName, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 修改性别
     *
     * @param sex 性别
     */
    public Observable<String> modifySex(String sex) {
        return getService().modifySex(sex, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 修改用户头像
     *
     * @param url 新头像Url
     */
    public Observable<String> modifyUserImage(String url) {
        return getService().modifyUserImage(url, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 上传文件
     */
    public Observable<List<RxUploadUrl>> upload(MultipartBody.Part file) {
        return getService().upload(file)
                .compose(this.apply());
    }


    /**
     * 获取消息状态
     */
    public Observable<RxMessageState> getMessageState() {
        return getService().getMessageState(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 获取未读消息
     */
    public Observable<List<RxUnreadMessage>> getUnreadMessage() {
        return getService().getUnreadMessage(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 分享成功回调
     *
     * @param shareLink 分享链接
     */
    public Observable<String> shareOK(String shareLink) {
        return getService().shareOK(shareLink, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 获取支付信息
     *
     * @param orderType 订单类型
     * @param orderId   订单ID
     */
    public Observable<RxPayPrice> getPayInfo(String orderType, String orderId) {
        return getService().getPayInfo(orderType, orderId, MyApplication.USER_ID, MyApplication
                .TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 获取商品详情
     *
     * @param productId 商品ID
     */
    public Observable<RxProduct> getProduct(String productId) {
        return getService().getProduct(productId)
                .compose(this.applySchedulers());
    }

    /**
     * 获取欢迎页面图片
     *
     * @param url 图片地址
     */
    public Observable<ResponseBody> downloadPicFromNet(String url) {
        return getService().downloadPicFromNet(url);
    }

    /**
     * 获取积分商品详情
     *
     * @param productId 商品Id
     */
    public Observable<RxIntegralProduct> getIntegralProduct(String productId) {
        return getService().getIntegralProduct(productId)
                .compose(this.applySchedulers());
    }

    /**
     * 获取硕虎人格信息
     */
    public Observable<RxShuohuInfo> getShuohuInfo() {
        return getService().getShuohuInfo()
                .compose(this.applySchedulers());
    }

    /**
     * 获取分享连接
     *
     * @param rawUrl 原始分享链接
     */
    public Observable<String> getShareUrl(String rawUrl) {
        return getService().getShareUrl(rawUrl)
                .compose(this.applySchedulers());
    }

    /**
     * 获取支付宝支付签名
     */
    public Observable<List<RxPayChannel>> getPayChannel() {
        return getService()
                .getPayChannel()
                .compose(this.applySchedulers());
    }

    /**
     * 获取支付宝支付签名
     *
     * @param orderType 订单类型
     * @param orderId   订单ID
     */
    public Observable<String> alipaySign(String orderType, String orderId) {
        return getService().alipaySign(orderType, orderId, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.apply());
    }

    /**
     * 获取银联支付签名
     *
     * @param orderType 订单类型
     * @param orderId   订单ID
     */
    public Observable<String> unionpaySign(int orderType, String orderId) {
        return getService().unionpaySign(orderType, orderId, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 获取微信支付签名
     *
     * @param orderType 订单类型
     * @param appType   app类型
     * @param orderId   订单ID
     */
    public Observable<RxWXPayInfo> weixinPaySign(String orderType, int appType, String orderId) {
        return getService()
                .weixinPaySign(orderType, appType, orderId, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }


    /**
     * 获取来源帖子
     *
     * @param typeId 帖子分类
     * @param pageNo 页码
     */
    public Observable<RxList<RxPostInfo>> officialPosts(String typeId, int pageNo) {
        return getService()
                .officialPosts(typeId, pageSize, pageNo)
                .compose(this.applySchedulers());
    }


    /**
     * 获取最新帖子列表(首页)
     *
     * @param typeId 帖子分类
     * @param pageNo 页码
     */
    public Observable<RxList<RxPostInfo>> newestPosts(String typeId, int pageNo) {
        return getService()
                .newestPosts(typeId, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 获取广告信息数据
     *
     * @param channelCode 频道编码
     */
    public Observable<List<RxAds>> getAds(String channelCode) {
        return getService().getAds(channelCode)
                .compose(this.applySchedulers());
    }

    /**
     * 获取融云Token
     */
    public Observable<String> getRYToken() {
        return getService()
                .getRYToken(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 刷新融云Token
     */
    public Observable<String> refreshToken() {
        return getService()
                .refreshToken(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 查询用户信息
     *
     * @param userId 查询的用户Id
     */
    public Observable<RxUserInfo> getCommunityUserInfo(String userId) {
        return getService()
                .getCommunityUserInfo(userId)
                .compose(this.applySchedulers());
    }

    /**
     * 登录
     *
     * @param userName 账号
     * @param password 密码
     */
    public Observable<HttpResult<RxMyUserInfo>> login(String userName, String password) {
        return getService()
                .login(userName, password);
    }

    /**
     * 获取我的信息
     */
    public Observable<RxMyUserInfo> getMyUserInfo() {
        return getService()
                .getMyUserInfo(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 获取积分任务
     */
    public Observable<List<RxIntegral>> getIntegralTask() {
        return getService()
                .getIntegralTask(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 获取账户积分信息
     */
    public Observable<RxIntegralAccount> getIntegralAccount() {
        return getService()
                .getIntegralAccount(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 获取手机验证码
     *
     * @param mobile 手机号码
     */
    public Observable<String> getSmsCode(String mobile) {
        return getService()
                .getSmsCode(mobile)
                .compose(this.applySchedulers());
    }

    /**
     * 注册
     *
     * @param mobile     手机号码
     * @param code       验证码
     * @param password   密码
     * @param macAddress 设备唯一Id
     */
    public Observable<String> register(String mobile, String code, String password, String
            macAddress, String invateCode) {
        return getService()
                .register(mobile, code, password, macAddress, invateCode)
                .compose(this.applySchedulers());
    }

    /**
     * 微信第三方登录
     *
     * @param code 授权码
     */
    public Observable<RxAuthorizeUserInfo> wechatAuthorize(String code) {
        return getService()
                .wechatAuthorize(code, 0)
                .compose(this.applySchedulers());
    }

    /**
     * qq第三方登录
     *
     * @param map 用户信息
     */
    public Observable<RxAuthorizeUserInfo> qqAuthorize(Map<String, String> map) {
        map.put("machineType", "0");
        return getService()
                .qqAuthorize(map)
                .compose(this.applySchedulers());
    }

    /**
     * 新浪微博第三方登录
     */
    public Observable<RxAuthorizeUserInfo> weiboAuthorize(Map<String, String> map) {
        map.put("machineType", "0");
        return getService()
                .weiboAuthorize(map)
                .compose(this.applySchedulers());
    }

    /**
     * 绑定手机号
     *
     * @param mobile 手机号码
     * @param code   短信验证码
     */
    public Observable<RxAuthorizeUserInfo> bindingMobile(String code, String mobile) {
        return getService()
                .bindingMobile(code, mobile)
                .compose(this.applySchedulers());
    }

    /**
     * 获取商城推荐商品
     */
    public Observable<RxMallInfo> getRecommendProducts(String recommendType) {
        return getService()
                .getRecommendProducts(recommendType)
                .compose(this.applySchedulers());
    }

    /**
     * 获取商城推荐商品
     */
    public Observable<List<RxProduct>> getRecommendProducts() {
        return getService()
                .getRecommendProducts()
                .compose(this.applySchedulers());
    }

    /**
     * 热门搜索关键字
     */
    public Observable<List<String>> getHotKeywords() {
        return getService()
                .getHotKeywords()
                .compose(this.applySchedulers());
    }

    /**
     * 获取所有的圈子以及分类信息
     */
    public Observable<List<RxPlateType>> allPlates() {
        return getService()
                .allPlates()
                .compose(this.applySchedulers());
    }

    /**
     * 获取社区频道列表
     */
    public Observable<List<RxChannel>> getChannels() {
        return getService()
                .getChannels()
                .compose(this.applySchedulers());
    }

    /**
     * 获取购物车数量
     */
    public Observable<Integer> getCartNum() {
        return getService()
                .getCartNum(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 签到
     */
    public Observable<String> userSigned() {
        return getService()
                .userSigned(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 查询用户昵称是否可以修改
     */
    public Observable<RxCheck> checkNickname() {
        return getService()
                .checkNickname(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 帖子详情
     *
     * @param postId 帖子ID
     */
    public Observable<RxPostInfo> postDetail(String postId) {
        return getService()
                .postDetail(postId, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 帖子点赞
     *
     * @param isLike 是否点赞
     * @param postId 帖子ID
     */
    public Observable<String> postLike(@NonNull String postId, boolean isLike) {
        Observable<HttpResult<String>> observable;
        if (isLike) {
            observable = getService()
                    .postLike(postId, MyApplication.USER_ID, MyApplication.TOKEN);
        } else {
            observable = getService()
                    .cancelPostLike(postId, MyApplication.USER_ID, MyApplication.TOKEN);
        }
        return observable.compose(this.applySchedulers());
    }

    /**
     * 评论点赞
     *
     * @param commentId 评论ID
     * @param isLike    是否点赞
     */
    public Observable<String> commentLike(@NonNull String commentId, boolean isLike) {
        Observable<HttpResult<String>> observable;
        if (isLike) {
            observable = getService()
                    .commentLike(commentId, MyApplication.USER_ID, MyApplication.TOKEN);
        } else {
            observable = getService()
                    .cancelCommentLike(commentId, MyApplication.USER_ID, MyApplication.TOKEN);
        }
        return observable.compose(this.applySchedulers());
    }

    /**
     * 帖子点赞列表
     *
     * @param postId   帖子ID
     * @param pageSize 一页数据条数
     * @param pageNo   页码
     */
    public Observable<RxList<RxPostLike>> getPraisePost(@NonNull String postId,
                                                        int pageSize,
                                                        int pageNo) {
        return getService()
                .getPraisePost(postId, pageSize, pageNo)
                .compose(this.applySchedulers());

    }

    /**
     * 帖子点赞列表
     *
     * @param postId  帖子ID
     * @param orderBy 排序
     * @param pageNo  页码
     */
    public Observable<RxList<RxCommentInfo>> getPostComments(@NonNull String postId,
                                                             int orderBy,
                                                             int pageNo) {
        return getService()
                .getPostComments(postId, MyApplication.USER_ID, MyApplication.TOKEN,
                        orderBy, pageSize, pageNo)
                .compose(this.applySchedulers());

    }


    /**
     * 圈子详情
     *
     * @param plateId 圈子ID
     */
    public Observable<RxPlate> getPlateDetail(String plateId) {
        return getService()
                .getPlateDetail(plateId, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 获取圈子帖子列表
     *
     * @param plateId 圈子ID
     * @param pageNo  页码
     */
    public Observable<RxList<RxPostInfo>> platePosts(String plateId, int pageNo) {
        return getService()
                .platePosts(MyApplication.USER_ID, MyApplication.TOKEN, plateId, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 获取单个评论回复
     *
     * @param commentId 评论ID
     */
    public Observable<RxPostComment> getPostComment(String commentId) {
        return getService()
                .getPostComment(MyApplication.USER_ID, MyApplication.TOKEN, commentId)
                .compose(this.applySchedulers());
    }

    /**
     * 获取评论回复列表
     *
     * @param commentId 评论ID
     * @param pageNo    页码
     */
    public Observable<RxList<RxReply>> getCommentReplys(String commentId, int pageNo) {
        return getService()
                .getCommentReplys(commentId, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 查阅信息
     *
     * @param messageCode 信息ID
     */
    public Observable<String> setMessageReadTime(String messageCode) {
        return getService()
                .setMessageReadTime(messageCode, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 评论
     *
     * @param postId  帖子id
     * @param comment 评论内容
     */
    public Observable<RxComment> comment(String postId, String comment) {
        return getService()
                .comment(postId, comment, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 回复评论
     *
     * @param commentId     评论ID
     * @param byReplyUserId 评论的用户ID
     * @param reply         评论内容
     */
    public Observable<RxCommentReply> reply(String commentId, String byReplyUserId, String reply) {
        return getService()
                .reply(commentId, byReplyUserId, reply, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 发帖
     *
     * @param picUrls       发帖图片
     * @param orderDetailId 订单Id
     * @param plateId       圈子ID
     * @param content       帖子内容
     */
    public Observable<String> publish(String picUrls,
                                      String orderDetailId,
                                      String plateId,
                                      String content) {
        picUrls = StringUtil.isEmpty(picUrls) ? "" : picUrls;
        orderDetailId = StringUtil.isEmpty(orderDetailId) ? "" : orderDetailId;
        return getService()
                .publish(MyApplication.USER_ID, MyApplication.TOKEN, picUrls,
                        orderDetailId, plateId, content)
                .compose(this.applySchedulers());
    }

    /**
     * 用户关注的人
     *
     * @param otherUserId 用户ID
     * @param pageNo      页码
     */
    public Observable<RxList<RxAttentionUserData>> getAttentionUsers(String otherUserId,
                                                                     int pageNo) {
        return getService()
                .getAttentionUsers(MyApplication.USER_ID, MyApplication.TOKEN, otherUserId, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 用户粉丝
     *
     * @param otherUserId 用户ID
     * @param pageNo      页码
     */
    public Observable<RxList<RxFans>> userFans(String otherUserId, int pageNo) {
        return getService()
                .userFans(MyApplication.USER_ID, MyApplication.TOKEN, otherUserId, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 用户加入的圈子
     *
     * @param otherUserId 用户ID
     * @param pageNo      页码
     */
    public Observable<RxList<RxPlate>> userPlates(String otherUserId, int pageNo) {
        return getService()
                .userPlates(MyApplication.USER_ID, MyApplication.TOKEN, otherUserId, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 用户收藏帖子
     *
     * @param otherUserId 用户ID
     * @param pageNo      页码
     */
    public Observable<RxList<RxPostInfo>> collectPosts(String otherUserId, int pageNo) {
        return getService()
                .collectPosts(MyApplication.USER_ID, MyApplication.TOKEN, otherUserId, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 用户发布的帖子
     *
     * @param otherUserId 用户ID
     * @param pageNo      页码
     */
    public Observable<RxList<RxPostInfo>> myPosts(String otherUserId, int pageNo) {
        return getService()
                .myPosts(MyApplication.USER_ID, MyApplication.TOKEN, otherUserId, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 回复的帖子评论
     *
     * @param otherUserId 用户ID
     * @param pageNo      页码
     */
    public Observable<RxList<RxReplyPostComment>> getReplyPostComments(String otherUserId,
                                                                       int pageNo) {
        return getService()
                .getReplyPostComments(MyApplication.USER_ID, MyApplication.TOKEN, otherUserId, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 用户社区
     *
     * @param friendId 用户ID
     */
    public Observable<RxUserCommunity> getUserCommunity(String friendId) {
        return getService()
                .getUserCommunity(MyApplication.USER_ID, MyApplication.TOKEN, friendId)
                .compose(this.applySchedulers());
    }

    /**
     * 第三方登录
     */
    public Observable<HttpResult<RxMyUserInfo>> authorizeAndroid(String code) {
        return getService()
                .authorizeAndroid(code);
    }


    /**
     * 关注用户
     *
     * @param isLike            是否关注
     * @param myAttentionUserId 关注的用户ID
     */
    public Observable<String> attentUser(@NonNull String myAttentionUserId, boolean isLike) {
        Observable<HttpResult<String>> observable;
        if (isLike) {
            observable = getService()
                    .attent(MyApplication.USER_ID, MyApplication.TOKEN, myAttentionUserId);
        } else {
            observable = getService()
                    .cancelAttent(MyApplication.USER_ID, MyApplication.TOKEN, myAttentionUserId);
        }
        return observable.compose(this.applySchedulers());
    }

//    /**
//     * 关注用户
//     *
//     * @param myAttentionUserId 要关注的用户ID
//     */
//    public Observable<String> attent(String myAttentionUserId) {
//        return getService().attent(MyApplication.USER_ID, MyApplication.TOKEN, myAttentionUserId)
//                .compose(this.<String>applySchedulers());
//    }
//
//    /**
//     * 取消关注用户
//     *
//     * @param myAttentionUserId 要取消关注的用户ID
//     */
//    public Observable<String> cancelAttent(String myAttentionUserId) {
//        return getService().cancelAttent(MyApplication.USER_ID, MyApplication.TOKEN,
// myAttentionUserId)
//                .compose(this.<String>applySchedulers());
//    }

    /**
     * 加入圈子
     *
     * @param plateId 圈子的ID
     * @param isJoin  是否加入
     */
    public Observable<String> joinPlate(@NonNull String plateId, boolean isJoin) {
        Observable<HttpResult<String>> observable;
        if (isJoin) {
            observable = getService()
                    .joinPlate(MyApplication.USER_ID, MyApplication.TOKEN, plateId);
        } else {
            observable = getService()
                    .exitPlate(MyApplication.USER_ID, MyApplication.TOKEN, plateId);
        }
        return observable.compose(this.applySchedulers());
    }

//    public Observable<String> joinPlate(String plateId) {
//        return getService().joinPlate(MyApplication.USER_ID, MyApplication.TOKEN, plateId)
//                .compose(this.<String>applySchedulers());
//    }

//    /**
//     * 退出圈子
//     *
//     * @param plateId 圈子的ID
//     */
//    public Observable<String> exitPlate(String plateId) {
//        return getService().exitPlate(MyApplication.USER_ID, MyApplication.TOKEN, plateId)
//                .compose(this.<String>applySchedulers());
//    }


    /**
     * 收藏帖子
     *
     * @param postId    帖子的ID
     * @param isCollect 是否收藏
     */
    public Observable<String> collect(@NonNull String postId, boolean isCollect) {
        Observable<HttpResult<String>> observable;
        if (isCollect) {
            observable = getService()
                    .collect(MyApplication.USER_ID, MyApplication.TOKEN, postId);
        } else {
            observable = getService()
                    .cancelCollect(MyApplication.USER_ID, MyApplication.TOKEN, postId);
        }
        return observable.compose(this.applySchedulers());
    }

//    /**
//     * 取消收藏帖子
//     *
//     * @param postId 帖子ID
//     */
//    public Observable<String> cancelCollect(String postId) {
//        return getService().cancelCollect(MyApplication.USER_ID, MyApplication.TOKEN, postId)
//                .compose(this.<String>applySchedulers());
//    }

    /**
     * 是否收藏该帖子
     *
     * @param postId 帖子ID
     */
    public Observable<Integer> isCollected(String postId) {
        return getService().isCollected(MyApplication.USER_ID, MyApplication.TOKEN, postId)
                .compose(this.applySchedulers());
    }

    /**
     * 删除帖子
     *
     * @param postId 帖子ID
     */
    public Observable<String> deletePost(String postId) {
        return getService().deletePost(MyApplication.USER_ID, MyApplication.TOKEN, postId)
                .compose(this.applySchedulers());
    }

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     */
    public Observable<String> deleteComment(String commentId) {
        return getService().deleteComment(MyApplication.USER_ID, MyApplication.TOKEN, commentId)
                .compose(this.applySchedulers());
    }

    /**
     * 退款申请
     *
     * @param orderDetailId 评论ID
     */
    public Observable<RxRefund> refund(String orderDetailId) {
        return getService().refund(MyApplication.USER_ID, MyApplication.TOKEN, orderDetailId)
                .compose(this.applySchedulers());
    }

    /**
     * 提交退款申请
     */
    public Observable<String> submitRefund(int refundReasonId, String productId,
                                           String orderId, String orderDetailId,
                                           String refundAmount, String refundRemark,
                                           String refundType, String refundVoucherPic) {
        return getService().submitRefund(MyApplication.USER_ID, MyApplication.TOKEN,
                refundReasonId, productId, orderId, orderDetailId, refundAmount, refundRemark, refundType,
                refundVoucherPic)
                .compose(this.applySchedulers());
    }

    /**
     * 电竞馆签到
     */
    public Observable<RxSing> checkCard(String esportShopId, double latitude, double longitude, String qrCode) {
        return getService()
                .checkCard(MyApplication.USER_ID, MyApplication.TOKEN, esportShopId, latitude, longitude, qrCode)
                .compose(this.applySchedulers());
    }

    /**
     * 获取电竞馆列表
     */
    public Observable<RxEsportList> getEsportList(double lat, double lng, int pageNo) {
        return getService()
                .getEsportList(lat, lng, pageNo, pageSize)
                .compose(this.applySchedulers());
    }

    /**
     * 获取电竞馆列表
     */
    public Observable<RxEsportList> getAttentEsportList() {
        return getService()
                .getAttentEsportList(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 检测打卡区域
     */
    public Observable<RxSing> checkUserInShop(String esportShopId, double latitude, double longitude) {
        return getService()
                .checkUserInShop(MyApplication.USER_ID, MyApplication.TOKEN, esportShopId, latitude, longitude)
                .compose(this.applySchedulers());
    }

    /**
     * 获取商家列表
     *
     * @param keyword  搜索用关键词
     * @param ereaCode 区域代码
     * @param pageNo   页码
     * @return
     */
    public Observable<RxShopList> getShopList(String keyword, String ereaCode, int pageNo, String areaName, String orderBy) {
        return getService()
                .getShopList(pageNo + "", pageSize + "", keyword, ereaCode, areaName, orderBy)
                .compose(this.applySchedulers());
    }

    /**
     * 获取定位信息
     *
     * @param lat
     * @param lng
     * @return
     */
    public Observable<RxLocation> getLocation(double lat, double lng) {
        return getService().getLocation(lat, lng)
                .compose(this.applySchedulers());
    }

    public Observable<RxShop> getShopDetial(String shopId) {
        return getService().getShopDetial(shopId)
                .compose(this.applySchedulers());
    }

    /**
     * 获取该定位城市下面的数据
     *
     * @param areaCode
     * @return
     */
    public Observable<List<RxCityArea>> getAllChildArea(String areaCode) {
        return getService().getAllChildArea(areaCode)
                .compose(this.applySchedulers());
    }

    /**
     * 获取进货商品列表
     *
     * @return
     */
    public Observable<RxBatchList> getBatchProducts(String keyword, int pageNo) {
        return getService()
                .getBatchProducts(pageNo + "", pageSize + "", keyword, "", "", MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 获取进货商品总价
     *
     * @param produceId
     * @param buyCount
     * @return
     */
    public Observable<RxBatchTotalPrice> getBatchTotalPrice(String produceId, int buyCount) {
        return getService().
                countBatchSingleTotalPrice(produceId, buyCount, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 获取融云token
     *
     * @return
     */
    public Observable<String> getRongToken() {
        return getService().getRongToken(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 获取融云token
     *
     * @return
     */
    public Observable<String> getRongToken(String userId, String token) {
        return getService().getRongToken(userId, token)
                .compose(this.applySchedulers());
    }

    public Observable<List<RxPostAddress>> getAddresses() {
        return getService().getAddresses(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 删除收货地址
     *
     * @param addressId
     * @return
     */
    public Observable<String> deleteAddress(String addressId) {
        return getService().deleteAddress(addressId, MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 修改收货地址
     *
     * @param addressId
     * @return
     */
    public Observable<String> modifyAddress(String addressId, String shipName, String shipAddress, String phone, String province, String provinceCode, String city,
                                            String cityCode, String area, String areaCode, String tel, String isDefault) {
        return getService().modifyAddress(MyApplication.USER_ID,
                MyApplication.TOKEN,
                addressId,
                shipName,
                shipAddress,
                phone,
                province,
                provinceCode,
                city,
                cityCode,
                area,
                areaCode,
                tel,
                isDefault)
                .compose(this.applySchedulers());
    }

    /**
     * 添加收货地址
     *
     * @return
     */
    public Observable<String> addAddress(String shipName, String shipAddress, String phone, String province, String provinceCode, String city,
                                         String cityCode, String area, String areaCode, String tel, String isDefault) {
        return getService().addAddress(MyApplication.USER_ID,
                MyApplication.TOKEN,
                shipName,
                shipAddress,
                phone,
                province,
                provinceCode,
                city,
                cityCode,
                area,
                areaCode,
                tel,
                isDefault)
                .compose(this.applySchedulers());
    }

    public Observable<RxTicket> checkOldMobile(String code) {
        return getService().checkOldMobile(MyApplication.USER_ID, code, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    public Observable<String> changeNewMobile(String code, String ticket, String mobile) {
        return getService().changeNewMobile(MyApplication.USER_ID, code, MyApplication.TOKEN, ticket, mobile)
                .compose(this.applySchedulers());
    }

    public Observable<RxOrderState> getOrderStatusNum() {
        return getService().getOrderStatusNum(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    public Observable<RxList<RxProduct>> getCollectProducts(int pageNo, int pageSize) {
        return getService().getCollectProducts(MyApplication.USER_ID, MyApplication.TOKEN, pageNo, pageSize)
                .compose(this.applySchedulers());
    }

    public Observable<RxDealer> getDealer() {
        return getService().getDealer(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 申请成为经销商
     *
     * @param dealerId
     * @param name
     * @param linkMan
     * @param linkPhone
     * @param idcardFront
     * @param idcardBack
     * @param provinceCode
     * @param provinceName
     * @param cityCode
     * @param cityName
     * @param countryCode
     * @param countryName
     * @param streetCode
     * @param streetName
     * @param detailAddress
     * @return
     */
    public Observable<RxDealer> becomeDealer(String dealerId, String name, String linkMan, String linkPhone,
                                             String idcardFront, String idcardBack, String provinceCode,
                                             String provinceName, String cityCode, String cityName,
                                             String countryCode, String countryName, String streetCode,
                                             String streetName, String detailAddress) {

        return getService().becomeDealer(MyApplication.USER_ID, dealerId, MyApplication.TOKEN,
                name, linkMan, linkPhone, idcardFront, idcardBack, provinceName, provinceCode,
                cityName, cityCode, countryName, countryCode, streetName, streetCode, detailAddress)
                .compose(this.applySchedulers());
    }

    public Observable<RxDealer> becomeDealer(String name, String linkMan, String linkPhone,
                                             String idcardFront, String idcardBack, String provinceCod,
                                             String provinceName, String cityCode, String cityNam,
                                             String countryCode, String countryName, String streetCode,
                                             String streetName, String detailAddress) {

        return getService().becomeDealer(MyApplication.USER_ID, MyApplication.TOKEN,
                name, linkMan, linkPhone, idcardFront, idcardBack, provinceName, provinceCod,
                cityNam, cityCode, countryName, countryCode, streetName, streetCode, detailAddress)
                .compose(this.applySchedulers());
    }

    /**
     * 修改经销商
     *
     * @param dealerId
     * @param name
     * @param linkMan
     * @param linkPhone
     * @param idcardFront
     * @param idcardBack
     * @param provinceCode
     * @param provinceName
     * @param cityCode
     * @param cityName
     * @param countryCode
     * @param countryName
     * @param streetCode
     * @param streetName
     * @param detailAddress
     * @return
     */
    public Observable<String> modifyDealer(String dealerId, String name, String linkMan, String linkPhone,
                                           String idcardFront, String idcardBack, String provinceCode,
                                           String provinceName, String cityCode, String cityName,
                                           String countryCode, String countryName, String streetCode,
                                           String streetName, String detailAddress) {

        return getService().modifyDealer(MyApplication.USER_ID, dealerId, MyApplication.TOKEN,
                name, linkMan, linkPhone, idcardFront, idcardBack, provinceName, provinceCode,
                cityName, cityCode, countryName, countryCode, streetName, streetCode, detailAddress)
                .compose(this.applySchedulers());
    }

    public Observable<String> addLogistics(String refundApplicationId, String companyName, String expressCode, String freight, String deliverGoodsTime) {
        return getService().addLogistics(MyApplication.USER_ID, MyApplication.TOKEN, refundApplicationId, companyName, expressCode, freight, deliverGoodsTime)
                .compose(this.applySchedulers());
    }

    /**
     * 获取全部分类信息
     *
     * @return
     */
    public Observable<List<RxClassfy>> getClassfy() {
        return getService().getClassfy()
                .compose(this.applySchedulers());
    }

    /**
     * 获取商家商品
     */
    public Observable<RxList<RxProduct>> searchProduct(String keyword, String shopId, int pageNo) {
        return getService()
                .shopProduct(MyApplication.USER_ID, MyApplication.TOKEN, keyword, shopId, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 上架商品
     *
     * @param productId
     * @return
     */
    public Observable<String> upShelf(String productId) {
        return getService().upShelf(MyApplication.USER_ID, MyApplication.TOKEN, productId)
                .compose(this.applySchedulers());
    }

    /**
     * 上架商品
     *
     * @param productId
     * @return
     */
    public Observable<String> downShelf(String productId) {
        return getService().downShelf(MyApplication.USER_ID, MyApplication.TOKEN, productId)
                .compose(this.applySchedulers());
    }

    /**
     * 删除商品
     *
     * @param productIds 商品Id合集用逗号分割开来
     * @return
     */
    public Observable<String> delProduct(String productIds) {
        return getService().downShelf(MyApplication.USER_ID, MyApplication.TOKEN, productIds)
                .compose(this.applySchedulers());
    }

    /**
     * 获取代理商
     */
    public Observable<RxDealerList> getDealerList(String areaName, String areaCode, int pageNo) {
        return getService()
                .getDealerList(MyApplication.USER_ID, MyApplication.TOKEN, areaCode, areaName, pageSize, pageNo)
                .compose(this.applySchedulers());
    }

    /**
     * 获取我的代理商
     */
    public Observable<List<RxDealerCheck>> getDealerMine(int pageNo) {
        return getService()
                .getDealerMine(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 获取我的代理商 未通过
     */
    public Observable<List<RxDealerCheck>> getDealerNot(int pageNo) {
        return getService()
                .getDealerNotPass(MyApplication.USER_ID, MyApplication.TOKEN)
                .compose(this.applySchedulers());
    }

    /**
     * 关联代理商
     * @param ids
     * @return
     */
    public Observable<String> applyDealer(String ids) {
        return getService().applyDealer(MyApplication.USER_ID, MyApplication.TOKEN, ids)
                .compose(this.applySchedulers());
    }

    /**
     * 取关代理商
     * @param ids
     * @return
     */
    public Observable<String> cancelDealer(String ids) {
        return getService().cancelDealer(MyApplication.USER_ID, MyApplication.TOKEN, ids)
                .compose(this.applySchedulers());
    }
}
