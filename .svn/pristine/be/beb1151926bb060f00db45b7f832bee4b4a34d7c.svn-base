package com.puxiang.mall.module.plate.view;

import android.content.Context;
import android.util.AttributeSet;

import com.puxiang.mall.widget.verticaltablayout.VTabLayout;

public class PlateVTabLayout extends VTabLayout {
    public PlateVTabLayout(Context context) {
        super(context);
    }

    public PlateVTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlateVTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTabSelected(int position) {
        if (position != getTabCount() - 1) {
            super.setTabSelected(position);
        } else {
            if (mListener != null) {
                mListener.onClick(position);
            }
        }
    }
}
