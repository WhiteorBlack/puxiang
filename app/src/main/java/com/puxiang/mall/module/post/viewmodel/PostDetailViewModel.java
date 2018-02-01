package com.puxiang.mall.module.post.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.actionsheet.ActionSheet;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.BR;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.PostDetailMultiItemEntity;
import com.puxiang.mall.model.data.RxComment;
import com.puxiang.mall.model.data.RxCommentInfo;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxPlate;
import com.puxiang.mall.model.data.RxPlateFormReply;
import com.puxiang.mall.model.data.RxPost;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.model.data.RxPostLike;
import com.puxiang.mall.model.data.RxUserInfo;
import com.puxiang.mall.module.emotion.viewmodel.adapter.ShareBottomDialog;
import com.puxiang.mall.module.post.adapter.PostDetailAdapter;
import com.puxiang.mall.module.post.adapter.PostLikeQuickAdapter;
import com.puxiang.mall.module.post.model.GifModel;
import com.puxiang.mall.module.post.model.ShareInfo;
import com.puxiang.mall.module.post.view.PostDetailActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.DraweeViewUtils;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class PostDetailViewModel extends BaseObservable implements ViewModel {

    private final PostDetailActivity activity;
    public final PostDetailAdapter adapter;
    private final LoadingWindow loadingWindow;
    private final PostLikeQuickAdapter likeAdapter;
    public ObservableBoolean isInitData = new ObservableBoolean();
    public ObservableBoolean isInit = new ObservableBoolean();
    public ObservableBoolean isCollected = new ObservableBoolean();
    private ActionSheet.Builder builder;
    public ObservableField<RxPostInfo> postInfoBean = new ObservableField<>();
    public String postId;
    private List<PostDetailMultiItemEntity> entityList;
    private List<RxPostLike> postLikeList = new ArrayList<>();
    private String rawUrl;
    private String shareUrl;
    private ShareInfo shareInfo;
    private String shareTitle;
    private String shareImage;
    private ShareBottomDialog shareDialog;
    private int pageNo = 0;
    public int detailDataSize;
    private String contentImageUrl;


    public PostDetailViewModel(PostDetailActivity activity) {
        EventBus.getDefault().register(this);
        this.activity = activity;
        loadingWindow = new LoadingWindow(activity);
        loadingWindow.delayedShowWindow();
        likeAdapter = new PostLikeQuickAdapter(R.layout.item_sdv);
        this.adapter = new PostDetailAdapter(new ArrayList<>(), likeAdapter);
        entityList = adapter.getData();
        getInitData();
        getData();
    }

    public void getData() {
        getPostDetailData();
        getPostLikeData();
        isCollected();
    }

    /**
     * 是否已关注
     */
    private void isCollected() {
        ApiWrapper.getInstance()
                .isCollected(postId)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<Integer>() {
                    @Override
                    public void onSuccess(Integer bean) {
                        isCollected.set(bean == 1);
                    }
                });
    }

    /**
     * 获取帖子详情
     */
    private void getPostDetailData() {
        ApiWrapper.getInstance()
                .postDetail(postId)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxPostInfo>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        isInitData.set(false);
                        loadingWindow.hidWindow();
                        ToastUtil.toast("网络错误");
                    }

                    @Override
                    public void onSuccess(RxPostInfo postInfo) {
                        isInitData.set(true);
                        isInit.set(true);
                        setPostDetailData(postInfo);
                    }
                });
    }

    /**
     * 获取帖子评论列表
     *
     * @param pageNo 页码
     * @param b      是否滚动到指定位置
     */
    private void getPostCommentsData(final int pageNo, final boolean b) {
        int order = postInfoBean.get().getOrder();
        Log.e(TAG, "getPostCommentsData: " + order);
        ApiWrapper.getInstance()
                .getPostComments(postId, order, pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxList<RxCommentInfo>>() {
                    @Override
                    public void onSuccess(RxList<RxCommentInfo> bean) {
                        setPostCommentsData(bean, pageNo, b);
                    }
                });
    }

    private void setPostDetailData(RxPostInfo bean) {
        initShareData(bean);
        setDetailData(bean);
        detailDataSize = entityList.size();
        loadingWindow.delayHideWindow();
    }

    /**
     * 获取分享链接
     */
    private void getShareUrl() {
        ApiWrapper.getInstance()
                .getShareUrl(rawUrl)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        ToastUtil.toast("分享失败");
                    }

                    @Override
                    public void onSuccess(String s) {
                        shareUrl = s;
                        share();
                    }
                });
    }

    /**
     * 分享
     */
    public void share() {
        if (StringUtil.isEmpty(shareUrl)) {
            getShareUrl();
        } else {
            if (shareInfo == null) {
                shareInfo = new ShareInfo(shareUrl, shareTitle, shareImage, rawUrl);
            }
            setShareDialog(shareInfo);
        }
    }

    private void setShareDialog(ShareInfo shareInfo) {
        if (shareInfo != null) {
            if (shareDialog == null) {
                shareDialog = new ShareBottomDialog(activity, shareInfo);
            } else {
                shareDialog.setShareInfo(shareInfo);
            }
            shareDialog.show();
        }
    }

    /**
     * 初始化分享数据
     *
     * @param bean 分享数据
     */
    private void initShareData(RxPostInfo bean) {
        String picUrls = bean.getPost().getPicUrl();
        if (!StringUtil.isEmpty(picUrls)) {
            String[] urls = picUrls.split(",");
            shareImage = urls.length > 0 ? urls[0] : "";
        }
        shareTitle = bean.getPost().getTitle();
    }

    /**
     * 收藏帖子
     */
    public void setCollect() {
        if (StringUtil.isEmpty(MyApplication.TOKEN)) {
            ActivityUtil.startLoginActivity(activity);
        } else {
            boolean b = !isCollected.get();
            isCollected.set(b);
            ApiWrapper.getInstance()
                    .collect(postId, b)
                    .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new NetworkSubscriber<String>() {
                        @Override
                        public void onSuccess(String bean) {
                        }
                    });
        }
    }

    /**
     * 事件订阅
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.KILL_POST) {
            activity.finish();
        } else if (i == Event.RV_SCROLL) {
            activity.rvScroll();
        } else if (i == Event.RELOAD_COMMENT) {
            loadPostComments(true);
        }
    }


    /**
     * 加载评论
     *
     * @param b 是否滚动到指定位置
     */
    public void loadPostComments(boolean b) {
        pageNo = 1;
        getPostCommentsData(pageNo, b);
    }

    /**
     * 初始化数据
     */
    private void getInitData() {
        Map<String, String> extraMap = ActivityUtil.getExtraMap(activity); //推送数据
        if (extraMap != null) {
            postId = extraMap.get("postId");
        } else {
            postId = activity.getIntent().getStringExtra("postId");
        }
        activity.setPageKey("postId");
        activity.setPageValue(postId);
        rawUrl = URLs.getHtmlPost(postId);
    }

    /**
     * @param rxCommentInfoRxList
     * @param pageNo
     * @param b
     */
    private void setPostCommentsData(RxList<RxCommentInfo> rxCommentInfoRxList, int pageNo, boolean b) {
        try {
            List<RxCommentInfo> list = rxCommentInfoRxList.getList();
            if (pageNo == 1) {
                adapter.setNewCommentData(list);
            } else {
                adapter.addCommentData(list);
            }
            if (list.size() < URLs.PAGE_SIZE) {
                adapter.loadMoreEnd();
                if (pageNo == 1 && list.isEmpty()) {
                    if (adapter.getFooterLayoutCount() == 0) {
                        adapter.addFootView(R.layout.view_comment_none);
                    }
                } else {
                    adapter.removeAllFooterView();
                    if (adapter.getLoadMoreViewCount() == 0) {
                        adapter.addFootView(R.layout.view_bottom_line);
                    }
                }
            } else {
                adapter.loadMoreComplete();
            }
            if (b) {
                activity.rvScroll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        pageNo++;
        getPostCommentsData(pageNo, false);
    }

    /**
     * 点击响应
     */
    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostDetailAdapter detailAdapter = (PostDetailAdapter) adapter;
                int itemViewType = detailAdapter.getItemViewType(position);
                PostDetailMultiItemEntity itemEntity = detailAdapter.getData().get(position);
                switch (itemViewType) {
                    case PostDetailAdapter.POST_TYPE_ACCOUNT:
                        RxPostInfo rxPostInfo = itemEntity.getBean();
                        String userId = rxPostInfo.getAccount().getUserId();
                        ActivityUtil.startPersonalActivity(activity, userId);
                        break;

                    case PostDetailAdapter.POST_TYPE_PLATE:
                        RxPlate plate = itemEntity.getBean();
                        String plateId = plate.getId();
                        ActivityUtil.startPlatePostActivity(activity, plateId);
                        break;

                    case PostDetailAdapter.POST_TYPE_COMMENT:
                        RxCommentInfo bean = itemEntity.getBean();
                        String commentId = bean.getComment().getId();
                        ActivityUtil.startCommentActivity(activity, commentId);
                        break;

                    case PostDetailAdapter.POST_TYPE_GIF_IMG:
                        GifModel gifModel = itemEntity.getBean();
                        if (!gifModel.isGif()) {
                            ViewGroup gifLayout = (ViewGroup) view;
                            View loadingView = activity.getLayoutInflater().inflate(R.layout
                                    .view_drawee_loading, gifLayout, false);

                            AutoUtils.auto(loadingView);
                            gifLayout.addView(loadingView);
                            SimpleDraweeView sdv = ((SimpleDraweeView) gifLayout.findViewById(R.id.sdv));

                            Observable.just(gifModel.getImgUrl())
                                    .map(s -> DraweeViewUtils.getInstance().getCacheDrawable(s))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(drawable -> {
                                        sdv.getHierarchy().setPlaceholderImage(drawable);
                                        gifModel.setGif(true);
                                    });
                        }
                        break;
                }
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                int itemViewType = adapter.getItemViewType(position);
                PostDetailAdapter detailAdapter = (PostDetailAdapter) adapter;
                PostDetailMultiItemEntity itemEntity = detailAdapter.getData().get(position);
                switch (itemViewType) {
                    case PostDetailAdapter.POST_TYPE_STATIC_IMG:
                        contentImageUrl = itemEntity.getBean();
                        Log.e(TAG, "onItemLongClick: " + contentImageUrl);
                        longClickImg();
                        break;
                    case PostDetailAdapter.POST_TYPE_GIF_IMG:
                        GifModel model = itemEntity.getBean();
                        if (model.isGif()) {
                            contentImageUrl = model.getGifUrl();
                            longClickImg();
                        }
                        break;
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PostDetailAdapter detailAdapter = (PostDetailAdapter) adapter;
                PostDetailMultiItemEntity itemEntity = detailAdapter.getData().get(position);

                int itemViewType = adapter.getItemViewType(position);
                switch (itemViewType) {
                    case PostDetailAdapter.POST_TYPE_LIKE_INFO:
                        switch (view.getId()) {
                            case R.id.iv_more_like:
                                String htmlLaudListUrl = URLs.getHTMLLaudListUrl(postId);
                                WebUtil.jumpWeb(htmlLaudListUrl, activity);
                                break;
                            case R.id.ll_zan:
                                if (!MyApplication.isLogin()) {
                                    ActivityUtil.startLoginActivity(activity);
                                } else {
                                    like();
                                }
                                break;
                            case R.id.tv_comment_order:
                                RxPostInfo postInfo = postInfoBean.get();
                                postInfo.setOrder(postInfo.getOrder() == 0 ? 1 : 0);
                                loadPostComments(true);
                                break;
                        }
                        break;
                    case PostDetailAdapter.POST_TYPE_COMMENT:
                        RxCommentInfo commentInfo = itemEntity.getBean();
                        switch (view.getId()) {
                            case R.id.fl_like_icon:
                            case R.id.tv_like_num:
                                if (StringUtil.isEmpty(MyApplication.TOKEN)) {
                                    ActivityUtil.startLoginActivity(activity);
                                } else {
                                    commentLike(commentInfo);
                                    adapter.notifyItemChanged(position, commentInfo.getIsLiked());
                                }
                                break;
                            case R.id.sdv_user_pic:
                            case R.id.tv_user_name:
                            case R.id.tv_time:
                                String userId = commentInfo.getCommentUser().getUserId();
                                ActivityUtil.startPersonalActivity(activity, userId);
                                break;
                            case R.id.tv_more_comment:
                                RxCommentInfo bean = itemEntity.getBean();
                                String commentId = bean.getComment().getId();
                                ActivityUtil.startCommentActivity(activity, commentId);
                                break;
                        }
                        break;
                }
            }
        };
    }

    /**
     * 评论点赞
     */
    private void commentLike(RxCommentInfo commentInfo) {
        RxComment comment = commentInfo.getComment();
        boolean isLiked = !commentInfo.getIsLiked();
        String commentId = comment.getId();
        int likeQty = comment.getLikeQty() + (isLiked ? 1 : -1);
        comment.setLikeQty(likeQty);
        commentInfo.setIsLiked(isLiked);
        ApiWrapper.getInstance()
                .commentLike(commentId, isLiked)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {

                    }
                });
    }

    @Override
    public void destroy() {
        loadingWindow.dismiss();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取帖子点赞用户
     */
    private void getPostLikeData() {
        ApiWrapper.getInstance()
                .getPraisePost(postId, 5, 1)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxList<RxPostLike>>() {
                    @Override
                    public void onSuccess(RxList<RxPostLike> bean) {
                        postLikeList.clear();
                        postLikeList.addAll(bean.getList());
                        if (postLikeList.size() > 10) {
                            postLikeList = postLikeList.subList(0, 10);
                        }
                        likeAdapter.setNewData(postLikeList);
                    }
                });
    }

    /**
     * 设置详情数据
     *
     * @param bean 帖子详情数据
     */
    private void setDetailData(RxPostInfo bean) {
        postInfoBean.set(bean);
        postInfoBean.get().setIsZan(postInfoBean.get().getIsLiked());
        notifyPropertyChanged(BR.postInfoBean);
        activity.initEmotionData();
        RxPost post = bean.getPost();
        int postType = post.getPostType();
        if (postType == 1) {
            entityList.add(new PostDetailMultiItemEntity(PostDetailAdapter.POST_TYPE_PLATE, bean.getPlate()));
            entityList.add(new PostDetailMultiItemEntity(PostDetailAdapter.POST_TYPE_ACCOUNT, bean));
        } else if (postType == 3) {
            entityList.add(new PostDetailMultiItemEntity(PostDetailAdapter.POST_TYPE_VIDEO, bean.getPost()));
        }
        setPostContent(bean);
    }

    /**
     * 帖子内容
     *
     * @param bean
     */
    private void setPostContent(RxPostInfo bean) {
        if (bean == null) return;
        RxPost post = bean.getPost();
        String content = post.getContent();
        getPostContent(content);
//        String picUrls = post.getPicUrl();
//        if (!StringUtil.isEmpty(picUrls)) {
//            String[] urls = picUrls.split(",");
//            for (final String url : urls) {
//                entityList.add(new PostDetailMultiItemEntity(PostDetailAdapter.POST_TYPE_STATIC_IMG, url));
//            }
//        }
        setHtmlOfficialReply(bean);
    }

    /**
     * 官方回复
     *
     * @param bean
     */
    private void setHtmlOfficialReply(RxPostInfo bean) {
        if (bean == null) return;
        if (bean.isAward()) {
            String remark = bean.getPostAward().getAwardRemark();
            entityList.add(new PostDetailMultiItemEntity(PostDetailAdapter.POST_TYPE_AWARD, remark));
        }
        RxPlateFormReply plateFormReply = bean.getPlateFormReply();
        if (plateFormReply != null) {
            String replyContent = plateFormReply.getReplyContent();
            entityList.add(new PostDetailMultiItemEntity(PostDetailAdapter.POST_TYPE_OFFICIAL_REPLY, replyContent));
        }
        entityList.add(new PostDetailMultiItemEntity(PostDetailAdapter.POST_TYPE_LIKE_INFO, bean));
    }

    /**
     * html 图文混编处理
     *
     * @param content
     */
    private void getPostContent(String content) {
        List<String> stringList = getStringList(content);
        //处理图片和文字
        for (String string : stringList) {
            //图片处理
            if (isPicUrl(string)) {
                ImageDispose(string);
            } else { //文字处理
                textDispose(string);
            }
        }
    }

    /**
     * 文字处理
     *
     * @param string
     */
    private void textDispose(String string) {
        String text = string;
        if (text.contains("<") && text.contains(">")) {
            String str = text.trim();
            String end = str.replaceFirst("<p>$", "");
            text = end.replaceFirst("^</p>", "");
        }
        if (!StringUtil.isEmpty(text)) {
            entityList.add(new PostDetailMultiItemEntity(
                    PostDetailAdapter.POST_TYPE_TEXT, string));
        }
    }

    /**
     * 图片处理
     *
     * @param string
     */
    private void ImageDispose(String string) {
        //gif处理
        if (string.contains(".gif")) {
            GifModel gifModel = new GifModel(string);
            entityList.add(new PostDetailMultiItemEntity(
                    PostDetailAdapter.POST_TYPE_GIF_IMG, gifModel));
        } else { //静态图片处理
            entityList.add(new PostDetailMultiItemEntity(
                    PostDetailAdapter.POST_TYPE_STATIC_IMG, string));
        }
    }

    /**
     * 抽取HTML 标签文字和图片
     *
     * @param content
     * @return
     */
    public List<String> getStringList(String content) {
        List<String> stringList = new ArrayList<>();
        //抽取文字和图片
        String[] split = content.split("<img");
        for (String s : split) {

            if (isPicUrl(s)) {
                if (s.contains("src=\"data:image")) {
                    String[] bases=s.split("src=\"");
                    String baseString = bases[1];
                    baseString = baseString.substring(0, baseString.indexOf("\""));
                    stringList.add(baseString);
                } else {
                    s = s.substring(s.indexOf("http"));
                    int i = 0;
                    if (s.contains("\"")) {
                        i = s.indexOf("\"");
                    } else {
                        i = s.indexOf(">") - 1;
                    }
                    int j = s.indexOf(">");
                    String imgUrl = s.substring(0, i);
                    stringList.add(imgUrl);
                    if (j < s.length()) {
                        s.replaceAll("/>", "");
                        stringList.add(s.substring(j + 1));
                    }
                }


            } else {
                stringList.add(s);
            }
        }
        return stringList;
    }

    private boolean isPicUrl(String url) {
        if (url.contains("src=\"data:image") || ((url.contains("http://") || url.contains("https://")) &&
                (url.contains(".jpg") || url.contains(".jpeg")
                        || url.contains(".png") || url.contains(".bmp") || url.contains(".JPG") || url.contains(".JPEG")
                        || url.contains(".PNG") || url.contains(".BMP") || url.contains(".gif") || url.contains("=jpg") || url.contains("=jpeg")
                        || url.contains("=png") || url.contains("=bmp") || url.contains("=JPG") || url.contains("=JPEG")
                        || url.contains("=PNG") || url.contains("=BMP") || url.contains("=gif")))) {
            return true;
        }
        return false;
    }

    /**
     * 分享图片
     *
     * @param uri 图片uri
     */
    private void shareImg(Uri uri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        activity.startActivity(Intent.createChooser(shareIntent, "请选择"));
    }

    private void longClickImg() {
        if (builder == null) {
            builder = ActionSheet.createBuilder(activity, activity.getSupportFragmentManager())
                    .setCancelButtonTitle("取消")
                    .setOtherButtonTitles("发送", "保存到手机")
                    .setCancelableOnTouchOutside(true)
                    .setListener(new ActionSheet.ActionSheetListener() {
                        @Override
                        public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                        }

                        @Override
                        public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                            File newFile = DraweeViewUtils.getInstance().saveCacheDrawable(contentImageUrl);
                            switch (index) {
                                case 0:
                                    sendImg(newFile);
                                    break;
                                case 1:
                                    saveImg(newFile);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
        }
        builder.show();
    }

    /**
     * 保存图片到手机
     */
    private void saveImg(File newFile) {
        if (newFile != null && newFile.exists()) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(newFile);
            intent.setData(uri);
            activity.sendBroadcast(intent);
            ToastUtil.toast("图片已保存到：" + newFile.getAbsolutePath());
        } else {
            ToastUtil.toast("图片保存失败");
        }
    }

    /**
     * 发送图片
     */
    private void sendImg(File newFile) {
        if (newFile != null && newFile.exists()) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(newFile);
            intent.setData(uri);
            shareImg(uri);
            activity.sendBroadcast(intent);
        } else {
            ToastUtil.toast("发送失败");
        }
    }

    /**
     * 点赞帖子
     */
    public void like() {
        boolean isLiked = !postInfoBean.get().getIsLiked();
        dealPostLike(isLiked);
        postInfoBean.get().setIsLiked(isLiked);
        postInfoBean.get().getPost().setLikeQty(postInfoBean.get().getPost().getLikeQty() + (isLiked ? 1 : -1));
        String postId = postInfoBean.get().getPost().getId();
        ApiWrapper.getInstance()
                .postLike(postId, isLiked)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {

                    }
                });
    }

    /**
     * 本地处理点赞用户数据
     *
     * @param isLiked
     */
    private void dealPostLike(boolean isLiked) {
        RxUserInfo userInfo = MyApplication.mCache.getAsJSONBean(CacheKey.USER_INFO, RxUserInfo.class);
        if (userInfo == null) {
            return;
        }
        if (isLiked) {
            RxPostLike postLike = new RxPostLike();
            postLike.setLikeUserId(userInfo.getUserId());
            postLike.setLikeUser(userInfo);
            postLikeList.add(0, postLike);
            likeAdapter.notifyItemInserted(0);
        } else {
            for (int i = 0; i < postLikeList.size(); i++) {
                if (TextUtils.equals(postLikeList.get(i).getLikeUserId(), userInfo.getUserId())) {
                    postLikeList.remove(i);
                    likeAdapter.notifyItemRemoved(i);
                    break;
                }
            }
        }
    }

}
