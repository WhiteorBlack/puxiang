package com.puxiang.mall.module.from.adapter;

import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.module.bbs.viewmodel.BbsRequest;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class FromAdapter extends EasyBindQuickAdapter<RxPostInfo> {

    public FromAdapter(int layoutResId, BbsRequest.RefreshListener listener) {
        super(layoutResId, listener);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxPostInfo item) {
        holder.addOnClickListener(R.id.tv_user_name)
                .addOnClickListener(R.id.sdv_user_pic)
                .addOnClickListener(R.id.iv_from_icon)
                .addOnClickListener(R.id.tv_from);
    }
}
