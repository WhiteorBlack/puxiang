package com.puxiang.mall.module.plate.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.databinding.ActivityPlateNewBinding;
import com.puxiang.mall.model.data.RxPlate;
import com.puxiang.mall.module.login.view.LoginFragment;
import com.puxiang.mall.module.main.view.MainActivity;
import com.puxiang.mall.module.plate.adapter.PlatePostAdapter;
import com.puxiang.mall.module.plate.viewmodel.PlateDetailViewModel;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.NetworkUtil;
import com.puxiang.mall.utils.RecycleViewUtils;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.widget.AppBarStateChangeListener;

import org.greenrobot.eventbus.EventBus;

public class PlatePostActivityNew extends BaseBindActivity {

    private PlatePostAdapter adapter;
    private ActivityPlateNewBinding binding;
    private PlateDetailViewModel viewModel;
    private AnimatorSet set2;
    private AnimatorSet set1;

    @Override
    protected void initBind() {
        EventBus.getDefault().post(Event.KILL_PLATE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_plate_new);
        adapter = new PlatePostAdapter(R.layout.item_circle, this);
        viewModel = new PlateDetailViewModel(this, adapter);
        binding.setViewModel(viewModel);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void initView() {
        initToolbar(binding.mobileToolbar);
        initRefresh(binding.mPtrFrame, false);
        initRv(binding.rvPlate);
        binding.cbAllExplain.setOnCheckedChangeListener((buttonView, isChecked) ->
                binding.tvExplain.setMaxLines(isChecked ? 100 : 1));
    }

    public void setExplain() {
        binding.tvExplain.postDelayed(() -> {
            if (binding.tvExplain.getLineCount() > 1) {
                binding.cbAllExplain.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        viewModel.getData();
    }

    private boolean isVisible = false;

    private void initRv(RecyclerView rvPlate) {
        RecycleViewUtils.setCollapseEmptyView(adapter, rvPlate, getLayoutInflater(), "暂无发帖~");
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rvPlate);

        rvPlate.addOnItemTouchListener(viewModel.itemClickListener());
        rvPlate.setLayoutManager(new LinearLayoutManager(this));
        rvPlate.setAdapter(adapter);


        FloatingActionButton ivPut = binding.ivPut;
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(ivPut, "translationY", 50f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(ivPut, "alpha", 1f, 0);
        set1 = new AnimatorSet();
        set1.setDuration(200);
        set1.playTogether(animator1, animator2);

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(ivPut, "translationY", 50f, 0f);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(ivPut, "alpha", 0, 1f);
        set2 = new AnimatorSet();
        set2.setDuration(200);
        set2.playTogether(animator3, animator4);

        rvPlate.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int status = 1;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //    Log.e(TAG, "onScrolled: " + dy);
                if (status == 1) {
                    if (dy > 0) {
                        if (isVisible) {
                            if (!set1.isRunning() && !set2.isRunning()) {
                                set1.start();
                                isVisible = false;
                            }
                        }
                    } else {
                        if (!isVisible) {
                            if (!set1.isRunning() && !set2.isRunning()) {
                                set2.start();
                                isVisible = true;
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //     Log.e(TAG, "onScrollStateChanged: " + newState);

                status = newState;
                if (!adapter.isLoading() && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isVisible) {
                        if (!set1.isRunning() && !set2.isRunning()) {
                            set2.start();
                            isVisible = true;
                        }
                    }
                }
            }
        });

        binding.abl.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                //    Log.d("STATE", state.name());
                if (state == State.EXPANDED) {
                    binding.mPtrFrame.setEnabled(true);
                    binding.ctl.setTitle("");
                    binding.ctl.setContentScrimColor(getResources().getColor(R.color.Alpha_theme));
                    mImmersionBar.transparentBar();
                    //展开状态
                } else if (state == State.COLLAPSED) {
                    binding.mPtrFrame.setEnabled(false);
                    //折叠状态
//                    ctl.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_GRAVITY);
                    binding.ctl.setTitle("圈子");
                    binding.ctl.setCollapsedTitleTextColor(getResources().getColor(R.color.searchTextColor));
                    binding.ctl.setContentScrimColor(getResources().getColor(R.color.toolbarColor));
                    mImmersionBar.barColor(R.color.toolbarColor);
                } else {
                    binding.mPtrFrame.setEnabled(false);
                    binding.ctl.setTitle("");
                    binding.ctl.setContentScrimColor(getResources().getColor(R.color.white_alpha));
                    mImmersionBar.barColor(R.color.white_alpha);
                    //中间状态
                }
            }
        });
    }

    @Override
    public void refreshOK() {
        super.refreshOK();
        if (!isVisible) {
            if (!set1.isRunning() && !set2.isRunning()) {
                set2.start();
                isVisible = true;
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_put:
                if (StringUtil.isEmpty(MyApplication.TOKEN)) {
                    startActivity(new Intent(this, LoginFragment.class));
                } else {
                    if (!NetworkUtil.isNetworkAvailable(this)) {
                        ToastUtil.toast("当前网络不可用！");
                    } else {
                        RxPlate plate = viewModel.plateBean.get();
                        ActivityUtil.startPublishActivity(this, plate.getId(), null, plate
                                .getPlateName());
                    }
                }
                break;
            case R.id.tv_explain:
                binding.cbAllExplain.setChecked(!binding.cbAllExplain.isChecked());
                break;
            case R.id.v_plate_jion:
                if (StringUtil.isEmpty(MyApplication.TOKEN)) {
                    startActivity(new Intent(this, LoginFragment.class));
                } else {
                    viewModel.setAttentPlate();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        viewModel.share();
        return true;
    }


    @Override
    public void onBackPressed() {
        if (!MainActivity.isInit) {
            ActivityUtil.startMainActivity(this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }
}
