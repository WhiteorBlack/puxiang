package com.puxiang.mall.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by 123 on 2017/9/5.
 */

public class MyRecylcerView extends RecyclerView {
    public MyRecylcerView(Context context) {
        super(context);
    }

    public MyRecylcerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecylcerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }
}
