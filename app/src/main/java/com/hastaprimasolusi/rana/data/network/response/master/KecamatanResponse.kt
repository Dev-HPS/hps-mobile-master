package com.hastaprimasolusi.rana.data.network.response.master


import com.google.gson.annotations.SerializedName

data class KecamatanResponse(
    @SerializedName("DATA")
    var dATA: List<KecamatanModel>? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)