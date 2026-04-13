# Stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Kotlin serialization (DTOs + generated serializers)
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep @kotlinx.serialization.Serializable class com.example.rickandmortyapplication.data.remote.dto.** { *; }
-keepclassmembers class com.example.rickandmortyapplication.data.remote.dto.** {
    *** Companion;
}

# Navigation type-safe routes
-keep @kotlinx.serialization.Serializable class com.example.rickandmortyapplication.ui.navigation.** { *; }

# Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-keep,allowobfuscation,allowshrinking class retrofit2.** { *; }
-keep,allowobfuscation,allowshrinking class okhttp3.** { *; }
-keep,allowobfuscation,allowshrinking class okio.** { *; }

# Coil
-dontwarn coil.**
