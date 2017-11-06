package com.puxiang.mall.module.im.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.WindowManager;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityConversationBinding;
import com.puxiang.mall.module.im.viewmodel.ConversationViewModel;
import com.puxiang.mall.module.main.view.MainActivity;
import com.puxiang.mall.utils.AutoUtils;

import io.rong.imlib.model.Conversation;

public class ConversationActivity extends BaseBindActivity {

    /**
     * 目标 Id
     */
    private String mTargetId;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    private ActivityConversationBinding binding;
    private ConversationViewModel viewModel;

    @Override
    protected void initBind() {
        mImmersionBar.keyboardEnable(true).keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE).init();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_conversation);
        viewModel = new ConversationViewModel(this);
        binding.setViewModel(viewModel);
        setAuto(false);
    }

    @Override
    public void initView() {
        AutoUtils.setSize(this, true, 720, 1154);
        AutoUtils.auto(binding.toolbar.getRoot());
    }

    public void onClick(View view) {
        onBackPressed();
    }


    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    @Override
    public void onBackPressed() {
        if (!MainActivity.isInit) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            super.onBackPressed();
        }
    }
}