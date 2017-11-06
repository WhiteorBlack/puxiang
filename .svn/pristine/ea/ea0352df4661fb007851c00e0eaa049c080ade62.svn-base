package com.puxiang.mall.module.web.view;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.puxiang.mall.R;
import com.puxiang.mall.databinding.FragmentWebBinding;
import com.puxiang.mall.fragment.BaseBindFragment;
import com.puxiang.mall.module.web.viewmodel.WebFragmentViewModel;
import com.puxiang.mall.utils.StringUtil;
import com.puxiang.mall.utils.ToastUtil;
import com.puxiang.mall.utils.WebUtil;
import com.puxiang.mall.utils.permissions.EasyPermission;
import com.puxiang.mall.utils.permissions.PermissionCode;

import java.util.List;

public class WebFragment extends BaseBindFragment implements EasyPermission.PermissionCallback {

    private FragmentWebBinding binding;
    private WebFragmentViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web, container, false);
        return binding.getRoot();
    }

    @Override
    public void initView() {

        viewModel = new WebFragmentViewModel(this);
        WebUtil.initWebViewSettings(binding.web, viewModel.getWebViewClient());
        initRefresh(binding.ptrFrame);
        binding.ptrFrame.setTouchEvent(() -> isWebViewToFirst(binding.web));


        binding.llNone.layoutNone.setOnClickListener(v -> binding.ptrFrame.autoRefresh());
        binding.setViewModel(viewModel);
        checkPermission();
    }

    public boolean isWebViewToFirst(WebView webView) {
        return webView.getScrollY() == 0;
    }


    @Override
    public void refreshData() {
        viewModel.loadUrl.notifyChange();
    }

    private void checkPermission() {
        EasyPermission.with(this)
                .rationale(StringUtil.getString(R.string.rationale_gps))
                .addRequestCode(PermissionCode.RG_ACCESS_FINE_LOCATION)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .request();
    }


//    private boolean checkUrlValid() {
//        Log.e(TAG, "loadUrl: " + viewModel.loadUrl.get());
//        if (StringUtil.isEmpty(viewModel.loadUrl.get())) {
//            View noneLayout = binding.vsNone.getViewStub().inflate();
//            AutoUtils.auto(noneLayout);
//            viewModel.toolBarTitle.set("出错啦！");
//            ((TextView) noneLayout.findViewById(R.id.tv_none)).setText("网址错误！");
//            return false;
//        }
//        return true;
//    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) {
            viewModel.destroy();
        }
        Log.e(TAG, "viewModelDestroy: " + 11111);
        webViewRecycle();
    }

    public void webViewRecycle() {
        try {
            WebView webView = binding.web;
            if (webView != null) {
                webView.stopLoading();
                ViewGroup rootView = getRootView();
                if (rootView != null) {
                    rootView.removeView(webView);
                }
                webView.removeAllViews();
                webView.destroy();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPermissionGranted(int requestCode, List<String> perms) {
        if (requestCode == PermissionCode.RG_ACCESS_FINE_LOCATION) {
            if (viewModel != null) {
                viewModel.initData();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case PermissionCode.RG_ACCESS_FINE_LOCATION:
                ToastUtil.toast(StringUtil.getString(R.string.rationale_gps_and_audio));
                if (viewModel != null) {
                    viewModel.initData();
                }
                break;
            default:
                break;
        }
    }
}
