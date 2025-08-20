# Untuk Gson: menjaga model dan annotation SerializedName
-keep class com.hastaprimasolusi.rana.data.network.response.order.** { *; }

# Jaga semua anotasi (termasuk @SerializedName)
-keepattributes *Annotation*

# Jaga class dari Gson agar tidak dihapus
-keep class com.google.gson.** { *; }

# Jaga fields yang menggunakan @SerializedName
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ===== ML Kit =====
# Untuk mencegah class di-stripped jika dipakai lewat refleksi
-keepclassmembers class com.google.mlkit.** {
    *;
}

# Terkadang digunakan oleh pipeline internal
-keep class com.google.mlkit.vision.common.internal.** { *; }

# Tambahan Google Play Services (terkadang digunakan di runtime)
-keep class com.google.android.gms.vision.** { *; }
-dontwarn com.google.android.gms.vision.**

# Keep ML Kit initializer
-keep class * extends com.google.mlkit.common.sdkinternal.ModelResource

# Untuk semua model lokal (jika kamu pakai offline model / local model)
-keep class com.google.mlkit.common.model.LocalModel { *; }

# Tambahan untuk kemungkinan error di beberapa device Samsung, Xiaomi, dll.
-dontwarn com.google.mlkit.vision.face.internal.**
-dontwarn com.google.mlkit.vision.face.internal.client.**
-dontwarn com.google.mlkit.vision.face.internal.client.model.**
