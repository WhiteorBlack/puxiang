package com.puxiang.mall.module.search.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxProduct;

public class SearchListAdapter extends EasyBindQuickAdapter<RxProduct> {

    public SearchListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxProduct item) {
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(BR.item, item);
        binding.executePendingBindings();
        holder.getBinding().getRoot().setTag(item);
        holder.addOnClickListener(R.id.tv_get_in);
        ((TextView)holder.getBinding().getRoot().findViewById(R.id.tv_item_price)).getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG| Paint.ANTI_ALIAS_FLAG);
    }

}
