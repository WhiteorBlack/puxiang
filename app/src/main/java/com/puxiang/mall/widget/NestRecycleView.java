package com.puxiang.mall.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by zhaoyong bai on 2017/10/20.
 */

public class NestRecycleView extends RecyclerView implements NestedScrollingChild {
    public NestRecycleView(Context context) {
        super(context);
    }

    public NestRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


}
