package com.puxiang.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import cn.bingoogolapple.bgabanner.BGABanner;

public class MyBanner extends BGABanner {
    private ViewGroup mView;

    public void setParentView(ViewGroup view) {
        this.mView = view;
    }
    public MyBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (this.mView != null && e.getAction() != MotionEvent.ACTION_UP) {
            this.mView.requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (this.mView != null && e.getAction() != MotionEvent.ACTION_UP) {
            this.mView.requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (this.mView != null && e.getAction() != MotionEvent.ACTION_UP) {
            this.mView.requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(e);
    }
}
