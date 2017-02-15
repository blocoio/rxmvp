# Models
-keep class com.example.rxmvp.data.** { *; }
-dontwarn com.example.rxmvp.data.**

# Butter Knife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# Crashlytics 2.+
-keep class com.crashlytics.** { *; }
-keep class com.crashlytics.android.**
-keepattributes SourceFile, LineNumberTable, *Annotation*
-keep public class * extends java.lang.Exception

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Sqlite
-keep class org.sqlite.** { *; }
-keep class org.sqlite.database.** { *; }
-keep class android.database.sqlite.** { *; }

# Retrolambda
-dontwarn java.lang.invoke.*

# Retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

# OkHTTP
-dontwarn rx.**

-dontwarn okio.**

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }

-keepattributes Signature
-keepattributes *Annotation*

# Dagger 2 (reflection constructors)
-keep public class * extends com.example.rxmvp.ui.common.lists.ItemView
-keepclassmembers class * extends com.example.rxmvp.ui.common.lists.ItemView {
 public <init>(android.content.Context);
}
