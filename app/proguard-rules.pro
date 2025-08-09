-keep class com.tumble.kronoxtoapp.domain.models.network.** { *; }
-keep class com.tumble.kronoxtoapp.domain.models.** { *; }
-keep class com.tumble.kronoxtoapp.presentation.viewmodels.** { *; }

-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

-keep class com.squareup.moshi.** { *; }

