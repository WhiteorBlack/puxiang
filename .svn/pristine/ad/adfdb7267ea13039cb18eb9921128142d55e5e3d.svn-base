package com.puxiang.mall.widget.dialog;

import android.content.Context;
import android.graphics.Color;

import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideLeftExit;
import com.flyco.dialog.widget.NormalDialog;
import com.puxiang.mall.R;
import com.puxiang.mall.utils.AppUtil;

public class DefaultDialog extends NormalDialog {
    private boolean isConfirm;

    public DefaultDialog(Context context, String dialogTitle, OnDialogExecuteListener listener) {
        super(context);

        int color = AppUtil.getColor(R.color.indicator_color);
        int textColor = Color.parseColor("#333333");
        this.titleTextColor(textColor)
                .content(dialogTitle)
                .style(NormalDialog.STYLE_TWO)
                .titleTextSize(18)
                .heightScale(0.3f)
                .widthScale(0.7f)
                .cornerRadius(10)
                .contentTextColor(textColor)
                .contentTextSize(16f)
                .btnTextColor(color, color)
                .btnTextSize(16f)
                .showAnim(new BounceLeftEnter())
                .dismissAnim(new SlideLeftExit())
                .btnTextColor(color, color)
                .setOnBtnClickL(this::dismiss, () -> {
                    isConfirm = true;
                    this.dismiss();

                });
        this.setOnDismissListener(dialog1 -> {
            if (isConfirm) {
                isConfirm = false;
                listener.execute();
            } else {
                listener.cancel();
            }
        });
    }
}
