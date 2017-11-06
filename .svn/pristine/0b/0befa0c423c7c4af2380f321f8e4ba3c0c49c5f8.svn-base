package com.puxiang.mall.network;//package com.somic.mall.network;
//
//import android.util.Log;
//
//import com.somic.mall.MyApplication;
//import com.somic.mall.config.Config;
//import com.somic.mall.model.data.HttpResult;
//import com.somic.mall.model.data.RxUploadUrl;
//import com.somic.mall.network.retrofit.ApiWrapper;
//import com.somic.mall.utils.luban.Luban;
//
//import org.reactivestreams.Subscriber;
//
//import java.io.File;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.schedulers.Schedulers;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.RequestBody;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * Created by ChenHengQuan on 16/3/9.
// * Email nullpointerchan@163.com
// */
//public class HttpMethods {
//
//
//    private static final int DEFAULT_TIMEOUT = 15;
//
//    private Retrofit retrofit;
//    private ApiService apiService;
//    private String TAG = "HttpMethods";
//
//    //构造方法私有
//    private HttpMethods() {
//        //手动创建一个OkHttpClient并设置超时时间
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//
//        retrofit = new Retrofit.Builder()
//                .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .baseUrl(Config.SERVER_HOST)
//                .build();
//
//        apiService = retrofit.create(ApiService.class);
//    }
//
//    //在访问HttpMethods时创建单例
//    private static class SingletonHolder {
//        private static final HttpMethods INSTANCE = new HttpMethods();
//    }
//
//    //获取单例
//    public static HttpMethods getInstance() {
//        return SingletonHolder.INSTANCE;
//    }
//
//    private static int MB = 1048576;
//
//
////    public void getShareUrl(Subscriber<String> subscriber, String url) {
////        Observable observable = apiService.getShareUrl(url)
////                .map(new HttpResultFunc<String>());
////        toSubscribe(observable, subscriber);
////    }
////
////    public void uploadDeviceInfo(Subscriber<String> subscriber, String type, String version, String machineId) {
////        Observable observable = apiService.uploadDeviceInfo(type, version, machineId)
////                .map(new HttpResultFunc<String>());
////        toSubscribe(observable, subscriber);
////    }
//
////    public void setUpload(final Subscriber<List<RxUploadUrl>> subscriber, String path, Map<String, String> picMap) {
////        if (picMap.get(path) != null) {
////            subscriber.onCompleted();
////            return;
////        }
////        final File file = new File(path);
////        Luban.get(MyApplication.getContext())
////                .load(file)
////                .putGear(Luban.THIRD_GEAR)
////                .asObservable()
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .doOnError(throwable -> throwable.printStackTrace())
////                .onErrorResumeNext(throwable -> {
////                    return Observable.empty();
////                })
////                .subscribe(bytes -> {
////                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
////                    String fileName = file.getName().replaceFirst("gif$", "png");
////                    MultipartBody.Part body =
////                            MultipartBody.Part.createFormData("file", fileName, requestFile);
////                    Observable observable = apiService.upload(body)
////                            .map(new HttpResultFunc<>());
////                    toSubscribe(observable, subscriber);
////                });
////    }
//
//
//
//    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
//        o.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(s);
//    }
//
//    /**
//     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
//     *
//     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
//     */
////    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
////
////        @Override
////        public T call(HttpResult<T> httpResult) {
////            Log.e("222", "call: " + httpResult.getErrorMessage());
////            return httpResult.getReturnObject();
////        }
////    }
//
//}
