package com.hastaprimasolusi.rana.data.network.response.canvas


import com.google.gson.annotations.SerializedName

data class ListTokoResponse(
    @SerializedName("DATA")
    var dATA: List<ListTokoModel>? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)