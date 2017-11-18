package com.puxiang.mall.module.my.adapter;

import android.view.View;

import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxPostAddress;
import com.puxiang.mall.utils.ToastUtil;

/**
 * Created by zhaoyong bai on 2017/11/14.
 */

public class PostAddressAdapter extends EasyBindQuickAdapter<RxPostAddress> {
    public PostAddressAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxPostAddress item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
        holder.addOnClickListener(R.id.tv_edit)
                .addOnClickListener(R.id.tv_del);
    }

}
