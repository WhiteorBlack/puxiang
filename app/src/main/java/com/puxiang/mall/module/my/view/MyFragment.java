package com.puxiang.mall.module.my.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.puxiang.mall.BR;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentMyBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxMyItem;
import com.puxiang.mall.model.data.RxOrderState;
import com.puxiang.mall.module.login.view.LoginActivity;
import com.puxiang.mall.module.my.adapter.MyItemAdapter;
import com.puxiang.mall.module.my.viewmodel.MyViewModel;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.Constant;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

public class MyFragment extends BaseBindFragment implements View.OnClickListener {

    private MyViewModel viewModel;
    private FragmentMyBinding binding;
    private MyItemAdapter orderAdapter, buyAdapter, saleAdapter, settingAdapter;

    @Override
    public View initBinding(final LayoutInflater inflater, final ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my, container, false);
        viewModel = new MyViewModel(this);
        if (MyApplication.isLogin()){
            viewModel.setIsSellerVis(MyApplication.messageState.getIsSeller());
        }else {
            viewModel.setIsSellerVis(true);
        }
        binding.setViewModel(viewModel);
        binding.setMessageState(MyApplication.messageState);
//        AutoUtils.auto(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public final void initView() {
        binding.myPic.setOnClickListener(this);
        binding.myLoginText.setOnClickListener(this);
        binding.llAllOrder.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.llUserInfo.setOnClickListener(this);
        binding.tvLogin.setOnClickListener(this);
        binding.tvRegister.setOnClickListener(this);
        initOrderRecyclerView(binding.rvOrder);
        initBuyRecyclerView(binding.rvBuyer);
        initSaleRecyclerView(binding.rvSaler);
        initSettingRecyclerView(binding.rvSetting);
        binding.nsvParent.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY < binding.rlHead.getHeight() - binding.rlBar.getHeight()) {
                viewModel.setIsBarVis(false);
            } else {
                viewModel.setIsBarVis(true);
            }
        });
        MyApplication.messageState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == BR.like) {
                    //赞我的
                    buyAdapter.getData().get(5).setHasMsg(MyApplication.messageState.isLike());
                    buyAdapter.notifyItemChanged(5);
                }
                if (i == BR.commentMe) {
                    //评论我的
                    buyAdapter.getData().get(4).setHasMsg(MyApplication.messageState.isCommentMe());
                    buyAdapter.notifyItemChanged(4);
                }
                if (i == BR.warnMessage) {
                    //我的消息
                    settingAdapter.getData().get(0).setHasMsg(MyApplication.messageState.isWarnMessage());
                }
                if (i == BR.isSeller) {
                    if (MyApplication.isLogin()) {
                        viewModel.setIsSellerVis(MyApplication.messageState.getIsSeller());
                    }
                    if (MyApplication.messageState.getIsSeller()) {
                        buyAdapter.notifyItemRemoved(9);
                    }
                }
                if (i == BR.isDealer) {

                }
                if (i == BR.isMember) {
                    if (!MyApplication.messageState.getIsMember()) {
                        buyAdapter.setNewData(viewModel.getBuyList());
                        viewModel.setIsSellerVis(true);
                    }else {
                        viewModel.setIsSellerVis(MyApplication.messageState.getIsSeller());
                    }

                }
            }
        });
    }

    /**
     * 初始化 系统中心模块儿
     *
     * @param rvBuyer
     */
    private void initSettingRecyclerView(RecyclerView rvBuyer) {
        rvBuyer.setLayoutManager(new GridLayoutManager(getContext(), 4));
        settingAdapter = new MyItemAdapter(R.layout.item_my_bottom, viewModel.getSettingList());
        rvBuyer.setNestedScrollingEnabled(false);
        rvBuyer.setAdapter(settingAdapter);
        settingAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (!MyApplication.isLogin()) {
                ActivityUtil.startLoginActivity(getActivity());
                return;
            }
            switch (viewModel.getSettingList().get(position).getPos()) {
                case 0:
                    if (RongIM.getInstance() != null) {
                        WeakReference<Context> wr = new WeakReference<>(
                                getContext());
                        Map<String, Boolean> map = new HashMap<>();
                        map.put(Conversation.ConversationType.PRIVATE.getName(), false);
                        RongIM.getInstance().startConversationList(wr.get(), map);
                    }
                    MyApplication.messageState.setMyMessage(0);
                    ((List<RxMyItem>) adapter.getData()).get(position).setHasMsg(false);
                    settingAdapter.notifyItemChanged(position);
                    break;

                case 1:
                    ActivityUtil.startSettingActivity(getActivity(), MyApplication.messageState.isNewestVersion(),
                            viewModel.introduce, viewModel.versionName);
                    break;
            }
        });
    }

    /**
     * 初始化 你想要的模块儿
     *
     * @param rvBuyer
     */
    private void initSaleRecyclerView(RecyclerView rvBuyer) {
        rvBuyer.setLayoutManager(new GridLayoutManager(getContext(), 4));
        saleAdapter = new MyItemAdapter(R.layout.item_my_bottom, viewModel.getSaleList());
        rvBuyer.setNestedScrollingEnabled(false);
        rvBuyer.setAdapter(saleAdapter);
        saleAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (viewModel.getSaleList().get(position).getPos()) {
                case 0:
                    //我要进货
                    ActivityUtil.startStockListActivity(getActivity());
                    break;
                case 1:
                    break;
            }
        });
    }

    /**
     * 初始化 我是买家模块儿
     *
     * @param rvBuyer
     */
    private void initBuyRecyclerView(RecyclerView rvBuyer) {
        rvBuyer.setLayoutManager(new GridLayoutManager(getContext(), 4));
        buyAdapter = new MyItemAdapter(R.layout.item_my_bottom, viewModel.getBuyList());
        rvBuyer.setNestedScrollingEnabled(false);
        rvBuyer.setAdapter(buyAdapter);
        buyAdapter.setOnItemClickListener((adapter, view, position) -> {

            switch (buyAdapter.getData().get(position).getPos()) {
                case 0:
                    //我的收藏
                    WebUtil.jumpMyWeb(URLs.HTML_MY_COLLECT, getActivity());
//                    ActivityUtil.startCollectionActivity(getActivity());
                    break;
                case 1:
                    //收货地址
                    ActivityUtil.startPostAddressActivity(getActivity());
                    break;
                case 2:
                    //我的社区
                    if (StringUtil.isEmpty(MyApplication.TOKEN)) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        return;
                    }
                    ActivityUtil.startPersonalActivity(getActivity(), MyApplication.USER_ID);
                    break;
                case 3:
                    //我的发帖
                    WebUtil.jumpMyWeb(URLs.HTML_MY_TALK, getContext());
                    break;
                case 4:
                    //评论我的
                    viewModel.setMessageReadTime(Constant.MESSAGE_CODE_COMMENT_ME);
                    MyApplication.messageState.setCommentMe(false);
                    WebUtil.jumpMyWeb(URLs.HTML_MY_COMMENT, getContext());
                    break;
                case 5:
                    //赞我的
                    viewModel.setMessageReadTime(Constant.MESSAGE_CODE_LIKE_ME);
                    MyApplication.messageState.setLike(false);
                    WebUtil.jumpMyWeb(URLs.HTML_MY_LAUD, getContext());
                    break;
                case 6:
                    //我的积分
                    break;
                case 7:
                    //积分商城
                    WebUtil.jumpWeb(URLs.HTML_EXCHANGE_PAGE, getActivity());
                    break;
                case 8:
                    //任务中心

                    break;
            }
        });
    }

    /**
     * 初始化订单模块儿
     *
     * @param rvOrder
     */
    private void initOrderRecyclerView(RecyclerView rvOrder) {
        orderAdapter = new MyItemAdapter(R.layout.item_my, viewModel.getOrderList());
        rvOrder.setLayoutManager(new GridLayoutManager(getContext(), 5));
        rvOrder.setNestedScrollingEnabled(false);
        rvOrder.setAdapter(orderAdapter);
        orderAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position < 4) {
                WebUtil.jumpMyWeb(URLs.getHTMLuserInfo(MyApplication.USER_ID, URLs.HTML_MY_ORDER) + "&tabIndex=" + (position + 1), getContext());
            } else {
                WebUtil.jumpMyWeb(URLs.getHTMLuserInfo(MyApplication.USER_ID, URLs.HTML_MY_RETURN_ORDER), getContext());
            }
        });
    }

    @Override
    public final void update() {
//        viewModel.getBgUrl();  //去除我的模块顶部背景图
    }

    /**
     * 更新订单状态
     *
     * @param rxOrderState
     */
    public void updateOrderState(RxOrderState rxOrderState) {
        if (rxOrderState == null) {
            List<RxMyItem> orderList = orderAdapter.getData();
            for (int i = 0; i < 4; i++) {
                orderList.get(i).setMsgCount(0);
            }
            orderAdapter.notifyDataSetChanged();
            buyAdapter.getData().get(1).setMsgCount(0);
            buyAdapter.notifyItemChanged(1);
            return;
        }
        List<RxMyItem> orderList = orderAdapter.getData();
        orderList.get(0).setMsgCount(rxOrderState.getBuyerWaitPay());
        orderList.get(1).setMsgCount(rxOrderState.getBuyerWaitDeliveryGoods());
        orderList.get(2).setMsgCount(rxOrderState.getBuyerWaitReceiveGoods());
        orderList.get(3).setMsgCount(rxOrderState.getBuyerWaitComment());
        orderList.get(4).setMsgCount(rxOrderState.getBuyerWaitRefund());
        orderAdapter.notifyDataSetChanged();
        buyAdapter.getData().get(2).setMsgCount(rxOrderState.getSellerCount());
        buyAdapter.notifyItemChanged(2);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.isLogin()) {
            viewModel.getOrderState();
        } else {
            updateOrderState(null);
        }
    }

    public void onClick(View view) {

        if (viewModel == null) {
            ToastUtil.toast(R.string.no_network);
            return;
        }
        switch (view.getId()) {
            case R.id.ll_all_order:
                if (StringUtil.isEmpty(MyApplication.TOKEN)) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                WebUtil.jumpMyWeb(URLs.getHTMLuserInfo(MyApplication.USER_ID, URLs.HTML_MY_ORDER), getContext());
                break;
            case R.id.my_pic:
            case R.id.ll_user_info:
                if (StringUtil.isEmpty(MyApplication.TOKEN)) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
//                ActivityUtil.startInfoActivity(getActivity());
                ActivityUtil.startSettingActivity(getActivity(), MyApplication.messageState.isNewestVersion(),
                        viewModel.introduce, viewModel.versionName);
                break;
            case R.id.my_login_text:
            case R.id.tv_login:
                ActivityUtil.startLoginActivity(getActivity(), true);
                break;
            case R.id.btn_register:
            case R.id.tv_register:
                ActivityUtil.startLoginActivity(getActivity(), false);
                break;

        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

//    private class MyReceivePushMessageListener implements RongIMClient.OnReceiveMessageListener {
//
//        /**
//         * 收到 push 消息的处理。
//         *
//         * @param message push 消息实体。
//         * @return true 自己来弹通知栏提示，false 融云 SDK 来弹通知栏提示。
//         */
//        @Override
//        public boolean onReceived(Message message, int i) {
//            String targetId = message.getTargetId();
//            IMRequest.getUserInfo(targetId);
//            Log.e(TAG, "onReceived: " + targetId);
//            return false;
//        }
//    }
}
