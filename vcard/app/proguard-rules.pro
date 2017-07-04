# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android-sdk/tools/proguard/proguard-android.txt
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

# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
-dontpreverify

# If you want to enable optimization, you should include the
# following:
# -optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
# -optimizationpasses 5
# -allowaccessmodification
#
# Note that you cannot just include these flags in your own
# configuration file; if you are including this file, optimization
# will be turned off. You'll need to either edit this file, or
# duplicate the contents of this file and remove the include of this
# file from your project's proguard.config path property.

##---------------Begin: 保留第三方包,这可是基本礼仪  ----------
-dontwarn java.awt.event.**
-keep class java.awt.event.**
-dontwarn javax.swing.**
-keep class javax.swing.**
-dontwarn java.awt.**
-keep class java.awt.**{*;}
-keep class com.maya.android.extra.zxing.scan.**{*;}
-keep public class com.maya.android.extra.zxing.scan.R{*;}
-keep class cn.sharp.android.ncr.ocr.**{*;}
-dontwarn org.apache.commons.codec.**
-dontwarn org.apache.commons.httpclient.**
-keep class org.apache.commons.httpclient.**{*;}
-dontwarn com.tencent.qqvision.util.**
-keep class com.tencent.qqvision.util.**{*;}
##---------------End: 保留第三方包,这可是基本礼仪  ----------

##---------------Begin: 保留开源包原样,别人都开源,你还混淆个什么劲  ----------
-keep class com.google.gson.**{*;}
-libraryjars /libs/pinyin4j-2.5.0.jar
-libraryjars /libs/commons-httpclient-3.0.1.jar
-dontwarn demo.**
-dontwarn demo.Pinyin4jAppletDemo.**
-keep class demo.Pinyin4jAppletDemo.**{*;}
-keepclassmembers class demo.Pinyin4jAppletDemo{
	void start();
	void setSize(java.awt.Dimension);
	void setContentPane(java.awt.Container);
	void setName(java.lang.String);
}
-keep class demo.Pinyin4jAppletDemo$*{*;}
##---------------End: 保留开源包原样,别人都开源,你还混淆个什么劲  ----------

##---------------Begin: 已混淆的就不用再混了,混多了不太好,另外友盟还要特别处理下  ----------
#电话拦截黑名单
-dontwarn android.telephony.**
-keep class android.telephony.**{*;}
-dontwarn com.android.internal.telephony.**
-keep class com.android.internal.telephony.**{*;}

#百度分享
-dontwarn com.baidu.sharesdk.**
-keep class com.baidu.sharesdk.**{*;}

##找不到相关混淆资料，只能遇到时具体分析
-dontwarn com.baidu.sharesdk.weixin.**
-keep class com.baidu.sharesdk.weixin.**{*;}

#腾讯SDK
-dontwarn com.tencent.mm.sdk.openapi.**
-keep class com.tencent.mm.sdk.openapi.**{*;}
-dontwarn com.tencent.mm.sdk.**
-keep class com.tencent.mm.sdk.**{*;}
-keep class com.tencent.tauth.**{*;}
-keep class com.tencent.common.**{*;}
-keep class com.tencent.connect.a.**{*;}
-keep class com.tencent.connect.common.**{*;}
-keep class com.tencent.**{*;}
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

#微信SDK
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}

#新浪SDK
-dontwarn com.sina.sso.**
-keep class com.sina.sso.**{*;}
-dontwarn android.webkit.**
-keep class com.weibo.sdk.android.**{*;}
-keep class com.weibo.sdk.android.R{*;}

#友盟
-keep class com.umeng.fb.**{*;}
-keep class com.umeng.common.**{*;}
-keep class com.umeng.xp.common.**{*;}
-keep class com.mobclick.android.**{*;}
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep public class com.umeng.fb.ui.ThreadView {
}
-keep public class com.maya.android.vcard.R$*{
    public static final int *;
}
-keep class com.umeng.fb.push.**{*;}
-keep public class com.umeng.fb.push.*$*{*;}
-dontwarn com.umeng.**
-libraryjars libs/com.umeng.fb.5.3.0.jar
#-dontwarn    org.apache.log4j.*
#-keep class  org.apache.log4j.** { *;}
##---------------End: 已混淆的就不用再混了,混多了不太好,另外友盟还要特别处理下  ----------

##---------------Begin: 自身文件的特别保留  ----------
-keep class com.maya.android.vcard.service.**{*;}
-keep class com.maya.android.vcard.upload.**{*;}
-keep public class com.maya.android.vcard.entity.result.BindingCardEntity{
	public <fields>;
    public <methods>;
}
-keep class com.maya.android.vcard.activity.LoginActivity{
	*;
}
-keep class com.maya.android.vcard.activity.RegisterCloudValidateActivity$RegisterCloudValidateJS{
	public <methods>;
}
##---------------End: 自身文件的特别保留  ----------

##---------------Begin: 去除Log输出,除了e  ----------
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
##---------------End: 去除Log输出,除了e  ----------

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers class * extends android.app.Service {
   public void *(android.content.Intent);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

##---------------End: proguard configuration for Gson  ----------
