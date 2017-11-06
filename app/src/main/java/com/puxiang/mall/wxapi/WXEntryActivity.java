package com.puxiang.mall.wxapi;

import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import org.greenrobot.eventbus.EventBus;

public class WXEntryActivity extends WXCallbackActivity {

    @Override
    public void onResp(BaseResp resp) {
        int type = resp.getType();
        Log.e("SendAuth", "type : " + type);
        if (type == 1) {
            //  int errorCode = resp.errCode;
            EventBus.getDefault().post(resp);
//            switch (errorCode) {
//                case BaseResp.ErrCode.ERR_OK:
//                    //用户同意
//                    //String code = ((SendAuth.Resp) resp).code;
//                    EventBus.getDefault().post(resp);
//                    break;
//                case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                    //用户拒绝
//                    EventBus.getDefault().post(resp);
//                    break;
//                case BaseResp.ErrCode.ERR_USER_CANCEL:
//                    //用户取消
//                    break;
//                default:
//                    break;
//            }
            finish();
        } else {
            super.onResp(resp);
        }
    }
}
