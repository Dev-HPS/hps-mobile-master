package com.hastaprimasolusi.rana.data.network.response.canvas


import com.google.gson.annotations.SerializedName

data class RiwayatCnvsProdModel(
    @SerializedName("IMAGE")
    var iMAGE: String? = null,
    @SerializedName("PRICE")
    var pRICE: String? = null,
    @SerializedName("QTY")
    var qTY: String? = null,
    @SerializedName("SUB_TOTAL_AMT")
    var sUBTOTALAMT: String? = null,
    @SerializedName("TITLE")
    var tITLE: String? = null,
    @SerializedName("UNIT")
    var uNIT: String? = null
)