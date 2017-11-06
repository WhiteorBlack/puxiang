package com.puxiang.mall.module.emotion.viewmodel.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.puxiang.mall.R;
import com.puxiang.mall.module.emotion.utils.EmotionUtils;

import java.util.List;

public class EmotionViewsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private final int emotionType;
    private final int itemWidth;

    public EmotionViewsAdapter(List<String> data, int emotionType, int itemWidth) {
        super(data);
        this.emotionType = emotionType;
        this.itemWidth = itemWidth;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        ImageView iv_emotion = new ImageView(parent.getContext());
        iv_emotion.setId(R.id.iv);
        // 设置内边距
        iv_emotion.setPadding(itemWidth / 8, itemWidth / 6, itemWidth / 8, itemWidth / 6);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, itemWidth);
        iv_emotion.setLayoutParams(params);
        return new BaseViewHolder(iv_emotion);
    }

    @Override
    protected void convert(BaseViewHolder helper, String emotionName) {
        helper.setImageResource(R.id.iv, EmotionUtils.getImgByName(emotionType, emotionName));
    }
}
