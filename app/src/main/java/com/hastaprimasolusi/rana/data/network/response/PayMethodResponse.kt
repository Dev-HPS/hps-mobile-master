package com.hastaprimasolusi.rana.data.network.response


import com.google.gson.annotations.SerializedName

data class PayMethodResponse(
    @SerializedName("DATA")
    var dATA: List<PayMethodModel>? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)