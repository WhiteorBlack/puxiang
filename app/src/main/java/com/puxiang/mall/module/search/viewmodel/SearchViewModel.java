package com.puxiang.mall.module.search.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.puxiang.mall.BaseBindActivity;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.config.CacheKey;
import com.puxiang.mall.module.search.view.SearchListActivity;
import com.puxiang.mall.mvvm.base.ViewModel;
import com.puxiang.mall.network.NetworkSubscriber;
import com.puxiang.mall.network.retrofit.ApiWrapper;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.umeng.analytics.MobclickAgent;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class SearchViewModel extends BaseObservable implements ViewModel {


    private final BaseBindActivity activity;
    public ObservableArrayList<String> hotList = new ObservableArrayList<>();
    public ObservableArrayList<String> hisList = new ObservableArrayList<>();
    public ObservableBoolean isEmpty = new ObservableBoolean(true);
    public ObservableField<String> keyword = new ObservableField<>("");

    public SearchViewModel(BaseBindActivity activity) {
        this.activity = activity;
        EventBus.getDefault().register(this);
        getIntentWords();
        getCacheData();
        getHotKeywords();
    }


    /**
     * 获取首页传的搜索热词
     */
    private void getIntentWords() {
        String keyWords = activity.getIntent().getStringExtra("keyword");
        if (!TextUtils.isEmpty(keyWords)) {
            keyword.set(keyWords);
        }
    }

    /**
     * 事件订阅
     *
     * @param key
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String key) {
        if (!hisList.contains(key)) {
            hisList.add(key);
        }
    }

    /**
     * 获取缓存热词
     */
    private void getCacheData() {
        MyApplication.mCache.getAsListBean(CacheKey.HIS_LIST, String[].class, his -> {
            if (!his.isEmpty()) {
                hisList.addAll(his);
            }
        });
        MyApplication.mCache.getAsListBean(CacheKey.HOT_KEYWORDS, String[].class, hot -> {
            if (!hot.isEmpty()) {
                hotList.addAll(hot);
            }
        });
    }

    /**
     * 清除搜索记录
     */
    public void deleteSearchCache() {
        ToastUtil.toast("清除成功");
        hisList.clear();
        MyApplication.mCache.remove(CacheKey.HIS_LIST);
    }

    /**
     * 获取搜索热词
     */
    private void getHotKeywords() {
        ApiWrapper.getInstance()
                .getHotKeywords()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<String>>() {
                    @Override
                    public void onSuccess(List<String> bean) {
                        setHotKeywords(bean);
                    }
                });
    }

    /**
     * 设置搜索热词
     *
     * @param list
     */
    private void setHotKeywords(List<String> list) {
        if (list.isEmpty()) return;
        MyApplication.mCache.put(CacheKey.HOT_KEYWORDS, list);
        hotList.clear();
        hotList.addAll(list);
    }

    @BindingAdapter("hisData")
    public static void setHisList(TagFlowLayout tfl, List<String> list) {
        tfl.getAdapter().notifyDataChanged();
    }

    @BindingAdapter("hotData")
    public static void setHotList(TagFlowLayout tfl, List<String> list) {
        if (list.isEmpty()) return;
        tfl.getAdapter().notifyDataChanged();
    }

    /**
     * 开始搜索
     */
    public void startSearch(String kw) {
        boolean empty = StringUtil.isEmpty(kw);
        if (!empty) {
            if (!hisList.contains(kw)) {
                hisList.add(kw);
                MyApplication.mCache.put(CacheKey.HIS_LIST, ((List) hisList));
            }
//            keyword.set("");
        }
        Intent intent = new Intent(activity, SearchListActivity.class);
        MobclickAgent.onEvent(activity, "keyword--->" + kw);
        intent.putExtra("keyword", kw);
        activity.startActivity(intent);
    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

}
