package com.puxiang.mall;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.puxiang.mall.base.ErrorShow;
import com.puxiang.mall.databinding.ToolbarLayoutBinding;
import com.puxiang.mall.module.bbs.viewmodel.BbsRequest;
import com.puxiang.mall.utils.AppUtil;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import cn.bingoogolapple.bgabanner.BGABanner;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by 123 on 2017/9/1.
 */

public abstract class BaseBindActivity extends RxAppCompatActivity implements BbsRequest.RefreshListener, ErrorShow {
    private PtrFrameLayout mPtrFrame;
    protected String TAG;
    private boolean isAuto = true;
    private String key;
    private String value;
    private ViewGroup rootView;
    private View noneView;
    public ImmersionBar mImmersionBar;
    private boolean isVisibleUpdate = true;
    private BGABanner banner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(false).flymeOSStatusBarFontColor(R.color.white).keyboardEnable(true).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE).init();
        initBind();
        rootView = (ViewGroup) getWindow().getDecorView();
        //是否自动适配
        if (isAuto) {
            AutoUtils.setSize(this, true, 720, 1154);

            AutoUtils.auto(this);
        }

        initView();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    public ViewGroup getRootView() {
        return rootView;
    }

    /**
     * 是否开启自动适配
     *
     * @param isAuto
     */
    public void setAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }

    /**
     * 用于初始化bind
     */
    protected abstract void initBind();

    /**
     * 初始化Views
     */
    public abstract void initView();

    /**
     * 上传页面信息
     *
     * @param key
     * @param value
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
     * 初始化ToolBar
     *
     * @param toolbar
     */
    public void initToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(R.mipmap.nav_back_w);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * 设置红色title
     *
     * @param toolbar
     */
    public void setRedTitle(ToolbarLayoutBinding toolbar) {
        toolbar.setBackSrc(R.mipmap.nav_back_w);
        toolbar.setColor(R.color.mall_activity);
        toolbar.setTextColor(R.color.white);
    }

    /**
     * 设置白色title
     *
     * @param toolbar
     */
    public void setWhiteTitle(ToolbarLayoutBinding toolbar) {
        toolbar.setBackSrc(R.mipmap.nav_back_g);
        toolbar.setColor(R.color.white);
        toolbar.setTextColor(R.color.text_black);

    }

    /**
     * 设置状态栏高度
     *
     * @param imageView
     */
    public void setBarHeight(ImageView imageView) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        params.height = AppUtil.getStatusBarHeight(this);
        imageView.setLayoutParams(params);
    }

    /**
     * 设置状态栏高度
     *
     * @param imageView
     */
    public void setBarHeightRel(ImageView imageView) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.height = AppUtil.getStatusBarHeight(this);
        imageView.setLayoutParams(params);
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
     * 懒加载回调
     */
    public void update() {
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

    @Override
    protected void onPause() {
        super.onPause();
        onUserInvisible();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onUserVisible();
        MobclickAgent.onResume(this);
    }

    /**
     * 初始化下拉刷新
     *
     * @param mPtrFrame 刷新布局
     * @param stateBar  是否含有状态栏
     */
    public void initRefresh(final PtrFrameLayout mPtrFrame, boolean stateBar) {
        this.mPtrFrame = mPtrFrame;
//        RefreshHeader ptrUIHandler = new RefreshHeader(this, stateBar);
//        mPtrFrame.addPtrUIHandler(ptrUIHandler);
//        mPtrFrame.setHeaderView(ptrUIHandler);
//        mPtrFrame.setPtrHandler(new PtrHandler() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                refreshData();
//            }
//
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//            }
//        });
    }

    /**
     * 刷新回调
     */
    public void refreshData() {
    }

    /**
     * 退出  已添加跳转动画
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /**
     * Activity销毁 提供抽象方法viewModelDestroy（）
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModelDestroy();
        mImmersionBar.destroy();
    }

    /**
     * Activity销毁 回调
     */
    protected abstract void viewModelDestroy();

    private long resumeTime = 0;
    private HashMap<String, String> statMap;


    /**
     * 关闭软键盘
     */
    public void closeInput() {
        InputMethodManager inputMethodManager = ((InputMethodManager) getSystemService(Context
                .INPUT_METHOD_SERVICE));
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void initRefresh(final PtrFrameLayout mPtrFrame) {
        initRefresh(mPtrFrame, true);
    }

    private int getDuration() {
        return (int) (System.currentTimeMillis() - resumeTime);
    }

    /**
     * 刷新成功
     */
    @Override
    public void refreshOK() {
        if (mPtrFrame != null && mPtrFrame.isRefreshing()) {
            ToastUtil.toast("更新完成");
            mPtrFrame.refreshComplete();
        }
    }

    /**
     * 刷新失败
     */
    @Override
    public void refreshFail() {
        if (mPtrFrame != null && mPtrFrame.isRefreshing()) {
            mPtrFrame.refreshComplete();
        }
    }


    @Override
    public void showNoneView() {

    }

    @Override
    public void showNoneView(String tips) {

    }

    @Override
    public void showNoneView(View.OnClickListener clickListener) {

    }

    @Override
    public void showNoneView(String tips, View.OnClickListener clickListener) {

    }

    @Override
    public void hidNoneView() {

    }
}
