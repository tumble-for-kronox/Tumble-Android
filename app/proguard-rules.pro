# Keep your data models
-keep class com.tumble.kronoxtoapp.domain.models.network.** { *; }

# Keep classes used with JSON serialization
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# If using Gson
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# If using Jackson
-keep class com.fasterxml.jackson.** { *; }

# If using Moshi
-keep class com.squareup.moshi.** { *; }

