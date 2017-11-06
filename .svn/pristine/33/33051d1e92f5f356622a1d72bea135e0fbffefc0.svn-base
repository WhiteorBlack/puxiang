package com.puxiang.mall.widget.verticaltablayout.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puxiang.mall.R;
import com.puxiang.mall.utils.AutoUtils;

public class QTabView extends TabView {
    private Context mContext;
    private ImageView mIcon;
    private TextView mTitle;
    private TextView mBadge;
    private int mMinHeight;
    private TabIcon mTabIcon;
    private TabTitle mTabTitle;
    private boolean mChecked;
    private LinearLayout mContainer;
    private GradientDrawable gd;

    public QTabView(Context context) {
        super(context);
        mContext = context;
        gd = new GradientDrawable();
        gd.setColor(0xFFE84E40);
        mMinHeight = dp2px(30);
        mTabIcon = new TabIcon.Builder().build();
        mTabTitle = new TabTitle.Builder(context).build();
        initView();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, new int[]{android.R.attr.state_checked});
        }
        return drawableState;
    }

    private void initView() {
        initContainer();
        initIconView();
        initTitleView();
        initBadge();
        addView(mContainer);
        addView(mBadge);
    }

    private void initContainer() {
        mContainer = new LinearLayout(mContext);
        mContainer.setOrientation(LinearLayout.HORIZONTAL);
        mContainer.setMinimumHeight(mMinHeight);
        mContainer.setPadding(0, 0, 0, 0);
        mContainer.setGravity(Gravity.CENTER);
    }

    private void initBadge() {
        mBadge = new TextView(mContext);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params2.gravity = Gravity.RIGHT | Gravity.TOP;
        params2.setMargins(0, dp2px(5), dp2px(5), 0);
        mBadge.setLayoutParams(params2);
        mBadge.setGravity(Gravity.CENTER);
        mBadge.setTextColor(0xFFFFFFFF);
        mBadge.setTextSize(9);
        setBadge(0);
    }

    private void initTitleView() {
        if (mTitle != null) mContainer.removeView(mTitle);
        mTitle = new TextView(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        mTitle.setLayoutParams(params);
        mTitle.setTextColor(mTabTitle.mColorNormal);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTitle.mTitleTextSize);
        mTitle.setText(mTabTitle.mContent);
        mTitle.setGravity(Gravity.CENTER);
        mTitle.setSingleLine();
        mTitle.setEllipsize(TextUtils.TruncateAt.END);
        mTitle.setPadding(mTabTitle.mLeft, mTabTitle.mTop, mTabTitle.mRight, mTabTitle.mBottom);
        AutoUtils.auto(mTitle);
        requestContainerLayout(mTabIcon.mIconGravity);
    }

    private void initIconView() {
        if (mIcon != null) mContainer.removeView(mIcon);
        mIcon = new ImageView(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mTabIcon.mIconWidth, mTabIcon.mIconHeight);
        mIcon.setLayoutParams(params);
        if (mTabIcon.mNormalIcon != 0) {
            mIcon.setImageResource(mTabIcon.mNormalIcon);
        } else {
            mIcon.setVisibility(View.GONE);
        }
        requestContainerLayout(mTabIcon.mIconGravity);
    }

    private void setBadgeImp(int num) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mBadge.getLayoutParams();
        if (num <= 9) {
            lp.width = dp2px(12);
            lp.height = dp2px(12);
            gd.setShape(GradientDrawable.OVAL);
            mBadge.setPadding(0, 0, 0, 0);
        } else {
            lp.width = FrameLayout.LayoutParams.WRAP_CONTENT;
            lp.height = FrameLayout.LayoutParams.WRAP_CONTENT;
            mBadge.setPadding(dp2px(3), 0, dp2px(3), 0);
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setCornerRadius(dp2px(6));
        }
        mBadge.setLayoutParams(lp);
        mBadge.setBackgroundDrawable(gd);
        mBadge.setText(String.valueOf(num));
        mBadge.setVisibility(View.VISIBLE);
    }

    @Override
    public QTabView setBadge(int num) {
        if (num > 0) {
            setBadgeImp(num);
        } else {
            mBadge.setText("");
            mBadge.setVisibility(View.GONE);
        }
        return this;
    }

    public QTabView setIcon(TabIcon icon) {
        if (icon != null)
            mTabIcon = icon;
        initIconView();
        setChecked(mChecked);
        return this;
    }

    public QTabView setTitle(TabTitle title) {
        if (title != null)
            mTabTitle = title;
        initTitleView();
        setChecked(mChecked);
        return this;
    }

    public QTabView setBackground(int resId) {
        super.setBackgroundResource(resId);
        return this;
    }

    private void requestContainerLayout(int gravity) {
        mContainer.removeAllViews();
        switch (gravity) {
            case Gravity.LEFT:
                mContainer.setOrientation(LinearLayout.HORIZONTAL);
                if (mIcon != null) {
                    mContainer.addView(mIcon);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIcon.getLayoutParams();
                    lp.setMargins(0, 0, mTabIcon.mMargin, 0);
                    mIcon.setLayoutParams(lp);
                }
                if (mTitle != null)
                    mContainer.addView(mTitle);
                break;
            case Gravity.TOP:
                mContainer.setOrientation(LinearLayout.VERTICAL);
                if (mIcon != null) {
                    mContainer.addView(mIcon);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIcon.getLayoutParams();
                    lp.setMargins(0, 0, 0, mTabIcon.mMargin);
                    mIcon.setLayoutParams(lp);
                }
                if (mTitle != null)
                    mContainer.addView(mTitle);
                break;
            case Gravity.RIGHT:
                mContainer.setOrientation(LinearLayout.HORIZONTAL);
                if (mTitle != null)
                    mContainer.addView(mTitle);
                if (mIcon != null) {
                    mContainer.addView(mIcon);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIcon.getLayoutParams();
                    lp.setMargins(mTabIcon.mMargin, 0, 0, 0);
                    mIcon.setLayoutParams(lp);
                }

                break;
            case Gravity.BOTTOM:
                mContainer.setOrientation(LinearLayout.VERTICAL);
                if (mTitle != null)
                    mContainer.addView(mTitle);
                if (mIcon != null) {
                    mContainer.addView(mIcon);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mIcon.getLayoutParams();
                    lp.setMargins(0, mTabIcon.mMargin, 0, 0);
                    mIcon.setLayoutParams(lp);
                }
                break;
        }
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        refreshDrawableState();
        if (mChecked) {
            mTitle.setTextColor(mTabTitle.mColorSelected);
            mTitle.setBackgroundColor(mTabTitle.mBackgroundSelected);
            if (mTabIcon.mSelectedIcon != 0) {
                mIcon.setVisibility(View.VISIBLE);
                mIcon.setImageResource(mTabIcon.mSelectedIcon);
            } else {
                mIcon.setVisibility(View.GONE);
            }
        } else {
            mTitle.setTextColor(mTabTitle.mColorNormal);
            mTitle.setBackgroundColor(mTabTitle.mBackgroundNormal);
            if (mTabIcon.mNormalIcon != 0) {
                mIcon.setVisibility(View.VISIBLE);
                mIcon.setImageResource(mTabIcon.mNormalIcon);
            } else {
                mIcon.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public static class TabIcon {
        public int mSelectedIcon;
        public int mNormalIcon;
        public int mIconGravity;
        public int mIconWidth;
        public int mIconHeight;
        public int mMargin;

        private TabIcon(int mSelectedIcon, int mNormalIcon, int mIconGravity, int mIconWidth, int mIconHeight, int mMargin) {
            this.mSelectedIcon = mSelectedIcon;
            this.mNormalIcon = mNormalIcon;
            this.mIconGravity = mIconGravity;
            this.mIconWidth = mIconWidth;
            this.mIconHeight = mIconHeight;
            this.mMargin = mMargin;
        }

        public static class Builder {
            private int mSelectedIcon;
            private int mNormalIcon;
            private int mIconGravity;
            private int mIconWidth;
            private int mIconHeight;
            public int mMargin;

            public Builder() {
                mSelectedIcon = 0;
                mNormalIcon = 0;
                mIconWidth = FrameLayout.LayoutParams.WRAP_CONTENT;
                mIconHeight = FrameLayout.LayoutParams.WRAP_CONTENT;
                mIconGravity = Gravity.LEFT;
                mMargin = 0;
            }

            public Builder setIcon(int selectIconResId, int normalIconResId) {
                mSelectedIcon = selectIconResId;
                mNormalIcon = normalIconResId;
                return this;
            }

            public Builder setIconSize(int width, int height) {
                mIconWidth = width;
                mIconHeight = height;
                return this;
            }

            public Builder setIconGravity(int gravity) {
                if (gravity != Gravity.LEFT && gravity != Gravity.RIGHT
                        & gravity != Gravity.TOP & gravity != Gravity.BOTTOM) {
                    throw new IllegalStateException("iconGravity only support Gravity.LEFT " +
                            "or Gravity.RIGHT or Gravity.TOP or Gravity.BOTTOM");
                }
                mIconGravity = gravity;
                return this;
            }

            public Builder setIconMargin(int margin) {
                mMargin = margin;
                return this;
            }

            public TabIcon build() {
                return new TabIcon(mSelectedIcon, mNormalIcon, mIconGravity, mIconWidth, mIconHeight, mMargin);
            }
        }
    }

    public static class TabTitle {
        public int mColorSelected;
        public int mColorNormal;
        public int mTitleTextSize;
        public String mContent;
        public int mLeft;
        public int mTop;
        public int mRight;
        public int mBottom;
        public int mBackgroundSelected;
        public int mBackgroundNormal;

        private TabTitle(int mColorSelected, int mColorNormal, int mTitleTextSize, String mContent
                , int mLeft, int mTop, int mRight, int mBottom, int mBackgroundSelected, int mBackgroundNormal) {
            this.mColorSelected = mColorSelected;
            this.mColorNormal = mColorNormal;
            this.mTitleTextSize = mTitleTextSize;
            this.mContent = mContent;
            this.mLeft = mLeft;
            this.mTop = mTop;
            this.mRight = mRight;
            this.mBottom = mBottom;
            this.mBackgroundSelected = mBackgroundSelected;
            this.mBackgroundNormal = mBackgroundNormal;
        }

        public static class Builder {
            private int mColorSelected;
            private int mColorNormal;
            private int mTitleTextSize;
            private String mContent;
            private int mLeft;
            private int mTop;
            private int mRight;
            private int mBottom;
            private int mBackgroundSelected;
            private int mBackgroundNormal;

            public Builder(Context context) {
                this.mColorSelected = context.getResources().getColor(R.color.colorAccent);
                this.mColorNormal = 0xFF757575;
                this.mTitleTextSize = 16;
                this.mContent = "title";
                this.mLeft = 0;
                this.mTop = 0;
                this.mRight = 0;
                this.mBottom = 0;
                this.mBackgroundSelected = 0xFFFFFFFF;
                this.mBackgroundNormal = 0x00000000;
            }

            public Builder setTextColor(int colorSelected, int colorNormal) {
                mColorSelected = colorSelected;
                mColorNormal = colorNormal;
                return this;
            }

            public Builder setTextBackground(int colorSelected, int colorNormal) {
                mBackgroundSelected = colorSelected;
                mBackgroundNormal = colorNormal;
                return this;
            }

            public Builder setTextSize(int sizePx) {
                mTitleTextSize = sizePx;
                return this;
            }

            public Builder setContent(String content) {
                mContent = content;
                return this;
            }

            public Builder setPadding(int left, int top, int right, int bottom) {
                mLeft = left;
                mTop = top;
                mRight = right;
                mBottom = bottom;
                return this;
            }

            public TabTitle build() {
                return new TabTitle(mColorSelected, mColorNormal, mTitleTextSize, mContent
                        , mLeft, mTop, mRight, mBottom, mBackgroundSelected, mBackgroundNormal);
            }
        }
    }
}