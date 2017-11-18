package com.puxiang.mall.module.post.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.puxiang.mall.BR;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.model.data.PostDetailMultiItemEntity;
import com.puxiang.mall.model.data.RxAccount;
import com.puxiang.mall.model.data.RxCommentInfo;
import com.puxiang.mall.model.data.RxPlate;
import com.puxiang.mall.model.data.RxPost;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.model.data.RxPostLike;
import com.puxiang.mall.module.post.model.GifModel;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

public class PostDetailAdapter extends BaseMultiItemQuickAdapter<PostDetailMultiItemEntity,
        BaseViewHolder> {

    public static final int POST_TYPE_PLATE = 0;
    public static final int POST_TYPE_VIDEO = 1;
    public static final int POST_TYPE_ACCOUNT = 2;
    public static final int POST_TYPE_SHAIDAN = 3;
    public static final int POST_TYPE_LIKE_INFO = 5;
    public static final int POST_TYPE_STATIC_IMG = 6;
    public static final int POST_TYPE_GIF_IMG = 7;
    public static final int POST_TYPE_TEXT = 8;
    public static final int POST_TYPE_COMMENT = 9;
    public static final int POST_TYPE_AWARD = 10;
    public static final int POST_TYPE_OFFICIAL_REPLY = 11;
    private final PostLikeQuickAdapter likeAdapter;

    public PostDetailAdapter(List<PostDetailMultiItemEntity> data, PostLikeQuickAdapter
            likeAdapter) {
        super(data);
        this.likeAdapter = likeAdapter;

        addItemType(POST_TYPE_PLATE, R.layout.layout_post_plate);
        addItemType(POST_TYPE_VIDEO, R.layout.layout_post_video);
        addItemType(POST_TYPE_ACCOUNT, R.layout.layout_post_account);
        addItemType(POST_TYPE_SHAIDAN, R.layout.layout_post_shaidan);
        addItemType(POST_TYPE_LIKE_INFO, R.layout.item_post_like_info);
        addItemType(POST_TYPE_STATIC_IMG, R.layout.item_static_img);
        addItemType(POST_TYPE_GIF_IMG, R.layout.item_gif_img);
        addItemType(POST_TYPE_TEXT, R.layout.item_post_text);
        addItemType(POST_TYPE_COMMENT, R.layout.item_comment);
        addItemType(POST_TYPE_AWARD, R.layout.item_post_award);
        addItemType(POST_TYPE_OFFICIAL_REPLY, R.layout.item_post_reply);
    }


    private RecyclerView.OnItemTouchListener onLikeItemTouchListener = new com.chad.library
            .adapter.base.listener.OnItemClickListener() {

        @Override
        public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            RxPostLike postLike = (RxPostLike) adapter.getData().get(position);
            ActivityUtil.startPersonalActivity(view.getContext(), postLike.getLikeUserId());
        }
    };

