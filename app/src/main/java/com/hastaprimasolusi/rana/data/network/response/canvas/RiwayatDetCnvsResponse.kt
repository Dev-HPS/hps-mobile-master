package com.hastaprimasolusi.rana.data.network.response.canvas


import com.google.gson.annotations.SerializedName

data class RiwayatDetCnvsResponse(
    @SerializedName("DATA")
    var dATA: RiwayatDetCnvsModel? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)