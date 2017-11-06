package com.puxiang.mall.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.flyco.dialog.widget.NormalDialog;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.base.ErrorShow;
import com.puxiang.mall.module.bbs.viewmodel.BbsRequest;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.widget.RefreshHeader;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import cn.bingoogolapple.bgabanner.BGABanner;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Desc : Fragment 基类 extends RxFragment 用于RxLifecycle
 */
public abstract class BaseBindFragment extends RxFragment implements BbsRequest.RefreshListener, ErrorShow {

    private PtrFrameLayout mPtrFrame;
    protected boolean mIsPrepared = false;
    protected boolean mIsFirstResume = true;
    protected boolean mIsFirstVisible = true;
    protected boolean mIsFirstInvisible = true;
    private BGABanner banner;
    private boolean isVisibleUpdate = true;
    public NormalDialog dialog;

    protected String TAG;
    private ViewGroup rootView;
    private String key;
    private String value;
    private Map<String, String> statMap;
    private long resumeTime;
    private View noneView;
//    public ImmersionBar immersionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        immersionBar = ImmersionBar.with(this);
//        immersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
//        immersionBar.transparentStatusBar().statusBarDarkFont(true).flymeOSStatusBarFontColor(R.color.text_black);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = (ViewGroup) initBinding(inflater, container);
            AutoUtils.auto(rootView);
            initView();
        }
        return rootView;
    }

    /**
     * 关闭软键盘
     */
    public void closeInput() {
        InputMethodManager inputMethodManager = ((InputMethodManager) getContext().getSystemService(Context
                .INPUT_METHOD_SERVICE));
        if (inputMethodManager != null && this.getActivity().getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getActivity().getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public ViewGroup getRootView() {
        return rootView;
    }

    /**
     * 初始化 bind
     *
     * @param inflater
     * @param container
     * @return
     */
    public abstract View initBinding(LayoutInflater inflater, ViewGroup container);

    /**
     * 初始化views
     */
    public abstract void initView();

    public void showNoneView() {
        showNoneView("");
    }

    public void showNoneView(View.OnClickListener clickListener) {
        showNoneView(null, clickListener);
    }

    public void showNoneView(String tips) {
        showNoneView(tips, null);
    }

    public void showNoneView(String tips, View.OnClickListener clickListener) {
        if (noneView == null) {
            noneView = LayoutInflater.from(MyApplication.getContext())
                    .inflate(R.layout.view_home_none, rootView, false);
            AutoUtils.auto(noneView);
            rootView.addView(noneView);
            if (StringUtil.isEmpty(tips)) {
                TextView tvNone = (TextView) noneView.findViewById(R.id.tv_none);
                tvNone.setText(tips);
            }
            if (clickListener != null) {
                noneView.setOnClickListener(clickListener);
            }
        } else {
            if (!noneView.isShown()) noneView.setVisibility(View.VISIBLE);
        }
    }

    public void hidNoneView() {
        if (noneView != null && noneView.isShown()) noneView.setVisibility(View.GONE);
    }


    /**
     * 初始化 banner
     *
     * @param banner banner
     */

    protected void initBanner(BGABanner banner) {
        this.banner = banner;
        banner.stopAutoPlay();
    }

    /**
     * 初始化刷新布局
     *
     * @param mPtrFrame 刷新布局
     */
    public void initRefresh(final PtrFrameLayout mPtrFrame) {
        this.mPtrFrame = mPtrFrame;
        RefreshHeader ptrUIHandler = new RefreshHeader(getContext());
        mPtrFrame.addPtrUIHandler(ptrUIHandler);
        mPtrFrame.setHeaderView(ptrUIHandler);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refreshData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    /**
     * 初始化刷新布局
     *
     * @param mPtrFrame 刷新布局
     */
    public void initRefresh(final PtrFrameLayout mPtrFrame,boolean stateBar) {
        this.mPtrFrame = mPtrFrame;
        RefreshHeader ptrUIHandler = new RefreshHeader(getContext(),stateBar);
        mPtrFrame.addPtrUIHandler(ptrUIHandler);
        mPtrFrame.setHeaderView(ptrUIHandler);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refreshData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    /**
     * 刷新逻辑块
     */
    public void refreshData() {

    }


    /**
     * 刷新成功
     */
    @Override
    public void refreshOK() {
        if (mPtrFrame != null && mPtrFrame.isRefreshing()) {
//            ToastUtil.toast("更新完成");
            mPtrFrame.refreshComplete();
        }
    }

    /**
     * 刷新失败
     */
    @Override
    public void refreshFail() {
        if (mPtrFrame != null && mPtrFrame.isRefreshing()) {
            ToastUtil.toast("网络异常");
            mPtrFrame.refreshComplete();
        }
    }

    /**
     * 添加页面统计
     *
     * @param key   当前页面类型
     * @param value Id等
     */
    public void setPageTag(String key, String value) {
        setPageKey(key);
        setPageValue(value);
    }

    public void setPageKey(String key) {
        this.key = key;
    }

    public void setPageValue(String value) {
        this.value = value;
    }

    /**
     * 添加 友盟页面统计
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mIsFirstResume) {
            mIsFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
        MobclickAgent.onPageStart(TAG);
        if (!StringUtil.isEmpty(key)) {
            if (statMap == null) {
                statMap = new HashMap<>();
            }
            resumeTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (immersionBar != null) {
//            immersionBar.destroy();
//        }
    }

    /**
     * 添加 友盟页面统计
     */
    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
        MobclickAgent.onPageEnd(TAG);
        if (!StringUtil.isEmpty(key)) {
            try {
                if (statMap == null) {
                    statMap = new HashMap<>();
                }
                statMap.put(key, value);
                MobclickAgent.onEventValue(getContext(), TAG, statMap, getDuration());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int getDuration() {
        return (int) (System.currentTimeMillis() - resumeTime);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (mIsFirstInvisible) {
                mIsFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    public synchronized void initPrepare() {
        if (mIsPrepared) {
            onFirstUserVisible();
        } else {
            mIsPrepared = true;
        }
    }


    /**
     * 第一次对用户可见时会调用该方法
     */
    protected void onFirstUserVisible() {
        if (banner != null) {
            banner.startAutoPlay();
        }
        if (isVisibleUpdate) {
            update();
        }
    }

    /**
     * 对用户可见时会调用该方法，除了第一次
     */
    public void onUserVisible() {
        if (banner != null) {
            banner.startAutoPlay();
        }
    }

    /**
     * 第一次对用户不可见时会调用该方法
     */
    public void onFirstUserInvisible() {
        if (banner != null) {
            banner.stopAutoPlay();
        }
    }

    /**
     * 对用户不可见时会调用该方法，除了第一次
     */
    public void onUserInvisible() {
        if (banner != null) {
            banner.stopAutoPlay();
        }
    }


    public void setIsFirstUserUpdate(boolean b) {
        isVisibleUpdate = b;
    }


    /**
     * 懒加载回调
     */
    public void update() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }


    /**
     * 提供 viewModelDestroy();
     */
    @Override
    public void onDestroyView() {
        viewModelDestroy();
        super.onDestroyView();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 实现该方法 并在调用viewModel 的 destroy函数
     */
    protected abstract void viewModelDestroy();

}
