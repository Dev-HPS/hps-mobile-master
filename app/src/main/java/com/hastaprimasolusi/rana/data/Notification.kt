package com.hastaprimasolusi.rana.data


import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("body")
    var body: String? = null,
    @SerializedName("title")
    var title: String? = null
)