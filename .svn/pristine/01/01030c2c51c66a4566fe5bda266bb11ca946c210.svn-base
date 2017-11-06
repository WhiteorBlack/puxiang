package com.puxiang.mall.module.bbs.adapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.adapter.BindingViewHolder;
import com.puxiang.mall.adapter.EasyBindQuickAdapter;
import com.puxiang.mall.utils.DraweeViewUtils;

import cn.finalteam.galleryfinal.model.PhotoInfo;

public class PublishAdapter extends EasyBindQuickAdapter<PhotoInfo> {

    public PublishAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, PhotoInfo item) {
        holder.getBinding().setVariable(BR.item,item);
        holder.getBinding().executePendingBindings();
        ((SimpleDraweeView)holder.getView(R.id.sdv_item_pic)).setImageURI(DraweeViewUtils.getUriPath(item.getPhotoPath()));
        holder.addOnClickListener(R.id.publish_item_lose)
                .addOnClickListener(R.id.sdv_item_pic);
    }
}
