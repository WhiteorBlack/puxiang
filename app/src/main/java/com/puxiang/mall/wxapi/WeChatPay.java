package com.puxiang.mall.wxapi;

import android.content.Context;

import com.puxiang.mall.config.Config;
import com.puxiang.mall.model.data.RxWXPayInfo;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WeChatPay {

    private String prepayid;
    private String timestamp;
    private Context mContext;
    private IWXAPI msgApi;
    private String sign;
    private String appid;
    private String mch_id;
    private String nonceStr;

    PayReq req;


    /**
     * 向微信客户端请求。
     *
     * @param mContext  上下文
     */
    public WeChatPay(Context mContext, RxWXPayInfo bean) {

        this.prepayid = bean.getPrepayid();
        this.timestamp = bean.getTimestamp();
        this.mContext = mContext;
        this.sign = bean.getSign();
        this.appid = bean.getAppid();
        this.mch_id = bean.getPartnerid();
        this.nonceStr = bean.getNoncestr();
        msgApi = WXAPIFactory.createWXAPI(mContext, appid);
        msgApi.registerApp(Config.WX_APP_ID);

    }

    public void payWechat() {

        req = new PayReq();
        req.appId = appid;
        req.partnerId = mch_id;
        req.prepayId = prepayid;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = nonceStr;
        req.timeStamp = timestamp;
        // genPayReq();
        req.sign = sign;
        msgApi.sendReq(req);
    }

//    private void genPayReq() {
//        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//        signParams.add(new BasicNameValuePair("appid", req.appId));
//        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
//        signParams.add(new BasicNameValuePair("package", req.packageValue));
//        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));//商户ID
//        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));//预付单id
//        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));//时间
//        req.sign = genAppSign(signParams);
//    }
//
//    private String genAppSign(List<NameValuePair> params) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < params.size(); i++) {
//            sb.append(params.get(i).getName());
//            sb.append('=');
//            sb.append(params.get(i).getValue());
//            sb.append('&');
//        }
//        sb.append("key=");
//        sb.append("1234567890abcdefghijklmnganmeizhisom");
//        //this.sb.append("sign str\n" + sb.toString() + "\n\n");
//        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//        return appSign;
//    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String getNonce_str(String timestamp) {

        String nonce_str = MD5.getMessageDigest(timestamp.getBytes());
        return nonce_str;
    }

}
