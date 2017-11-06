# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk-for-studio/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-printmapping mapping.txt

-dontwarn java.lang.invoke.*
-dontwarn android.net.**
-keep class android.net.SSLCertificateSocketFactory{*;}
-dontwarn org.apache.**
-keep class org.apache.** { *;}
-dontwarn com.facebook.**
-keep class com.facebook.** { *;}

#解决内部类警告
-keepattributes InnerClasses
-dontoptimize
-optimizations optimization_filter

# Tinker
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

# linju
-dontwarn com.lingju.**
-keep class com.lingju.**{
    <fields>;
    <methods>;
}
-dontwarn com.hp.hpl.**
-keep class com.hp.hpl.**{
    <fields>;
    <methods>;
}
-dontwarn net.sourceforge.pinyin4j.**
-keep class net.sourceforge.pinyin4j.**{
    <fields>;
    <methods>;
}
-dontwarn org.joda.**
-keep class org.joda.**{
    <fields>;
    <methods>;
}


#-libraryjars libs/libammsdk.jar
#-libraries libammsdk.jar
#
#-keep class com.alipay.android.app.IAlixPay{*;}
#-keep class com.alipay.android.app.IAlixPay$Stub{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
#-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
#-keep class com.alipay.sdk.app.PayTask{ public *;}
#-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.** {*; }
-keep class com.tencent.** {*;}
#-libraryjars libs/SocialSDK_WeiXin_1.jar
#-libraryjars libs/SocialSDK_WeiXin_2.jar
-keep class com.umeng.** {*;}
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*; }
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*; }

#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------

-keep class com.somic.mall.model.data.** { *; }

#-------------------------------------------------------------------------
#---------------------------------2.第三方包-------------------------------
#eventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# unionpay 银联
-keep class org.simalliance.openmobileapi.** {*;}
-keep class org.simalliance.openmobileapi.service.** {*;}

-keep class com.unionpay.** {*;}
-dontwarn com.unionpay.**

#PersistentCookieJar
-dontwarn com.franmontiel.persistentcookiejar.**
-keep class com.franmontiel.persistentcookiejar.**

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#galleryfinal
-keep class cn.finalteam.galleryfinal.widget.*{*;}
-keep class cn.finalteam.galleryfinal.widget.crop.*{*;}
-keep class cn.finalteam.galleryfinal.widget.zoonview.*{*;}

#okhttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

#jiecaoMediaPlayer
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**

-keep class com.lippi.recorder.iirfilterdesigner.** {*; }
-keep class org.apache.** {*; }
-keep class com.facebook.** {*; }
-keep class com.baoyz.actionsheet.** {*; }
-keep class com.zhy.** {*; }
-keep class com.shizhefei.** {*; }
-keep class com.somic.mall.utils.AutoUtils{*; }
-keep class in.srain.cube.** {*; }
-keep class cn.bingoogolapple.** {*; }
-keep class com.nineoldandroids.** {*; }
-keep class org.lzh.nonview.** {*; }
-keep class com.flyco.dialog.** {*; }
-dontwarn top.zibin.**
-keep class top.zibin.** {*; }
-dontwarn com.sunfusheng.**
-keep class com.sunfusheng.** {*; }
-dontwarn io.reactivex.**
-keep class io.reactivex.** {*; }

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 科大讯飞 语音
-keep class com.iflytek.**{*;}

# For retrolambda
-dontwarn java.lang.invoke.*

