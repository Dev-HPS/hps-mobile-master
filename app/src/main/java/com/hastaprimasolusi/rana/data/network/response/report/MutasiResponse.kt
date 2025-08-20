package com.hastaprimasolusi.rana.data.network.response.report


import com.google.gson.annotations.SerializedName

data class MutasiResponse(
    @SerializedName("DATA")
    var dATA: MutasiModel? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)