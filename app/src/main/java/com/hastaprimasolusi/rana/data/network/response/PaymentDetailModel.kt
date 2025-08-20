package com.hastaprimasolusi.rana.data.network.response


import com.google.gson.annotations.SerializedName

data class PaymentDetailModel(
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
    @SerializedName("TRX_PAYMENT_CHANNEL")
    var tRXPAYMENTCHANNEL: String? = null,
    @SerializedName("TRX_PAYMENT_EXPIRED")
    var tRXPAYMENTEXPIRED: String? = null,
    @SerializedName("TRX_PAYMENT_NAME")
    var tRXPAYMENTNAME: String? = null,
    @SerializedName("TRX_PAYMENT_NO")
    var tRXPAYMENTNO: String? = null,
    @SerializedName("TRX_PAYMENT_TOTAL")
    var tRXPAYMENTTOTAL: String? = null,
    @SerializedName("TRX_PAYMENT_VIA")
    var tRXPAYMENTVIA: String? = null,
    @SerializedName("TRX_PAY_METHOD")
    var tRXPAYMETHOD: String? = null,
    @SerializedName("TRX_PAY_METHOD_TEXT")
    var tRXPAYMETHODTEXT: String? = null,
    @SerializedName("TRX_PAY_STATUS")
    var tRXPAYSTATUS: String? = null,
    @SerializedName("TRX_PAY_STATUS_TEXT")
    var tRXPAYSTATUSTEXT: String? = null,
    @SerializedName("TRX_TYPE")
    var tRXTYPE: String? = null
)