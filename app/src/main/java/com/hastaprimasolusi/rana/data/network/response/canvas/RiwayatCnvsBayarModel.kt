package com.hastaprimasolusi.rana.data.network.response.canvas


import com.google.gson.annotations.SerializedName

data class RiwayatCnvsBayarModel(
    @SerializedName("CARA_BAYAR_LABEL")
    var cARABAYARLABEL: String? = null,
    @SerializedName("CARA_BAYAR_TEXT")
    var cARABAYARTEXT: String? = null,
    @SerializedName("KETERANGAN_TEXT")
    var kETERANGANTEXT: String? = null,
    @SerializedName("LABEL")
    var lABEL: String? = null,
    @SerializedName("STATUS_LABEL")
    var sTATUSLABEL: String? = null,
    @SerializedName("STATUS_TEXT")
    var sTATUSTEXT: String? = null,
    @SerializedName("TGL_BAYAR_2_TEXT")
    var tGLBAYAR2TEXT: String? = null,
    @SerializedName("TGL_BAYAR_LABEL")
    var tGLBAYARLABEL: String? = null,
    @SerializedName("TGL_BAYAR_TEXT")
    var tGLBAYARTEXT: String? = null
)