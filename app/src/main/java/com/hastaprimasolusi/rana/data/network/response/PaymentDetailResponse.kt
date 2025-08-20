package com.hastaprimasolusi.rana.data.network.response


import com.google.gson.annotations.SerializedName
import com.hastaprimasolusi.rana.data.network.response.order.PaymentModel

data class PaymentDetailResponse(
    @SerializedName("DATA")
    var dATA: PaymentModel? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)