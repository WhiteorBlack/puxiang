package com.puxiang.mall.module.search.view;

import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.TextView;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivitySearchBinding;
import com.puxiang.mall.module.search.viewmodel.SearchViewModel;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.MyTextUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

public class SearchActivity extends BaseBindActivity implements TagFlowLayout.OnTagClickListener, View.OnClickListener {

    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        viewModel = new SearchViewModel(this);
        mImmersionBar.keyboardEnable(false).init();
    }

    public void initView() {
        binding.toolbarLayout.et.setCursorVisible(false);
        binding.toolbarLayout.et.setOnClickListener(v -> binding.toolbarLayout.et.setCursorVisible(true));
        TagAdapter<String> hisAdapter = new TagAdapter<String>(viewModel.hisList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.view_search_history_tv, null);
                tv.setText(s);
                AutoUtils.autoTextSize(tv);
                return tv;
            }
        };
        binding.tflHistory.setOnTagClickListener(this);
        binding.tflHistory.setAdapter(hisAdapter);
        TagAdapter<String> hotAdapter = new TagAdapter<String>(viewModel.hotList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.view_search_history_tv, null);
                tv.setText(s);
                AutoUtils.autoTextSize(tv);
                return tv;
            }
        };
        binding.tflHot.setOnTagClickListener(this);
        binding.tflHot.setAdapter(hotAdapter);
        binding.setViewModel(viewModel);
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) {
            viewModel.destroy();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_search_btn:
              //  viewModel.keyword.set(MyTextUtils.getEditTextString(binding.toolbarLayout.et));
                viewModel.startSearch(MyTextUtils.getEditTextString(binding.toolbarLayout.et));
                break;
            case R.id.iv_del:
                viewModel.deleteSearchCache();
                break;
        }
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        String keyword = ((TextView) view).getText().toString();
        viewModel.startSearch(keyword);
        return true;
    }
}
