package com.hastaprimasolusi.rana.data.network.response.report


import com.google.gson.annotations.SerializedName

data class MutasiInfoModel(
    @SerializedName("ACC_NAME")
    var aCCNAME: String? = null,
    @SerializedName("ACC_NUMBER")
    var aCCNUMBER: String? = null,
    @SerializedName("TOTAL_RECORD")
    var tOTALRECORD: String? = null
)