# This file is intentionally left mostly empty for ProGuard configuration
# Android Gradle plugin handles most optimizations automatically

# Keep all public API of Android stack
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.Fragment
-keep public class * extends androidx.fragment.app.Fragment

# Keep Kotlin Serialization
-keepclassmembers class * {
    *** Companion;
}

-keepclasseswithmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep Jetpack Compose
-keep class androidx.compose.** { *; }
