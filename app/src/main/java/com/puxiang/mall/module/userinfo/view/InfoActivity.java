package com.puxiang.mall.module.userinfo.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.databinding.ActivityUserInfoBinding;
import com.puxiang.mall.model.data.RxMyUserInfo;
import com.puxiang.mall.module.login.view.LoginFragment;
import com.puxiang.mall.module.userinfo.viewmodel.InfoViewModel;
import com.puxiang.mall.network.URLs;
import com.puxiang.mall.utils.ActivityUtil;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.WebUtil;

import org.greenrobot.eventbus.EventBus;
public class InfoActivity extends BaseBindActivity {


    private ActivityUserInfoBinding binding;
    private InfoViewModel viewModel;

    @Override
    protected void initBind() {
        EventBus.getDefault().post(Event.KILL_INFO);
        EventBus.getDefault().post(Event.KILL_WEB);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info);
        viewModel = new InfoViewModel(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("个人信息");
    }

    public void onClick(View view) {
        if (StringUtil.isEmpty(MyApplication.TOKEN)) {
            startActivity(new Intent(this, LoginFragment.class));
            return;
        }
        RxMyUserInfo userInfo = viewModel.userBean.get();
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_info_head:
                ActivityUtil.startShowHeadPicActivity(this, userInfo.getUserImage(), userInfo.getUserId());
                break;
            case R.id.ll_info_nick:
                if (viewModel.check.get() != null) {
                    if (viewModel.check.get().getChangeAble() == 1) {
                        ActivityUtil.startNickActivityForResult(this, userInfo.getNickname(), 0);
                    }
                }
                break;
            case R.id.ll_info_realName:
                ActivityUtil.startNameActivityForResult(this, userInfo.getRealName(), 0);
                break;
            case R.id.ll_info_mobile:
                WebUtil.jumpWeb(URLs.getHtmlModifyMobile(userInfo.getMobile()), InfoActivity.this);
                break;
            case R.id.ll_info_sex:
                ActivityUtil.startSexActivityForResult(this, userInfo.getSex(), 0);
                break;
            case R.id.my_info_birthday:
                viewModel.selectBirthday();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            viewModel.getInfo();
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }
}
