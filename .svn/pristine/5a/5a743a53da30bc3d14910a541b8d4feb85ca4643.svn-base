package com.puxiang.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import in.srain.cube.views.ptr.PtrFrameLayout;

public class WebPtrFrameLayout extends PtrFrameLayout {


    public WebPtrFrameLayout(Context context) {
        super(context);
    }

    public WebPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean mIsDisallowIntercept = false;
    private boolean mIsWebFirstMode = true;

    private boolean isFirst = true;

    public WebTouchEvent touchEvent;

    public void setTouchEvent(WebTouchEvent touchEvent) {
        this.touchEvent = touchEvent;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            isFirst = true;
            mIsDisallowIntercept = false;
            return super.dispatchTouchEvent(e);
        }
        if (!touchEvent.isFirst()) {
            return dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        this.mIsDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public interface WebTouchEvent {
        boolean isFirst();
    }
}
