package com.puxiang.mall.module.search.viewmodel;

import android.databinding.ObservableField;

import com.puxiang.mall.mvvm.base.ViewModel;

public class SearchListViewModel implements ViewModel {

    public static final int TYPE_SEARCH_PRODUCT = 1;
    public static final int TYPE_SEARCH_BBS = 2;

    public ObservableField<String> keyword = new ObservableField<>();

    public SearchListViewModel(String kw) {
        this.keyword.set(kw);
    }

    @Override
    public void destroy() {

    }
}
