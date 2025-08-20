package com.hastaprimasolusi.rana.data.network.response.payment


import com.google.gson.annotations.SerializedName

data class PaymentStatusResponse(
    @SerializedName("DATA")
    var dATA: List<PaymentStatusModel>? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)