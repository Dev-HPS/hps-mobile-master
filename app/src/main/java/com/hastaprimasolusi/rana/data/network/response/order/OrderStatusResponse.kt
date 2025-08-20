package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class OrderStatusResponse(
    @SerializedName("DATA")
    var dATA: List<OrderStatusModel>? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)