package com.puxiang.mall.module.circle.view;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentCircleBinding;
import com.puxiang.mall.databinding.ViewHomeHeadviewBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.bbs.viewmodel.BbsRequest;
import com.puxiang.mall.module.circle.adapter.CircleAdapter;
import com.puxiang.mall.module.circle.adapter.CircleNavigateAdapter;
import com.puxiang.mall.module.circle.viewmodel.CircleHeadViewModel;
import com.puxiang.mall.module.circle.viewmodel.CircleViewModel;
import com.puxiang.mall.module.main.view.MainActivity;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.RecycleViewUtils;

/**
 * 圈子页面
 */
public class CircleFragment extends BaseBindFragment implements BbsRequest.RefreshListener {

    private CircleAdapter adapter;
    private FragmentCircleBinding binding;
    private ViewGroup container;
    private ViewHomeHeadviewBinding headBind;
    private CircleViewModel viewModel;
    private CircleHeadViewModel headViewModel;
    /**
     * toolbar是否需要设置颜色
     */
    public static ObservableBoolean isVisiable = new ObservableBoolean(false);

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        this.container = container;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_circle, container, false);
        adapter = new CircleAdapter(R.layout.item_circle, this);
        viewModel = new CircleViewModel(this, adapter);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void refreshData() {
        viewModel.getData(1);
        if (headViewModel != null) {
            headViewModel.getBannerData(URLs.COMMUNITY_CAROUSEL);
        }
    }

    @Override
    public void update() {
        if (viewModel.isFirst && CirclePageFragment.CREATE_STATUS == CirclePageFragment.STATUS_FIRST) {
            CirclePageFragment.CREATE_STATUS = -1;
            MainActivity.handler.postDelayed(this::refresh, 500);
        }
    }

    public void refresh() {
        binding.ptrFrame.autoRefresh();
    }

    @Override
    public void initView() {
        if (viewModel.isFirst) {
            initHeadView();
            initBanner(headBind.banner);
            headBind.rv.setVisibility(View.GONE);  //类目暂时没用，先隐藏
        }
        initRecyclerView(binding.rv);
        initRefresh(binding.ptrFrame);
    }

    private int totalY = 0;

    private void initRecyclerView(RecyclerView rv) {

        if (viewModel.isFirst) {
            adapter.addHeaderView(headBind.getRoot());
        }
        adapter.setHasStableIds(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> viewModel.loadMore(), rv);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
        rv.addOnItemTouchListener(viewModel.itemClickListener());
        rv.setAdapter(adapter);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Fresco.getImagePipeline().resume();
                        break;
                    default:
                        Fresco.getImagePipeline().pause();
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalY -= dy;
                if (totalY < -300) {
                    isVisiable.set(true);
                } else {
                    isVisiable.set(false);
                }
                isVisiable.notifyChange();
            }
        });
    }

    private void initHeadView() {
        headBind = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.view_home_headview, container,
                false);
        AutoUtils.auto(headBind.getRoot());
        CircleNavigateAdapter navigateAdapter = new CircleNavigateAdapter(R.layout.item_circle_navigate);
        headViewModel = new CircleHeadViewModel(this, navigateAdapter);
        headBind.setViewModel(headViewModel);

        headBind.banner.setAdapter(headViewModel);
        headBind.banner.setDelegate(headViewModel);
        headBind.banner.setParentView(binding.ptrFrame);

        headBind.rv.setLayoutManager(new GridLayoutManager(getContext(), 5));
        headBind.rv.addOnItemTouchListener(headViewModel.itemClickListener());
        headBind.rv.setAdapter(navigateAdapter);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headBind.banner.getLayoutParams();
        params.height = (int) (MyApplication.widthPixels / 1.68);
        headBind.banner.setLayoutParams(params);
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
        if (headViewModel != null) headViewModel.destroy();
    }


}
