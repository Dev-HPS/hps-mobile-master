package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class ReportTransModel(
    @SerializedName("TRX_ADMIN")
    var tRXADMIN: String? = null,
    @SerializedName("TRX_AMOUNT")
    var tRXAMOUNT: String? = null,
    @SerializedName("TRX_CODE")
    var tRXCODE: String? = null,
    @SerializedName("TRX_CURRENCY")
    var tRXCURRENCY: String? = null,
    @SerializedName("TRX_DATE")
    var tRXDATE: String? = null,
    @SerializedName("TRX_ID")
    var tRXID: String? = null,
    @SerializedName("TRX_ITEMS")
    var tRXITEMS: String? = null,
    @SerializedName("TRX_STATUS")
    var tRXSTATUS: String? = null,
    @SerializedName("TRX_TOTAL")
    var tRXTOTAL: String? = null,
    @SerializedName("TRX_EC")
    var tRXEC: String? = null,
    @SerializedName("TRX_OUTLET")
    var tRXOUTLET: String? = null,
    @SerializedName("TRX_TYPE_TEXT")
    var tRXTYPETEXT: String? = null
)