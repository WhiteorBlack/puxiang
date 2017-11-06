package com.puxiang.mall.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.flyco.dialog.widget.NormalDialog;
import com.puxiang.mall.R;

public class DialogUtil {

    /**
     * dialog 初始化
     *
     * @param dialogTitle
     */
    public void initDialog(String dialogTitle, Context context) {
        NormalDialog  dialog = new NormalDialog(context);

    }

    public static PopupWindow createTipsWindow(View view) {
        AutoUtils.auto(view);
        PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
//        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//                lp.alpha = 1f; //0.0-1.0
//                activity.getWindow().setAttributes(lp);
//            }
//        });

        return popWindow;
    }

    private static int i = 0;
    private static int res[] = {R.mipmap.help01, R.mipmap.help02, R.mipmap.help03, R.mipmap.help04};

    public static void tipsDialog(final ImageView imageView, View view) {


        imageView.setBackgroundResource(R.mipmap.help01);
        try {
            final PopupWindow tipsWindow = createTipsWindow(view);
            view.setOnClickListener(view1 -> {
                i++;
                if (i < res.length) {
                    imageView.setBackgroundResource(res[i]);
                } else {
                    i = 0;
                    tipsWindow.dismiss();
                }
            });
            tipsWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
