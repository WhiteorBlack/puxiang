package com.puxiang.mall.module.my.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentMyBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.model.data.RxMyItem;
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
        initOrderRecyclerView(binding.rvOrder);
        initBuyRecyclerView(binding.rvBuyer);
        initSaleRecyclerView(binding.rvSaler);
        initSettingRecyclerView(binding.rvSetting);
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
            switch (position) {
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
            switch (position) {
                case 0:
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
            switch (position) {
                case 0:
                    //我的收藏
                    WebUtil.jumpMyWeb(URLs.HTML_MY_COLLECT, getContext());
                    break;
                case 1:
                    //收货地址
                    ActivityUtil.startPostAddressActivity(getActivity());
                    break;
                case 2:
                    //我的社区

                    break;
                case 3:
                    //我的发帖
                    WebUtil.jumpMyWeb(URLs.HTML_MY_TALK, getContext());
                    break;
                case 4:
                    //评论我的
                    viewModel.setMessageReadTime(Constant.MESSAGE_CODE_COMMENT_ME);
                    MyApplication.messageState.setCommentMe(0);
                    WebUtil.jumpMyWeb(URLs.HTML_MY_COMMENT, getContext());
                    break;
                case 5:
                    //赞我的
                    viewModel.setMessageReadTime(Constant.MESSAGE_CODE_LIKE_ME);
                    MyApplication.messageState.setLikeMe(0);
                    WebUtil.jumpMyWeb(URLs.HTML_MY_LAUD, getContext());
                    break;
                case 6:
                    //我的积分
                    break;
                case 7:
                    //积分商城

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
            switch (position) {
                case 0:

                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
            }
        });
    }

    @Override
    public final void update() {
//        viewModel.getBgUrl();  //去除我的模块顶部背景图
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!StringUtil.isEmpty(MyApplication.TOKEN)) {
            viewModel.getMessageState();
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
                if (StringUtil.isEmpty(MyApplication.TOKEN)) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                ActivityUtil.startPersonalActivity(getActivity(), MyApplication.USER_ID);
                break;
            case R.id.my_login_text:
                ActivityUtil.startLoginActivity(getActivity(), true);
                break;
            case R.id.btn_register:
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
