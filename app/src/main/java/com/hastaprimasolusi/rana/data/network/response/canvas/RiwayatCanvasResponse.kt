package com.hastaprimasolusi.rana.data.network.response.canvas


import com.google.gson.annotations.SerializedName
import com.hastaprimasolusi.rana.data.network.response.canvas.RiwayatCanvasModel

data class RiwayatCanvasResponse(
    @SerializedName("DATA")
    var dATA: List<RiwayatCanvasModel>? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)