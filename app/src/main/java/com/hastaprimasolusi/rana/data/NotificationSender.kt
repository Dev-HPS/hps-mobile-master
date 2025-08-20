package com.hastaprimasolusi.rana.data


import com.google.gson.annotations.SerializedName

data class NotificationSender(
    @SerializedName("data")
    var `data`: Data? = null,
    @SerializedName("notification")
    var notification: Notification? = null,
    @SerializedName("to")
    var to: String? = null
)