package com.puxiang.mall.module.plate.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.model.data.RxList;
import com.puxiang.mall.model.data.RxPlate;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.module.bbs.viewmodel.BbsRequest;
import com.puxiang.mall.module.emotion.viewmodel.adapter.ShareBottomDialog;
import com.puxiang.mall.module.plate.adapter.PlatePostAdapter;
import com.puxiang.mall.module.plate.view.PlatePostActivityNew;
import com.puxiang.mall.module.post.model.ShareInfo;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.network.retrofit.RetrofitUtil;
import com.puxiang.mall.network.rx.RxZipModel;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.LoadingWindow;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class PlateDetailViewModel implements ViewModel {

    private final PlatePostActivityNew activity;
    private final PlatePostAdapter adapter;
    private final LoadingWindow loadingWindow;
    public ObservableField<RxPlate> plateBean = new ObservableField<>();
    public ObservableBoolean isInitData = new ObservableBoolean();
    public ObservableBoolean isJoin = new ObservableBoolean(false);
    private String plateId;
    private int pageNo = 1;
    private String shareUrl;
    private ShareInfo shareInfo;
    private String shareTitle;
    private String shareImage;
    private ShareBottomDialog shareDialog;
    private String rawUrl;

    public PlateDetailViewModel(PlatePostActivityNew activity, PlatePostAdapter adapter) {
        EventBus.getDefault().register(this);
        loadingWindow = new LoadingWindow(activity);
        this.activity = activity;
        this.adapter = adapter;
        initData();
        loadingWindow.delayedShowWindow();
        getPlateDetailData();
        getPlatePosts(1);
    }

    /**
     * 获取圈子数据
     */
    public void getData() {
        Observable<RxPlate> plateDetail = ApiWrapper.getInstance().getPlateDetail(plateId);
        Observable<RxList<RxPostInfo>> platePosts = ApiWrapper.getInstance().platePosts(plateId, 1);
        Observable.zip(plateDetail, platePosts, RxZipModel.Model2::new)
                .doOnTerminate(() -> {
                    activity.refreshOK();
                    isInitData.set(true);
                })
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxZipModel.Model2<RxPlate, RxList<RxPostInfo>>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(RxZipModel.Model2<RxPlate, RxList<RxPostInfo>> bean) {
                        RxPlate plate = bean.getModel1();
                        RxList<RxPostInfo> postInfoList = bean.getModel2();
                        setPlateDetailData(plate);
                        dealData(postInfoList.getList());
                        adapter.setPagingData(postInfoList.getList(), 1);
                    }
                });
    }

    /**
     * 将图片的信息处理成数组
     *
     * @param postInfoList
     */
    private void dealData(List<RxPostInfo> postInfoList) {
        for (RxPostInfo postInfo : postInfoList) {
            if (!TextUtils.isEmpty(postInfo.getPost().getPicUrl())) {
                String[] pics = postInfo.getPost().getPicUrl().split(",");
                postInfo.getPost().setPicUrls(pics);
            }
        }
    }

    private void initData() {
        Map<String, String> extraMap = ActivityUtil.getExtraMap(activity);
        if (extraMap != null) {
            plateId = extraMap.get("plateId");
        } else {
            plateId = activity.getIntent().getStringExtra("plateId");
        }
        activity.setPageTag("plateId", plateId);
        rawUrl = URLs.getHtmlPlate(plateId);
    }

    /**
     * 获取圈子详情数据
     */
    private void getPlateDetailData() {
        ApiWrapper.getInstance()
                .getPlateDetail(plateId)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<RxPlate>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        //     showNoneView("当前网络不可用~");
                    }

                    @Override
                    public void onSuccess(RxPlate bean) {
                        setPlateDetailData(bean);
                    }
                });
    }

    /**
     * 圈子详情数据赋值
     *
     * @param bean 圈子详情数据源
     */
    private void setPlateDetailData(RxPlate bean) {
        shareTitle = bean.getPlateName();
        shareImage = bean.getPlatePic();
        plateBean.set(bean);
        isJoin.set(bean.getIsAttented());
        activity.setExplain();
    }

    /**
     * 获取分享的Url
     */
    private void getShareUrl() {
        ApiWrapper.getInstance().getShareUrl(rawUrl)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(loadingWindow::hidWindow)
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
                shareInfo = new ShareInfo(shareUrl, "来聊聊吧" + shareTitle, shareImage, rawUrl);
            }
            setShareDialog(shareInfo);
        }
    }

    /**
     * 初始化分享窗口
     *
     * @param shareInfo 分享信息
     */
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
     * 获取圈子内帖子信息
     *
     * @param pageNo 分页
     */
    private void getPlatePosts(int pageNo) {
        ApiWrapper.getInstance()
                .platePosts(plateId, pageNo)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> isInitData.set(true))
                .subscribe(new NetworkSubscriber<RxList<RxPostInfo>>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onSuccess(RxList<RxPostInfo> bean) {
                        loadingWindow.hidWindow();
                        List<RxPostInfo> postInfos = bean.getList();
                        dealData(postInfos);
                        adapter.setPagingData(postInfos, pageNo);
                    }
                });
    }

    /**
     * 事件订阅
     *
     * @param i 事件信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Integer i) {
        if (i == Event.KILL_PLATE) { //保持只有一个圈子
            activity.finish();
        } else if (i == Event.RELOAD_PLATE) {
            getPlatePosts(pageNo);
        }
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        pageNo++;
        getPlatePosts(pageNo);
    }

    /**
     * 点击响应
     */
    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<RxPostInfo> datas = adapter.getData();
                RxPostInfo bean = datas.get(position);
                ActivityUtil.startPostDetailActivity(activity, bean.getPost().getId());
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<RxPostInfo> datas = adapter.getData();
                RxPostInfo bean = datas.get(position);
                switch (view.getId()) {
                    case R.id.iv_like_icon:
                        if (!MyApplication.isLogin()) {
                            ActivityUtil.startLoginActivity(activity);
                        } else {
                            BbsRequest.setPostLike(bean);
                        }
                        break;
                    case R.id.tv_user_name:
                    case R.id.sdv_bbs_pic:
                        ActivityUtil.startPersonalActivity(activity, bean.getAccount().getUserId());
                        break;
                }
            }
        };
    }

    /**
     * 加入圈子
     */
    public void setAttentPlate() {
        if (!MyApplication.isLogin()) {
            ActivityUtil.startLoginActivity(activity);
            return;
        }
        RxPlate plate = plateBean.get();
        if (plate == null) {
            ToastUtil.toast(R.string.no_network);
            return;
        }
        String id = plate.getId();

        ApiWrapper.getInstance()
                .joinPlate(id, !isJoin.get())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String bean) {
                        isJoin.set(!isJoin.get());
                    }
                });
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
        loadingWindow.dismiss();
    }
}
