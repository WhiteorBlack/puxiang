package com.puxiang.mall.module.welcome.view;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.databinding.ActivityGuideBinding;
import com.puxiang.mall.module.welcome.viewmodel.GuideViewModel;
import com.puxiang.mall.utils.ActivityUtil;

/**
 * Created by zhaoyong bai on 2017/9/30.
 */

public class GuideActivity extends BaseBindActivity {
    private ActivityGuideBinding binding;
    private GuideViewModel viewModel;
    private int[] res = new int[]{R.mipmap.guide_one, R.mipmap.guide_two};
    private GuideAdapter adapter;
    private View[] views;
    private View[] points;

    @Override
    protected void initBind() {
        viewModel = new GuideViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guide);
        binding.setViewModel(viewModel);
        mImmersionBar.transparentNavigationBar().navigationBarWithKitkatEnable(true);
    }

    @Override
    public void initView() {
        binding.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.startMainActivity(GuideActivity.this);
                finish();
            }
        });
        views = new View[res.length];
        points = new View[res.length];
        adapter = new GuideAdapter();
        binding.viewpager.setAdapter(adapter);

        binding.viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == res.length - 1) {
                    viewModel.setIsButton(true);
                } else {
                    viewModel.setIsButton(false);
                }
                for (int i = 0; i < res.length; i++) {
                    points[i].setEnabled(i == position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        for (int i = 0; i < res.length; i++) {
            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
            params.leftMargin = 20;
            params.rightMargin = 20;
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.point_bg);
            view.setEnabled(i == 0);
            points[i] = view;
            binding.llPointContent.addView(view);
        }
    }


    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) {
            viewModel.destroy();
        }
    }

    private class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return res.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views[position]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (views[position] == null) {
                views[position] = getView();
            }
            ((SimpleDraweeView) views[position]).setImageURI(Uri.parse("res:///" + res[position]));
            container.addView(views[position]);

//            if (position == 1) {
//                if (views[position] == null) {
//                    views[position] = animView();
//                }
//                container.addView(views[position]);
//            }

            return views[position];
        }

        private View animView() {
            View view = LayoutInflater.from(GuideActivity.this).inflate(R.layout.view_guide_two, null, false);
            ImageView animView = view.findViewById(R.id.iv_logo);
            TranslateAnimation animation = new TranslateAnimation(0, -MyApplication.widthPixels - 200, 0, 0);
            animation.setDuration(8 * 1000);
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatCount(-1);
            animView.setAnimation(animation);
            animation.startNow();
            AnimationDrawable animationDrawable = (AnimationDrawable) animView.getDrawable();
            animationDrawable.start();
            return view;
        }

        private SimpleDraweeView getView() {
            SimpleDraweeView simView = new SimpleDraweeView(GuideActivity.this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            simView.setLayoutParams(params);
            simView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return simView;
        }
    }
}
