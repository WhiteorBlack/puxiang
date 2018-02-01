package com.puxiang.mall.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.flyco.dialog.widget.MaterialDialog;
import com.puxiang.mall.R;
import com.puxiang.mall.utils.AppUtil;

public class MyMaterialDialog extends MaterialDialog {
    private final boolean isForced;
    private Context context;

    public MyMaterialDialog(Context context, boolean isForced) {
        super(context);
        this.isForced = isForced;
        this.context = context;
        setCanceledOnTouchOutside(!isForced);
    }

    @Override
    public View onCreateView() {
//        View view = super.onCreateView();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null, false);
        LinearLayout titleContaner = view.findViewById(R.id.ll_title_contaner);
        titleContaner.addView(mTvTitle);

        LinearLayout llContent = view.findViewById(R.id.ll_content);
        mTvContent.setGravity(Gravity.CENTER);
        llContent.addView(mTvContent);

        LinearLayout llButton = view.findViewById(R.id.fl_btn_contenter);
        mTvBtnMiddle.setPadding(dp2px(10), dp2px(5), dp2px(10), dp2px(5));
        mTvBtnLeft.setPadding(dp2px(10), dp2px(5), dp2px(10), dp2px(5));
        llButton.addView(mTvBtnLeft, 0);

        LinearLayout llRight = view.findViewById(R.id.ll_right);
        llRight.addView(mTvBtnMiddle);

        return view;
    }

    @Override
    public void setUiBeforShow() {
        super.setUiBeforShow();
        LinearLayout.LayoutParams leftParams = (LinearLayout.LayoutParams) mTvBtnLeft.getLayoutParams();
        leftParams.setMargins(0, 0, dp2px(20), 0);
        mTvBtnLeft.setLayoutParams(leftParams);


        mTvBtnLeft.setBackground(AppUtil.getDrawable(R.drawable.btn_gohome_bg));
        mTvBtnLeft.setTextColor(AppUtil.getColor(R.color.white));
        mTvBtnMiddle.setBackground(AppUtil.getDrawable(R.drawable.btn_gobuy_bg));
        mTvBtnMiddle.setTextColor(AppUtil.getColor(R.color.white));
    }

    @Override
    public void onBackPressed() {
        if (isForced) {
            return;
        }
        super.onBackPressed();
    }
}
