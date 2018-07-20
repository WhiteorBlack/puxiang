package com.puxiang.mall.module.mall.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentMallBinding;
import com.puxiang.mall.databinding.ViewBottomLineBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.mall.adapter.MallClassAdapter;
import com.puxiang.mall.module.mall.adapter.SectionAdapterNew;
import com.puxiang.mall.module.mall.viewmodel.MallHeadViewModel;
import com.puxiang.mall.module.mall.viewmodel.MallPicAdds;
import com.puxiang.mall.module.mall.viewmodel.MallViewModel;
import com.puxiang.mall.module.mall.viewmodel.MsgCountViewModel;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.NetworkUtil;
import com.puxiang.mall.utils.RecycleViewUtils;
import com.puxiang.mall.utils.ScreenUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * Created by 123 on 2017/9/2.
 */

public class MallFragment extends BaseBindFragment implements View.OnClickListener {

    private FragmentMallBinding binding;
    private MallViewModel viewModel;
    private ViewBottomLineBinding bottomLineBinding;
    private SectionAdapterNew adapter;
    private MallHeadViewModel headViewModel;
    private MallClassAdapter mallClassAdapter;

    private MallPicAdds mallPicAdds;
    private MsgCountViewModel msgCountViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_scan:
                ActivityUtil.startQRCodeActivityForResult(this.getActivity(), 0, "", 0.0, 0.0);
                break;
            case R.id.iv_warn:
                if (!MyApplication.isLogin()) {
                    ActivityUtil.startLoginActivity(getActivity());
                    return;
                }
                if (RongIM.getInstance() != null) {
                    WeakReference<Context> wr = new WeakReference<>(
                            getContext());
                    Map<String, Boolean> map = new HashMap<>();
                    map.put(Conversation.ConversationType.PRIVATE.getName(), false);
                    RongIM.getInstance().startConversationList(wr.get(), map);
                }
                MyApplication.messageState.setMyMessage(0);
                break;
            case R.id.fl_search:
                String searchText = binding.toolbarMall.tvSearch.getText().toString();
                ActivityUtil.startSearchActivity(this.getActivity(), TextUtils.isEmpty(searchText) ? "" : searchText);
                break;
            case R.id.iv_to_top:
                binding.nsvParent.smoothScrollBy(0, 20);
                break;
        }
    }

    @Override
    public void update() {
        super.update();
        refresh();
    }

    public void refresh() {
        binding.ptrFrame.autoRefresh();
    }

    @Override
    public void refreshData() {
        super.refreshData();
        headViewModel.getClassDdata();
        headViewModel.getBannerData();
        headViewModel.getHotLine();
        headViewModel.getRecommend();
        mallPicAdds.getRecommend();
        viewModel.getSearchTextData();
        viewModel.getRecommendProducts("recommendFloor1");
        msgCountViewModel.getMsgCountData();
    }

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mall, container, false);
        bottomLineBinding = DataBindingUtil.inflate(inflater, R.layout.view_bottom_line, container, false);
        adapter = new SectionAdapterNew(R.layout.item_mall_goods, R.layout.item_mall_title, null);

        viewModel = new MallViewModel(this, adapter);
        mallPicAdds = new MallPicAdds(this);
        msgCountViewModel = new MsgCountViewModel(this);
        binding.setMsgModel(msgCountViewModel);
        binding.setViewModel(viewModel);
        binding.layoutHead.setViewModel(headViewModel);
        binding.layoutHead.setPicModel(mallPicAdds);
        bottomLineBinding.setViewModel(viewModel);
        binding.toolbarMall.setMessageState(MyApplication.messageState);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        initHeadView();
        initBanner(binding.layoutHead.banner);
        initGoodsRecyclerView(binding.rvMall);
        initRefresh(binding.ptrFrame);
        ImmersionBar.with(this).statusBarDarkFont(false).init();
    }


    private void initGoodsRecyclerView(RecyclerView rvMall) {
        adapter.addFooterView(bottomLineBinding.getRoot());
        rvMall.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        rvMall.setAdapter(adapter);
        rvMall.setFocusableInTouchMode(false);
        rvMall.setFocusable(false);
        rvMall.setNestedScrollingEnabled(false);
        if (!NetworkUtil.isNetworkAvailable(getContext())) {
            RecycleViewUtils.setNetWorkOutView(adapter, rvMall, getLayoutInflater(), "");
        }
        binding.nsvParent.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (oldScrollY > ScreenUtil.getWidthAndHeight().heightPixels) {
                viewModel.setIsVisible(true);

            } else {
                viewModel.setIsVisible(false);
            }
            if (oldScrollY > binding.layoutHead.banner.getHeight()) {
                binding.toolbarMall.llParent.setBackgroundResource(R.mipmap.mall_bar_bg);
            } else {
                binding.toolbarMall.llParent.setBackgroundResource(0);
            }
        });

        binding.ivToTop.setOnClickListener(this);
        rvMall.addOnItemTouchListener(viewModel.itemClickListener());
    }

    private void initHeadView() {
        mallClassAdapter = new MallClassAdapter(R.layout.item_circle_navigate, null);
        headViewModel = new MallHeadViewModel(this, mallClassAdapter);
        binding.layoutHead.banner.setAdapter(headViewModel);
        binding.layoutHead.banner.setDelegate(headViewModel);
        binding.layoutHead.banner.setParentView(binding.ptrFrame);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.layoutHead.banner.getLayoutParams();
        params.height = (int) (MyApplication.widthPixels * 0.54);
        binding.layoutHead.banner.setLayoutParams(params);
        binding.toolbarMall.flSearch.setOnClickListener(this);
        binding.toolbarMall.ivScan.setOnClickListener(this);
        binding.toolbarMall.ivWarn.setOnClickListener(this);
        binding.layoutHead.setViewModel(headViewModel);

        binding.layoutHead.rvClasses.setAdapter(mallClassAdapter);

        binding.layoutHead.rvClasses.setLayoutManager(new GridLayoutManager(this.getContext(), 5));
    }

    private Observable.OnPropertyChangedCallback callback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
        }
    };

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
        if (headViewModel != null) headViewModel.destroy();
        if (msgCountViewModel != null) msgCountViewModel.destroy();
        MyApplication.isLoginOB.removeOnPropertyChangedCallback(callback);
    }

}
