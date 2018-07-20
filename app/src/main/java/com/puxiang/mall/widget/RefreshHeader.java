package com.puxiang.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.Rotatable;
import com.puxiang.mall.utils.ScreenUtil;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class RefreshHeader extends FrameLayout implements PtrUIHandler {
    //  private ImageView imageView;
    private int imgWidth;
    private int screenWidth;
    private TextView textView;
    private int textViewWidth;
    private LinearLayout ll;
    private int llWidth;
    private boolean stateBar = true;
    private ImageView lv;
    private Rotatable rotatable;

    public RefreshHeader(Context context) {
        super(context);
        initView();
    }

    public RefreshHeader(Context context, boolean stateBar) {
        super(context);
        this.stateBar = stateBar;
        initView();
    }

    public RefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    private void initView() {
        int layout_refresh_header;
        if (stateBar) {
            layout_refresh_header = R.layout.view_refresh_header;
        } else {
            layout_refresh_header = R.layout.view_refresh_header_no_state_bar;
        }
        View view = LayoutInflater.from(getContext()).inflate(layout_refresh_header, this);
        AutoUtils.auto(view);
        lv = findViewById(R.id.lv);
        ll = (LinearLayout) findViewById(R.id.ll_loading);
        textView = (TextView) findViewById(R.id.tv_loading);
        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        //  imageView.measure(w, h);
        lv.measure(w, h);
        textView.measure(w, h);
        ll.measure(w, h);
        //  imgWidth = imageView.getMeasuredWidth();
        imgWidth = lv.getMeasuredWidth();
        textViewWidth = textView.getMeasuredWidth();
        llWidth = ll.getMeasuredWidth();
        screenWidth = ScreenUtil.getWidthAndHeight().widthPixels;

    }

    private void moveCar(int position) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ll.getLayoutParams();
        lp.setMargins(position, 0, 0, 0);
        ll.requestLayout();

    }

    public void onUIReset(PtrFrameLayout frame) {
        moveCar(-1 * llWidth);
    }

    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        textView.setText("放手吧我要刷新了");
        lv.setVisibility(VISIBLE);
    }

    public void onUIRefreshBegin(PtrFrameLayout frame) {
        int carPosition = (screenWidth - llWidth) / 2;
        moveCar(carPosition);
        if (lv.getVisibility() != View.VISIBLE) {
            lv.setVisibility(VISIBLE);
        }
        textView.setText("加载中...");
        if (rotatable == null) {
            rotatable = new Rotatable.Builder(lv).direction(Rotatable.ROTATE_Y).build();
            rotatable.rotate(Rotatable.ROTATE_Y, 1000);
        }
    }

    public void onUIRefreshComplete(PtrFrameLayout frame) {
        textView.setText("更新完成");
//        rotatable.drop();
//        lv.setVisibility(GONE);
    }

    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int offsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        MyApplication.isRefreshing.set(currentPos > 100);
        if (currentPos <= offsetToRefresh) {
            if (status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                int carPosition = currentPos * (screenWidth + llWidth) / 2 / offsetToRefresh - llWidth;
                moveCar(carPosition);
            } else if (currentPos < frame.getOffsetToKeepHeaderWhileLoading() && !isUnderTouch && status ==
                    PtrFrameLayout.PTR_STATUS_COMPLETE) {
                int carPosition = screenWidth - currentPos * (screenWidth + llWidth) / 2 / offsetToRefresh;
                moveCar(carPosition);
            }
        }
    }
}
