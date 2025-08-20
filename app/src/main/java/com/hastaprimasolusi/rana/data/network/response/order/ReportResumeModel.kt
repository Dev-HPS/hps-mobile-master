package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class ReportResumeModel(
    @SerializedName("TRX_COUNT")
    var tRXCOUNT: String? = null,
    @SerializedName("TRX_TOTAL_ADMIN")
    var tRXTOTALADMIN: String? = null,
    @SerializedName("TRX_TOTAL_AMOUNT")
    var tRXTOTALAMOUNT: String? = null,
    @SerializedName("TRX_TOTAL_ITEMS")
    var tRXTOTALITEMS: String? = null,
    @SerializedName("TRX_TOTAL_TOTAL")
    var tRXTOTALTOTAL: String? = null
)