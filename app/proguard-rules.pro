# Smart Drawer ProGuard rules

# Keep your app's main classes
-keep class com.smartdrawer.app.** { *; }

# Keep data classes and models
-keep class com.smartdrawer.app.data.models.** { *; }
-keep class com.smartdrawer.app.domain.models.** { *; }

# Kotlin Coroutines
-dontwarn kotlinx.coroutines.**
-keep class kotlinx.coroutines.** { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Keep ViewBinding classes
-keep class * extends androidx.viewbinding.ViewBinding {
    <init>(...);
    public static *** inflate(...);
    public static *** bind(...);
}

# Keep Service classes
-keep class * extends android.app.Service { *; }

# Keep overlay related classes
-keep class android.view.WindowManager$LayoutParams { *; } 