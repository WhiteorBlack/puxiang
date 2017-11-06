package com.puxiang.mall.module.mall.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.puxiang.mall.BR;
import com.puxiang.mall.R;
import com.puxiang.mall.config.Config;
import com.puxiang.mall.model.data.MallData;
import com.puxiang.mall.model.data.RxProduct;
import com.puxiang.mall.utils.DraweeViewUtils;
import com.puxiang.mall.utils.WebUtil;

import java.util.ArrayList;
import java.util.List;

public class BottomSectionAdapter extends BaseSectionQuickAdapter<MallData, BottomSectionAdapter.MovieViewHolder>
        implements View.OnClickListener {
    private int sdvWidth = DraweeViewUtils.IMG_SIZE_PRODUCT, sdvHeight = DraweeViewUtils
            .IMG_SIZE_PRODUCT;

    public BottomSectionAdapter(int layoutResId, int sectionHeadResId, List<MallData> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    List<MallData> mallList = new ArrayList<>();
    private int floowerCount = 0;

    /**
     * 设置数据推荐列表
     */
    public void setData(List<RxProduct> beanList) {
        if (beanList == null) return;
        if (floowerCount == 0) {
            mallList.clear();
        }
        MallData mallData = new MallData(true, "");
        mallData.isTitle = true;
        mallData.floower = floowerCount;

        mallData.isClass = true;
        mallList.add(mallData);
        for (int j = 0; j < beanList.size(); j++) {
            MallData mall = new MallData(false);
            mall.setBen(beanList.get(j));
            mall.isMore = true;
            mallList.add(mall);
        }
//        mallData.isBottom = true;
        floowerCount++;
        if (floowerCount > Config.RECOMMENDBOTTOM) {

            mallList.get(mallList.size()-1).isBottom=true;
            floowerCount=0;
        }
        setNewData(mallList);
    }


    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding);
//        AutoUtils.auto(view);
        return view;
    }

    @Override
    protected void convert(MovieViewHolder helper, MallData item) {
        helper.getBinding().setVariable(BR.mallItem, item);
        helper.getBinding().executePendingBindings();
        helper.getBinding().getRoot().setTag(item);
    }

//    @Override
//    protected BindingViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
//        ViewDataBinding binding = viewType == SECTION_HEADER_VIEW ? DataBindingUtil.inflate
//                (mLayoutInflater, mSectionHeadResId, parent, false) : DataBindingUtil.inflate
//                (mLayoutInflater, mLayoutResId, parent, false);
//        return new BindingViewHolder<>(binding);
//    }

    @Override
    protected void convertHead(MovieViewHolder helper, MallData item) {
        ViewDataBinding binding = helper.getBinding();
        helper.getBinding().getRoot().setTag(item);
        binding.setVariable(BR.mallItem, item);
        binding.executePendingBindings();
        if (item.isTitle) {
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            String id = (String) v.getTag();
            Log.e(TAG, "onClick: " + id);
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