//    private int commentCount = 0;
//
//    public void setCommentCount(int num) {
//        commentCount = num;
//    }
//
//    public int getCommentCount() {
//        return commentCount;
//    }

    private List<PostDetailMultiItemEntity> commentList = new ArrayList<>();

    public void setNewCommentData(List<RxCommentInfo> data) {
        if (data == null || data.size() == 0) return;
        mData.removeAll(commentList);
        int start = mData.size();
        commentList.clear();
        for (RxCommentInfo commentInfo : data) {
            commentList.add(new PostDetailMultiItemEntity(PostDetailAdapter.POST_TYPE_COMMENT,
                    commentInfo));
        }
        mData.addAll(commentList);
        notifyDataSetChanged();
//        setNewData(mData);
        //    notifyItemRangeChanged(start, mData.size());
    }

    public void addCommentData(List<RxCommentInfo> data) {
        List<PostDetailMultiItemEntity> newCommentList = new ArrayList<>();
        for (RxCommentInfo commentInfo : data) {
            newCommentList.add(new PostDetailMultiItemEntity(PostDetailAdapter.POST_TYPE_COMMENT,
                    commentInfo));
        }
        commentList.addAll(newCommentList);
        addData(newCommentList);
    }

    public void addFootView(@LayoutRes int resId) {
        View footView = LayoutInflater.from(MyApplication.getContext()).inflate(resId, null);
        AutoUtils.auto(footView);
        addFooterView(footView);
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        View view = mLayoutInflater.inflate(layoutResId, parent, false);
        AutoUtils.auto(view);
        return view;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        int layoutResId = 0;
        boolean isAuto = true;
        switch (viewType) {
            case POST_TYPE_PLATE:
                layoutResId = R.layout.layout_post_plate;
                break;
            case POST_TYPE_VIDEO:
                layoutResId = R.layout.layout_post_video;
                isAuto = false;
                break;
            case POST_TYPE_ACCOUNT:
                layoutResId = R.layout.layout_post_account;
                break;
            case POST_TYPE_SHAIDAN:
                layoutResId = R.layout.layout_post_shaidan;
                break;
            case POST_TYPE_LIKE_INFO:
                layoutResId = R.layout.item_post_like_info;
                break;
            case POST_TYPE_STATIC_IMG:
                layoutResId = R.layout.item_static_img;
                break;
            case POST_TYPE_GIF_IMG:
                layoutResId = R.layout.item_gif_img;
                break;
            case POST_TYPE_TEXT:
                layoutResId = R.layout.item_post_text;
                break;
            case POST_TYPE_COMMENT:
                layoutResId = R.layout.item_comment;
                break;
            case POST_TYPE_AWARD:
                layoutResId = R.layout.item_post_award;
                break;
            case POST_TYPE_OFFICIAL_REPLY:
                layoutResId = R.layout.item_post_reply;
                break;
        }
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        return new BindingViewHolder<>(binding, isAuto);
    }

    @Override
    protected void convert(BaseViewHolder holder, PostDetailMultiItemEntity bean) {
        ViewDataBinding binding = ((BindingViewHolder) holder).getBinding();

        int itemViewType = holder.getItemViewType();
        switch (itemViewType) {
            case POST_TYPE_PLATE:
                RxPlate plate = bean.getBean();
                binding.setVariable(BR.item, plate);
                break;
            case POST_TYPE_VIDEO:
                RxPost post = bean.getBean();
                binding.setVariable(BR.item, post);
                break;
            case POST_TYPE_ACCOUNT:
                RxPostInfo info = bean.getBean();
                RxAccount account = info.getAccount();
                String publishTime = info.getPost().getPublishTime();
                binding.setVariable(BR.item, account);
                binding.setVariable(BR.publishTime, publishTime);
                break;
            case POST_TYPE_SHAIDAN:
                break;
            case POST_TYPE_LIKE_INFO:
                RxPostInfo postInfo = bean.getBean();
                RecyclerView rv = holder.getView(R.id.rv_zan);
                if (rv.getAdapter() == null) {
                    rv.setLayoutManager(new LinearLayoutManager(rv.getContext(),
                            LinearLayoutManager.HORIZONTAL, false));
                    rv.addOnItemTouchListener(onLikeItemTouchListener);
                    rv.setAdapter(likeAdapter);
                }
                holder.addOnClickListener(R.id.iv_more_like)
                        .addOnClickListener(R.id.ll_zan)
                        .addOnClickListener(R.id.tv_comment_order);
                binding.setVariable(BR.item, postInfo);
                break;
            case POST_TYPE_STATIC_IMG:
                String url = bean.getBean();
                if (url.contains("?")) {
                    try {
                        String[] size = url.substring(url.indexOf("?") + 1).split("x");
                        int wide = Integer.parseInt(size[0]);
                        int height = Integer.parseInt(size[1]);
                        float wh = wide * 1.0f / height;
                        ((SimpleDraweeView) holder.getView(R.id.sdv)).setAspectRatio(wh);
                    } catch (Exception e) {

                    }
                }
                binding.setVariable(BR.item, url);
                break;
            case POST_TYPE_GIF_IMG:
                GifModel gifModel = bean.getBean();
                String urlGif=gifModel.getGifUrl();
                if (urlGif.contains("?")) {
                    try {
                        String[] size = urlGif.substring(urlGif.indexOf("?") + 1).split("x");
                        int wide = Integer.parseInt(size[0]);
                        int height = Integer.parseInt(size[1]);
                        float wh = wide * 1.0f / height;
                        ((SimpleDraweeView) holder.getView(R.id.sdv)).setAspectRatio(wh);
                        urlGif=urlGif.substring(0,urlGif.indexOf("?"));
                    } catch (Exception e) {

                    }
                }
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setAutoPlayAnimations(true)
                        .setUri(urlGif)
                        .build();
                ((SimpleDraweeView) holder.getView(R.id.sdv)).setController(controller);
                binding.setVariable(BR.item, gifModel);
                break;
            case POST_TYPE_TEXT:
                String text = bean.getBean();
                binding.setVariable(BR.item, text);
                break;
            case POST_TYPE_COMMENT:
                RxCommentInfo commentInfo = bean.getBean();
                binding.setVariable(BR.item, commentInfo);
                holder.addOnClickListener(R.id.fl_like_icon)
                        .addOnClickListener(R.id.tv_like_num)
                        .addOnClickListener(R.id.sdv_user_pic)
                        .addOnClickListener(R.id.tv_user_name)
                        .addOnClickListener(R.id.tv_time)
                        .addOnClickListener(R.id.tv_more_comment);
                break;

            case POST_TYPE_AWARD:
                String award = bean.getBean();
                binding.setVariable(BR.item, award);
                break;
            case POST_TYPE_OFFICIAL_REPLY:
                String reply = bean.getBean();
                binding.setVariable(BR.item, reply);
                break;
        }
        binding.executePendingBindings();
    }
}
