package com.puxiang.mall.module.userinfo.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityVersionExplainBinding;
import com.puxiang.mall.utils.StringUtil;

public class VersionExplainActivity extends BaseBindActivity {

    private ActivityVersionExplainBinding binding;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_version_explain);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("版本介绍");
        String introduce = getIntent().getStringExtra("introduce");
        if (StringUtil.isEmpty(introduce)) {
            binding.setExplain("暂无版本介绍");
        } else {
            binding.setExplain(introduce);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {

    }
}
