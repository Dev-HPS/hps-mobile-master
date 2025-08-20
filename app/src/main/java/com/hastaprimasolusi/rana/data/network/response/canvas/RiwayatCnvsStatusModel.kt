package com.hastaprimasolusi.rana.data.network.response.canvas


import com.google.gson.annotations.SerializedName

data class RiwayatCnvsStatusModel(
    @SerializedName("STATUS")
    var sTATUS: String? = null,
    @SerializedName("TANGGAL")
    var tANGGAL: String? = null
)