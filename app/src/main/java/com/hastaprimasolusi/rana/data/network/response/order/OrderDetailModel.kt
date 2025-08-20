package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class OrderDetailModel(
    @SerializedName("ORDER_CURRENCY")
    var oRDERCURRENCY: String? = null,
    @SerializedName("ORDER_DATE")
    var oRDERDATE: String? = null,
    @SerializedName("ORDER_EXPIRED")
    var oRDEREXPIRED: String? = null,
    @SerializedName("ORDER_FROM")
    var oRDERFROM: OrderFromModel? = null,
    @SerializedName("ORDER_HISTORIES")
    var oRDERHISTORIES: List<OrderHistoriesModel>? = null,
    @SerializedName("ORDER_ID")
    var oRDERID: String? = null,
    @SerializedName("ORDER_METHOD_PAY")
    var oRDERMETHODPAY: String? = null,
    @SerializedName("ORDER_METHOD_PAY_ADMIN")
    var oRDERMETHODPAYADMIN: String? = null,
    @SerializedName("ORDER_METHOD_PAY_NAME")
    var oRDERMETHODPAYNAME: String? = null,
    @SerializedName("ORDER_METHOD_PAY_STATUS")
    var oRDERMETHODPAYSTATUS: String? = null,
    @SerializedName("ORDER_NO")
    var oRDERNO: String? = null,
    @SerializedName("ORDER_PROD_DETAIL")
    var oRDERPRODDETAIL: List<OrderProdModel>? = null,
    @SerializedName("ORDER_STATUS")
    var oRDERSTATUS: String? = null,
    @SerializedName("ORDER_STATUS_COLOR")
    var oRDERSTATUSCOLOR: String? = null,
    @SerializedName("ORDER_STATUS_PAY")
    var oRDERSTATUSPAY: String? = null,
    @SerializedName("ORDER_STATUS_PAY_TEXT")
    var oRDERSTATUSPAYTEXT: String? = null,
    @SerializedName("ORDER_STATUS_TEXT")
    var oRDERSTATUSTEXT: String? = null,
    @SerializedName("ORDER_TEXT")
    var oRDERTEXT: String? = null,
    @SerializedName("ORDER_TOTAL_AMT")
    var oRDERTOTALAMT: String? = null,
    @SerializedName("ORDER_TOTAL_PAY_AMT")
    var oRDERTOTALPAYAMT: String? = null,
    @SerializedName("ORDER_TOTAL_PAY_QTY")
    var oRDERTOTALPAYQTY: String? = null,
    @SerializedName("ORDER_TOTAL_QTY")
    var oRDERTOTALQTY: String? = null,
    @SerializedName("ORDER_TYPE")
    var oRDERTYPE: String? = null,
    @SerializedName("ORDER_TYPE_TEXT")
    var oRDERTYPETEXT: String? = null,
    @SerializedName("ORDER_ONGKIR")
    var oRDERONGKIR: String? = null,
    @SerializedName("ORDER_AMOUNT")
    var oRDERAMOUNT: String? = null,
    @SerializedName("TRX_PAYMENT")
    var tRXPAYMENT: String? = null
)