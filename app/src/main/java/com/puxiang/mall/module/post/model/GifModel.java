package com.puxiang.mall.module.post.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.puxiang.mall.BR;

public class GifModel extends BaseObservable {
    private String imgUrl;
    private String gifUrl;
    private boolean isGif;

    public GifModel(String gifUrl) {
        this.gifUrl = gifUrl;
        this.imgUrl = gifUrl.replaceFirst(".gif", ".jpg");
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
        this.imgUrl = gifUrl.replaceFirst(".gif", ".jpg");
    }

    @Bindable
    public boolean isGif() {
        return isGif;
    }

    public void setGif(boolean gif) {
        isGif = gif;
        notifyPropertyChanged(BR.gif);
    }
}
