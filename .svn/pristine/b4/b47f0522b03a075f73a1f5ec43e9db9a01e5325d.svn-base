package com.puxiang.mall.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.puxiang.mall.MyApplication;
import com.puxiang.mall.R;
import com.puxiang.mall.utils.DraweeViewUtils;


public class SimpleFragment extends Fragment {

    private String url;
    private SimpleDraweeView draweeView;

    public static SimpleFragment newInstance(String url) {
        SimpleFragment fragment = new SimpleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (draweeView == null) {
            draweeView = ((SimpleDraweeView) LayoutInflater.from(container.getContext()).inflate(R.layout.viewpager_img, container, false));
            Log.e("haha", "SimpleFragment: ");
            if (url != null) {
                DraweeViewUtils.getInstance().loadImg(draweeView, url, MyApplication.widthPixels / 2, MyApplication.heightPixels / 5);
            }
        }
        return draweeView;
    }
}
