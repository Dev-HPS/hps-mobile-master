package com.hastaprimasolusi.rana.data.network.response


import com.google.gson.annotations.SerializedName

data class DeliveryCourierResponse(
    @SerializedName("DATA")
    var dATA: List<DeliveryCourierModel>? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)