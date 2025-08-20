package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class HistoryOrderLpResponse(
    @SerializedName("DATA")
    var dATA: List<HistoryOrderLpModel>? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)