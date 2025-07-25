# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class com.shakilpatel.bytebuddy.data.models.** { *; }
-keep class com.shakilpatel.bytebuddy.common.MyApplication
-keep class com.shakilpatel.bytebuddy.data.notification.** { *; }
-keep class org.spongycastle.** { *; }
-dontwarn org.spongycastle.**

-keep class com.itextpdf.text.** { *; }
-dontwarn com.itextpdf.text.**

-keep class android.graphics.Bitmap.** {*;}
-keep class com.itextpdf.** { *;}
-keep class android.graphics.** {*;}


-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class javax.annotation.** { *; }
-keep class dagger.hilt.** { *; }
-keep class com.rajat.pdfviewer.** { *;}

-keep class com.google.firebase.** { *; }
-keepattributes Signature


# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile