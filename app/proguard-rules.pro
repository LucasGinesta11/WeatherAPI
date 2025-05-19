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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Retrofit
-keep class retrofit2.** {*;}
-dontwarn retrofit2.**

# Gson
-keep class com.google.gson.** {*;}
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn com.google.gson.**

# ViewModel
-keep class androidx.lifecycle.** { *; }
-keep class com.example.tmdb.viewModel.** { *; }

# Jetpack Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Evitar que Compose borre funciones
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Model
-keep class com.lucas.weatherapi.data.model.** { *; }
-keep class com.lucas.weatherapi.data.model.CityResponse{
<fields>;
}
-keep class com.lucas.weatherapi.data.model.CurrentResponse{
<fields>;
}
-keep class com.lucas.weatherapi.data.model.ForecastResponse{
<fields>;
}

# Reglas generica
-dontwarn org.jetbrains.annotations.**
-dontwarn javax.annotation.**
