package com.puxiang.mall.module.post.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityPostDetailNewBinding;
import com.puxiang.mall.module.emotion.utils.GlobalOnItemClickManagerUtils;
import com.puxiang.mall.module.emotion.view.fragment.EmotionMainFragment;
import com.puxiang.mall.module.main.view.MainActivity;
import com.puxiang.mall.module.post.adapter.PostDetailAdapter;
import com.puxiang.mall.module.post.utils.FaceUtils;
import com.puxiang.mall.module.post.viewmodel.PostDetailViewModel;
import com.puxiang.mall.utils.RecycleViewUtils;
import com.umeng.socialize.UMShareAPI;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by ChenHengQuan on 2016/9/13.
 * Email nullpointerchan@163.com
 */
public class PostDetailActivity extends BaseBindActivity implements OnClickListener {

    private EmotionMainFragment emotionMainFragment;
    private boolean isInitEmotionMainFragment = false;
    public ActivityPostDetailNewBinding binding;
    public PostDetailViewModel postViewModel;
    private PostDetailAdapter adapter;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail_new);
        postViewModel = new PostDetailViewModel(this);
        adapter = postViewModel.adapter;
        binding.setViewModel(postViewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("帖子详情");
        initEmotionMainFragment();
        initRv();
        mImmersionBar.statusBarDarkFont(true).flymeOSStatusBarFontColor(R.color.text_black);
    }

    public void initEmotionData() {
        emotionMainFragment.setViewModel(postViewModel.postInfoBean);
    }

    private void initRv() {
        adapter.setHasStableIds(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView(false));
        adapter.setEnableLoadMore(false);
        adapter.setOnLoadMoreListener(() -> postViewModel.loadMore(), binding.rvPost);
        // adapter.addHeaderView(headBind.getRoot());
        binding.rvPost.addOnItemTouchListener(postViewModel.itemClickListener());
        binding.rvPost.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPost.setAdapter(adapter);
    }

    public void rvScroll() {
        binding.rvPost.smoothScrollToPosition(postViewModel.detailDataSize);
    }

    /**
     * 初始化表情面板
     */
    public void initEmotionMainFragment() {
        if (!isInitEmotionMainFragment) {
            isInitEmotionMainFragment = true;
            //构建传递参数
            Bundle bundle = new Bundle();
            //绑定主内容编辑框
            bundle.putBoolean(EmotionMainFragment.BIND_TO_EDITTEXT, true);
            //隐藏控件
            bundle.putBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN, true);
            //替换fragment
            //创建修改实例
            //emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, bundle);
            emotionMainFragment = new EmotionMainFragment();
            emotionMainFragment.setArguments(bundle);
            emotionMainFragment.bindToContentView(binding.rvPost);
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // Replace whatever is in thefragment_container view with this fragment,
            // and add the transaction to the backstack
            transaction.replace(R.id.fl_emotion, emotionMainFragment);
            // transaction.addToBackStack(null);
            //提交修改
            transaction.commit();

            binding.rvPost.setOnTouchListener((v, event) -> {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    if (emotionMainFragment != null) {
                        if (!emotionMainFragment.isInterceptBackPress()) {
                            binding.rvPost.requestFocus();
                        }
                    }
                }
                return false;
            });

            binding.rvPost.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                int heightDiff = MyApplication.heightPixels - binding.rvPost.getHeight();
                if (heightDiff > MyApplication.heightPixels / 3) { // 说明键盘是弹出状态
                    emotionMainFragment.setBarState(true);
                } else {
                    emotionMainFragment.setBarState(false);
                    //   binding.rvPost.requestFocus();
                }
            });
        }
    }

    public String getPostId() {
        return postViewModel.postId;
    }

    public void share() {
        postViewModel.share();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.cb_icon:
                postViewModel.setCollect();
                break;
            case R.id.ll_none:
                postViewModel.getData();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        if (!MainActivity.isInit) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            super.onBackPressed();
            Log.e(TAG, "detachToEditText: ");
            GlobalOnItemClickManagerUtils.getInstance().detachToEditText();
            FaceUtils.resetGifRun();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        Log.e("JieCaoVideoPlayer", "onPause: releaseAllVideos");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            try {
                if (adapter.getItemCount() > 0) {
                    recycleVideo();
                }
                adapter.getData().clear();
                binding.rvPost.removeAllViews();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void recycleVideo() {
        if (adapter.getItemViewType(0) == PostDetailAdapter.POST_TYPE_VIDEO) {
            JCVideoPlayerStandard videoPlayer =
                    (JCVideoPlayerStandard) adapter.getViewByPosition(binding.rvPost, 0, R.id.post_video);
            videoPlayer.cancelDismissControlViewTimer();
            videoPlayer.cancelProgressTimer();
            JCMediaManager.textureView = null;
            Log.e("JieCaoVideoPlayer", "onDestroy: ");
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (postViewModel != null) postViewModel.destroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
