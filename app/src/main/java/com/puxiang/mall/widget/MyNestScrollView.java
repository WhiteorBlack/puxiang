package com.puxiang.mall.widget;

import android.content.Context;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

import com.orhanobut.logger.Logger;

/**
 * Created by zhaoyong bai on 2017/10/20.
 */

public class MyNestScrollView extends NestedScrollView {
    private int height=300;
    private NestedScrollingParentHelper nestedScrollingParentHelper;
    public MyNestScrollView(Context context) {
        super(context);
        nestedScrollingParentHelper=new NestedScrollingParentHelper(this);
    }

    public MyNestScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        nestedScrollingParentHelper=new NestedScrollingParentHelper(this);
    }

    public MyNestScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        nestedScrollingParentHelper=new NestedScrollingParentHelper(this);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
       Logger.e("onFinishInflate");
    }


}
