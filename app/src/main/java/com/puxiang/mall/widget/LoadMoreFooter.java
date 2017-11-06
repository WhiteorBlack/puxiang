package com.puxiang.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.puxiang.mall.R;
import com.puxiang.mall.utils.Rotatable;


public class LoadMoreFooter extends android.support.v7.widget.AppCompatImageView {
    private Rotatable rotatable;

    public LoadMoreFooter(Context context) {
        super(context);
        initView();
    }


    public LoadMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        rotatable = new Rotatable.Builder(this).direction(Rotatable.ROTATE_Y).build();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.getVisibility() == VISIBLE) {
            startAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    public void startAnimation() {
        if (rotatable != null) {
            setVisibility(VISIBLE);
            rotatable.rotate(Rotatable.ROTATE_Y, 1000);
        }
    }

    public void stopAnimation() {
        if (rotatable != null) {
            rotatable.drop();
        }
        setVisibility(GONE);
    }
}
