package com.hastaprimasolusi.rana.data.network.response.order


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hastaprimasolusi.rana.data.network.response.UserModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryOrderModel(
    @SerializedName("ORDER_CURRENCY")
    var oRDERCURRENCY: String? = null,
    @SerializedName("ORDER_DATE")
    var oRDERDATE: String? = null,
    @SerializedName("ORDER_EXPIRED")
    var oRDEREXPIRED: String? = null,
    @SerializedName("ORDER_ID")
    var oRDERID: String? = null,
    @SerializedName("ORDER_NO")
    var oRDERNO: String? = null,
    @SerializedName("ORDER_PROD_1_NAME")
    var oRDERPROD1NAME: String? = null,
    @SerializedName("ORDER_PROD_1_PIC_LARGE")
    var oRDERPROD1PICLARGE: String? = null,
    @SerializedName("ORDER_PROD_1_PIC_MEDIUM")
    var oRDERPROD1PICMEDIUM: String? = null,
    @SerializedName("ORDER_PROD_1_PIC_SMALL")
    var oRDERPROD1PICSMALL: String? = null,
    @SerializedName("ORDER_PROD_1_PRICE")
    var oRDERPROD1PRICE: String? = null,
    @SerializedName("ORDER_PROD_1_QTY")
    var oRDERPROD1QTY: String? = null,
    @SerializedName("ORDER_PROD_2_QTY")
    var oRDERPROD2QTY: String? = null,
    @SerializedName("ORDER_STATUS")
    var oRDERSTATUS: String? = null,
    @SerializedName("ORDER_STATUS_TEXT")
    var oRDERSTATUSTEXT: String? = null,
    @SerializedName("ORDER_STATUS_COLOR")
    var oRDERSTATUSCOLOR: String? = null,
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
    @SerializedName("ORDER_STATUS_PAY")
    var oRDERSTATUSPAY: String? = null,
    @SerializedName("ORDER_STATUS_PAY_TEXT")
    var oRDERSTATUSPAYTEXT: String? = null,
    @SerializedName("ORDER_FROM_NAME")
    var oRDERFROMNAME: String? = null,
    @SerializedName("ORDER_FROM_PHONE")
    var oRDERFROMPHONE: String? = null,
    @SerializedName("ORDER_FROM")
    var oRDERFROM: UserModel? = null
) : Parcelable