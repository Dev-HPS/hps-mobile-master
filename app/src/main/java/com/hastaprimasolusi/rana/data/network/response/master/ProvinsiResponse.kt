package com.hastaprimasolusi.rana.data.network.response.master


import com.google.gson.annotations.SerializedName

data class ProvinsiResponse(
    @SerializedName("DATA")
    var dATA: List<ProvinsiModel>? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)