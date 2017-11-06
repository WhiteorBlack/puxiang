package com.puxiang.mall.module.my.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentMyBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.login.view.LoginActivity;
import com.puxiang.mall.module.my.viewmodel.MyViewModel;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.network.retrofit.Constant;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
public class MyFragment extends BaseBindFragment implements View.OnClickListener {

    private MyViewModel viewModel;
    private FragmentMyBinding binding;

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
        binding.llMyAbout.setOnClickListener(this);
        binding.llMyAddress.setOnClickListener(this);
        binding.llMyCollect.setOnClickListener(this);
        binding.llMyLike.setOnClickListener(this);
        binding.llMyMessage.setOnClickListener(this);
        binding.llMyOrder.setOnClickListener(this);
        binding.llMyReply.setOnClickListener(this);
        binding.llMyServer.setOnClickListener(this);
        binding.llMySettings.setOnClickListener(this);
        binding.llMyTopic.setOnClickListener(this);
        binding.llMyAgent.setOnClickListener(this);
        binding.myPic.setOnClickListener(this);
        binding.myLoginText.setOnClickListener(this);
        binding.myNextUp.setOnClickListener(this);
    }

    @Override
    public final void update() {
        viewModel.getBgUrl();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!StringUtil.isEmpty(MyApplication.TOKEN)) {
            viewModel.getMessageState();
        }
    }

    public void onClick(View view) {
        if (StringUtil.isEmpty(MyApplication.TOKEN)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        if (viewModel == null) {
            ToastUtil.toast(R.string.no_network);
            return;
        }
        switch (view.getId()) {
            case R.id.my_pic:
            case R.id.my_login_text:
                ActivityUtil.startInfoActivity(getActivity());
                break;
            case R.id.my_next_up:
                ActivityUtil.startPersonalActivity(getActivity(), MyApplication.USER_ID);
                break;
            case R.id.ll_my_order:
                WebUtil.jumpMyWeb(URLs.getHTMLuserInfo(MyApplication.USER_ID, URLs.HTML_MY_ORDER), getContext());
                break;
            case R.id.ll_my_collect:
                WebUtil.jumpMyWeb(URLs.HTML_MY_COLLECT, getContext());
                break;
            case R.id.ll_my_address:
                WebUtil.jumpMyWeb(URLs.HTML_MY_GOOD_ADDRESS, getContext());
                break;
            case R.id.ll_my_agent:
                WebUtil.jumpMyWeb(URLs.HTML_MY_AGENCY_ORDER, getContext());
                break;
            case R.id.ll_my_settings:
                ActivityUtil.startSettingActivity(getActivity(), MyApplication.messageState.isNewestVersion(),
                        viewModel.introduce, viewModel.versionName);
                break;
            case R.id.ll_my_topic:
                WebUtil.jumpMyWeb(URLs.HTML_MY_TALK, getContext());
                break;
            case R.id.ll_my_reply:
                viewModel.setMessageReadTime(Constant.MESSAGE_CODE_COMMENT_ME);
                MyApplication.messageState.setCommentMe(0);
                WebUtil.jumpMyWeb(URLs.HTML_MY_COMMENT, getContext());
                break;
            case R.id.ll_my_like:
                viewModel.setMessageReadTime(Constant.MESSAGE_CODE_LIKE_ME);
                MyApplication.messageState.setLikeMe(0);
                WebUtil.jumpMyWeb(URLs.HTML_MY_LAUD, getContext());
                break;
            case R.id.ll_my_server:
                WebUtil.jumpMyWeb(URLs.HTML_MY_SERVER, getContext());
                break;
            case R.id.ll_my_about:
                WebUtil.jumpMyWeb(URLs.HTML_MY_ABOUT, getContext());
                break;
            case R.id.ll_my_message:
                if (RongIM.getInstance() != null) {
                    WeakReference<Context> wr = new WeakReference<>(
                            getContext());
                    Map<String, Boolean> map = new HashMap<>();
                    map.put(Conversation.ConversationType.PRIVATE.getName(), false);
                    RongIM.getInstance().startConversationList(wr.get(), map);
                }
                MyApplication.messageState.setMyMessage(0);
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
