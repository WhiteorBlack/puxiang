package com.puxiang.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author xiety on 2017/4/28.
 */
public class WarpLinearLayout extends ViewGroup {

    public WarpLinearLayout(Context context) {
        super(context);
    }

    public WarpLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WarpLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxHeight = getWrapHeight(widthMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), maxHeight);
    }

    private int getWrapHeight(int widthMeasureSpec) {
        WrapLayoutParams params; //边距
        int row = 1; //行号
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int occupancyWidth = 0; //所占宽度
        int occupancyHeight = 0; //所占高度

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            params = (WrapLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            occupancyWidth += childWidth;
            if (occupancyWidth > maxWidth) {
                row++;
                occupancyWidth = childWidth;
            }
            occupancyHeight = childHeight * row;
        }
        return occupancyHeight;
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        WrapLayoutParams params;
        int row = 0;
        int occupancyWidthPosition = left;
        int occupancyHeightPosition = top;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            params = (WrapLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = child.getMeasuredHeight() + params.bottomMargin + params.topMargin;
            if (occupancyWidthPosition + childWidth > right) {
                row++;
                occupancyWidthPosition = left;
            }
            occupancyHeightPosition = row * childHeight;
            /*
             * layout(left,top,right,bottom)
             * left = 左坐标 = 目前占用的 +leftmargin
             * top (距离parent top距离) = 目前占用的top坐标(随行数增加)
             * right = 左坐标+child width
             * bottom = 目前占用top坐标 + child height
              * */
            child.layout(occupancyWidthPosition + params.leftMargin, occupancyHeightPosition,
                    occupancyWidthPosition + childWidth, occupancyHeightPosition + childHeight);
            occupancyWidthPosition += childWidth;
        }

    }
    /*
    * 计算margin
    * */

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new WrapLayoutParams(getContext(),attrs);
    }

    public class WrapLayoutParams extends MarginLayoutParams {

        public WrapLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
    }
}
