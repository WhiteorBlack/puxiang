package com.puxiang.mall.module.post.view;

import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityCommentDetailBinding;
import com.puxiang.mall.databinding.ViewCommentDetailHeadviewBinding;
import com.puxiang.mall.model.data.RxPostComment;
import com.puxiang.mall.module.post.adapter.CommentDetailQuickAdapter;
import com.puxiang.mall.module.post.viewmodel.CommentViewModel;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.RecycleViewUtils;

public class CommentActivity extends BaseBindActivity implements View.OnClickListener {


    private CommentDetailQuickAdapter adapter;
    private EmotionCommentFragment emotionMainFragment;
    private ActivityCommentDetailBinding binding;
    public CommentViewModel viewModel;


    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_detail);
        adapter = new CommentDetailQuickAdapter(R.layout.item_comment_detail);
        viewModel = new CommentViewModel(this, adapter);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("更多评论");
        initRv(binding.rvComment);
        initEmotionMainFragment(binding.rvComment);
        setBarHeight(binding.toolbar.ivBar);
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    /**
     * 初始化表情面板
     */
    public void initEmotionMainFragment(RecyclerView rvComment) {


        //创建修改实例
        emotionMainFragment = new EmotionCommentFragment();
        emotionMainFragment.bindToContentView(rvComment);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in thefragment_container view with this fragment,
        // and add the transaction to the backstack
        transaction.replace(R.id.fl_emotion, emotionMainFragment);
        //提交修改
        transaction.commit();
        rvComment.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                if (emotionMainFragment != null) {
                    if (!emotionMainFragment.isInterceptBackPress()) {
                        rvComment.requestFocus();
                    }
                }
            }
            return false;
        });
    }

    private void initRv(RecyclerView rvComment) {
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setEnableLoadMore(false);
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore());

        rvComment.addOnItemTouchListener(viewModel.itemClickListener());
        rvComment.setLayoutManager(new LinearLayoutManager(this));
        rvComment.setAdapter(adapter);
    }

    public void initHeadView(RxPostComment item) {
        ViewCommentDetailHeadviewBinding headBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_comment_detail_headview, (ViewGroup) binding.getRoot(), false);
        AutoUtils.auto(headBinding.getRoot());
        headBinding.tvUserName.setOnClickListener(this);
        headBinding.sdvUserPic.setOnClickListener(this);
        headBinding.tvTime.setOnClickListener(this);
//        headBinding.tvPostTitle.setOnClickListener(this);
        headBinding.setItem(item);
        adapter.addHeaderView(headBinding.getRoot());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
//            case R.id.tv_post_title:
//                ActivityUtil.startPostDetailActivity(this, viewModel.postId);
//                break;
            case R.id.tv_time:
            case R.id.sdv_user_pic:
            case R.id.tv_user_name:
                ActivityUtil.startPersonalActivity(this, viewModel.userId);
                break;
        }
    }
}
