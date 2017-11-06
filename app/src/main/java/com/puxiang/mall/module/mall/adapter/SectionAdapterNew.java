package com.puxiang.mall.module.mall.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.model.data.MallData;
import com.puxiang.mall.utils.AutoUtils;
import com.puxiang.mall.utils.DraweeViewUtils;
import com.puxiang.mall.utils.WebUtil;

import java.util.List;

public class SectionAdapterNew extends BaseSectionQuickAdapter<MallData, SectionAdapterNew.MovieViewHolder>
        implements View.OnClickListener {

    public SectionAdapterNew(int layoutResId, int sectionHeadResId, List<MallData> data) {
        super(layoutResId, sectionHeadResId, data);
    }


    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding);
        AutoUtils.auto(view);
        return view;
    }

    @Override
    protected void convert(MovieViewHolder helper, MallData item) {
        helper.getBinding().setVariable(BR.mallItem, item);
        helper.getBinding().executePendingBindings();
        helper.getBinding().getRoot().setTag(item);
    }


    @Override
    protected void convertHead(MovieViewHolder helper, MallData item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.mallItem, item);
        binding.executePendingBindings();
        helper.getBinding().getRoot().setTag(item);
        if (item.isTitle) {
            helper.setText(R.id.tv_desc, item.desc);
            switch (item.floower) {
                case 1:
                    helper.setBackgroundRes(R.id.iv_title, R.mipmap.new_goods);
                    helper.setBackgroundRes(R.id.fl_title, R.mipmap.title_bg_green);

                    break;
                case 2:
                    helper.setBackgroundRes(R.id.iv_title, R.mipmap.hot_goods);
                    helper.setBackgroundRes(R.id.fl_title, R.mipmap.title_red_l);
                    break;
                case 3:
                    helper.setBackgroundRes(R.id.iv_title, R.mipmap.white_win);
                    helper.setBackgroundRes(R.id.fl_title, R.mipmap.title_bg_blue);
                    break;
                case 4:
                    helper.setBackgroundRes(R.id.iv_title, R.mipmap.red_win);
                    helper.setBackgroundRes(R.id.fl_title, R.mipmap.title_bg_red_d);
                    break;
                case 5:
                    helper.setBackgroundRes(R.id.iv_title, R.mipmap.category_3);
                    helper.setBackgroundRes(R.id.fl_title, R.mipmap.title_bg_green);
                    break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            String id = (String) v.getTag();
            WebUtil.jumpGoodsWeb(mContext, id);
        }
    }

    public static class MovieViewHolder extends BaseViewHolder {

        public MovieViewHolder(View view) {
            super(view);
        }

        public ViewDataBinding getBinding() {
            return (ViewDataBinding) itemView.getTag(R.id.BaseQuickAdapter_databinding_support);
        }
    }
}
