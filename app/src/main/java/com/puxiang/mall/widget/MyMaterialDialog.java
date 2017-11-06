package com.puxiang.mall.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.flyco.dialog.widget.MaterialDialog;

public class MyMaterialDialog extends MaterialDialog {
    private final boolean isForced;

    public MyMaterialDialog(Context context, boolean isForced) {
        super(context);
        this.isForced = isForced;
        setCanceledOnTouchOutside(!isForced);
    }

    @Override
    public View onCreateView() {
        View view = super.onCreateView();
        mTvBtnLeft.setPadding(dp2px(15), dp2px(8), dp2px(15), dp2px(8));
        mTvBtnLeft.setGravity(Gravity.LEFT);
        mLeftBtnTextColor = Color.parseColor("#999999");
        mRightBtnTextColor = Color.parseColor("#DE000000");
        mMiddleBtnTextColor = Color.parseColor("#DE000000");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        mTvBtnLeft.setLayoutParams(lp);
        mLlBtns.setPadding(dp2px(10), dp2px(0), dp2px(10), dp2px(10));
        return view;
    }


    @Override
    public void onBackPressed() {
        if (isForced) {
            return;
        }
        super.onBackPressed();
    }
}
