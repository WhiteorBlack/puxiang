package com.puxiang.mall.module.goods.view;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.view.View;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityGoodsInfoBinding;
import com.puxiang.mall.module.goods.fragment.GoodsCommentFragment;
import com.puxiang.mall.module.goods.fragment.GoodsDetialFragment;
import com.puxiang.mall.module.goods.fragment.GoodsInfoFragment;
import com.puxiang.mall.module.shop.adapter.SimpleFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/3/23.
 */

public class GoodsInfoActivity extends BaseBindActivity {
    private ActivityGoodsInfoBinding binding;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goods_info);
    }

    @Override
    public void initView() {
        String[] titles = new String[]{"商品", "详情", "评论"};
        binding.tbGoods.setupWithViewPager(binding.viewpager);
        binding.viewpager.setAdapter(new SimpleFragmentAdapter(getSupportFragmentManager(), getFragments(), titles));
        binding.viewpager.setCurrentItem(0);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.cb_collect:

                break;

            case R.id.btn_menu:

                break;
        }
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new GoodsInfoFragment());
        fragments.add(new GoodsDetialFragment());
        fragments.add(new GoodsCommentFragment());
        return fragments;
    }

    @Override
    protected void viewModelDestroy() {

    }
}
