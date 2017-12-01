package com.puxiang.mall.module.plate.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.model.data.RxPostInfo;
import com.puxiang.mall.module.bbs.viewmodel.BbsRequest;

public class PlatePostAdapter extends EasyBindQuickAdapter<RxPostInfo> {
    public PlatePostAdapter(int layoutResId, BbsRequest.RefreshListener listener) {
        super(layoutResId, listener);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxPostInfo item) {
        holder.addOnClickListener(R.id.tv_user_name)
                .addOnClickListener(R.id.sdv_bbs_pic)
                .addOnClickListener(R.id.iv_like_icon);
        if (item.getPost().getPicUrls() != null && item.getPost().getPicUrls().length == 1) {
            String url = item.getPost().getPicOnly().get();
            if (url.contains("?")) {
                try {
                    String[] size = url.substring(url.indexOf("?") + 1).split("x");
                    int wide = Integer.parseInt(size[0]);
                    int height = Integer.parseInt(size[1]);
                    float wh = wide * 1.0f / height;
                    ((SimpleDraweeView) holder.getView(R.id.show_item_one)).setAspectRatio(wh);
                } catch (Exception e) {

                }
            }
        }
        ViewDataBinding binding = holder.getBinding();
        binding.setVariable(BR.item, item);
        binding.executePendingBindings();
        binding.getRoot().setTag(item);
    }

}