# 融云
-dontwarn io.rong.**
-keep class io.rong.** {*;}
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keep class *.R$ { *;}
-keep class com.somic.mall.module.im.IMNotificationReceiver {*;}
#融云
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
# public *;
#}
#-keepattributes Exceptions,InnerClasses
#
#-keepattributes Signature
#
#-keepattributes *Annotation*
#
#-keep class com.google.gson.examples.android.model.** { *; }
#
#-keep class **$Properties
#-dontwarn org.eclipse.jdt.annotation.**
#
##-libraryjars libs/agora-rtc-sdk.jar
##-keep class io.agora.rtc.** {*;}
#
#-keep class io.rong.** {*;}
#-keep class io.agora.rtc.** {*; }
#-keep class * implements io.rong.imlib.model.MessageContent{*;}
#
#-dontwarn io.rong.push.** 
#-dontnote com.xiaomi.** 
#-dontnote com.huawei.android.pushagent.** 
#-dontnote com.google.android.gms.gcm.** 
#-dontnote io.rong.**
# -ignorewarnings


#alibaba
-keepclasseswithmembernames class ** {
    native <methods>;
}
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-keep class com.ut.** {*;}
-dontwarn com.ut.**
-keep class com.ta.** {*;}
-dontwarn com.ta.**
-keep class anet.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**

#umeng
 -dontshrink
 -dontoptimize
 -dontwarn com.google.android.maps.**
 -dontwarn android.webkit.WebView
 -dontwarn com.umeng.**
 -dontwarn com.tencent.weibo.sdk.**
 -dontwarn com.facebook.**
 -keep public class javax.**
 -keep public class android.webkit.**
 -dontwarn android.support.v4.**
 -keep enum com.facebook.**
 -keepattributes Exceptions,InnerClasses,Signature
 -keepattributes *Annotation*
 -keepattributes SourceFile,LineNumberTable

 -keep public interface com.facebook.**
 -keep public interface com.tencent.**
 -keep public interface com.umeng.socialize.**
 -keep public interface com.umeng.socialize.sensor.**
 -keep public interface com.umeng.scrshot.**

 -keep public class com.umeng.socialize.* {*;}


 -keep class com.facebook.**
 -keep class com.facebook.** { *; }
 -keep class com.umeng.scrshot.**
 -keep public class com.tencent.** {*;}
 -keep class com.umeng.socialize.sensor.**
 -keep class com.umeng.socialize.handler.**
 -keep class com.umeng.socialize.handler.*
 -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
 -keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

 -keep class im.yixin.sdk.api.YXMessage {*;}
 -keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}

 -dontwarn twitter4j.**
 -keep class twitter4j.** { *; }

 -keep class com.tencent.** {*;}
 -dontwarn com.tencent.**
 -keep public class com.umeng.soexample.R$*{
     public static final int *;
 }
 -keep public class com.umeng.soexample.R$*{
     public static final int *;
 }
 -keep class com.tencent.open.TDialog$*
 -keep class com.tencent.open.TDialog$* {*;}
 -keep class com.tencent.open.PKDialog
 -keep class com.tencent.open.PKDialog {*;}
 -keep class com.tencent.open.PKDialog$*
 -keep class com.tencent.open.PKDialog$* {*;}

 -keep class com.sina.** {*;}
 -dontwarn com.sina.**
 -keep class  com.alipay.share.sdk.** {
    *;
 }
 -keepnames class * implements android.os.Parcelable {
     public static final ** CREATOR;
 }

 -keep class com.linkedin.** { *; }
 -keepattributes Signature


-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
#-keepattributes Signature-keepattributes Exceptions
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
-keep class com.somic.mall.model.data.**{*;}

#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------



#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------



#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------

#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-dontwarn
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
-keep class android.webkit.JavascriptInterface {*;}
#这个类 必须保留，这个类在WVJBWebViewClient中传递数据，如果被混淆 会导致一些callback无法调用
-keep class com.com.somic.mall.web.JavaScriptinterface$*
#类中成员的变量名也不能混淆，这些变量名被作为json中的字段，不能改变。
-keepclassmembers class com.somic.mall.web.JavaScriptinterface$*{
    <fields>;
}
-keepclassmembers class com.somic.mall.web.Activity$AppAndroid {
  public *;
}

-keep public class com.somic.mall.web.JavaScriptinterface {
    public *;
}

-keep public class com.somic.mall.web.WebActivity$JavaScriptinterface {
    public *;
}

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------