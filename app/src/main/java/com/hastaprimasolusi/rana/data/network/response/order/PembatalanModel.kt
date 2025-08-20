package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class PembatalanModel(
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
    @SerializedName("TRX_PAY_METHOD")
    var tRXPAYMETHOD: String? = null,
    @SerializedName("TRX_PAY_METHOD_TEXT")
    var tRXPAYMETHODTEXT: String? = null,
    @SerializedName("TRX_TYPE")
    var tRXTYPE: String? = null
)