package com.puxiang.mall.module.pay.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Event;
import com.puxiang.mall.databinding.ActivityPayResultBinding;
import com.puxiang.mall.module.my.adapter.CollectionAdapter;
import com.puxiang.mall.module.my.viewmodel.CollectionViewModel;
import com.puxiang.mall.module.pay.viewmodel.PayResultViewModel;
import com.puxiang.mall.utils.ActivityUtil;

import org.greenrobot.eventbus.EventBus;

public class PayResultActivity extends BaseBindActivity {

    private ActivityPayResultBinding binding;
    private PayResultViewModel viewModel;
    private CollectionViewModel collectionViewModel;
    private CollectionAdapter adapter;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pay_result);
        viewModel = new PayResultViewModel(this);
        collectionViewModel = new CollectionViewModel(this, adapter);
        binding.setViewModel(viewModel);

    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("支付结果");
        binding.toolbar.setTextColor(R.color.text_black);
        binding.toolbar.setColor(R.color.white);
        binding.toolbar.setBackSrc(R.mipmap.nav_back_g);
        EventBus.getDefault().post(Event.FINISH);
        EventBus.getDefault().post(Event.RELOAD_WEB);
        initRv(binding.rvCollect);
        collectionViewModel.getCollectProducts(4, 1);
        setBarHeight(binding.toolbar.ivBar);
    }

    private void initRv(RecyclerView rvCollect) {
        adapter = new CollectionAdapter(R.layout.item_collection);
        rvCollect.setLayoutManager(new GridLayoutManager(this, 2));
        rvCollect.setNestedScrollingEnabled(false);
        rvCollect.setAdapter(adapter);
        rvCollect.addOnItemTouchListener(collectionViewModel.onItemTouchListener());
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_pay_result:
                viewModel.depart();
                break;
            case R.id.btn_goHome_:
                EventBus.getDefault().post(Event.GO_HOME);
                ActivityUtil.startMainActivity(this);
                finish();
                break;
            case R.id.btn_more:
//                WebUtil.jumpMyWeb(URLs.HTML_MY_COLLECT, this);
//                ActivityUtil.startCollectionActivity(this);
                break;
        }
    }
}
