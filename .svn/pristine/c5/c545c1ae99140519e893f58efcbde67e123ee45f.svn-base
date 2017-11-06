package com.puxiang.mall.widget;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.puxiang.mall.R;

public class CustomLoadMoreView extends LoadMoreView {

    private boolean isShowEndView = true;

    public CustomLoadMoreView(boolean isShowEndView) {
        this.isShowEndView = isShowEndView;

    }

    private int getLoadingIconId() {
        return R.id.pb;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.tv_loading;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.tv_load_fail;
    }

    @Override
    protected int getLoadEndViewId() {
        return isShowEndView ? R.id.tv_load_end : 0;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        super.convert(holder);
        visibleLoadingIcon(holder, getLoadMoreStatus() == 2);
    }

    private void visibleLoadingIcon(BaseViewHolder holder, boolean visible) {
        holder.setVisible(this.getLoadingIconId(), visible);
    }
}
