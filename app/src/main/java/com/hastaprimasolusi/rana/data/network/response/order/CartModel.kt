package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class CartModel(
    @SerializedName("CART_CURRENCY")
    var cARTCURRENCY: String? = null,
    @SerializedName("CART_ID")
    var cARTID: String? = null,
    @SerializedName("CART_MODIFIED_AT")
    var cARTMODIFIEDAT: String? = null,
    @SerializedName("CART_TEXT")
    var cARTTEXT: String? = null,
    @SerializedName("CART_TOTAL_AMT")
    var cARTTOTALAMT: String? = null,
    @SerializedName("CART_TOTAL_QTY")
    var cARTTOTALQTY: String? = null,
    @SerializedName("TOTAL_AMT")
    var tOTALAMT: String? = null,
    @SerializedName("TOTAL_QTY")
    var tOTALQTY: String? = null,
    @SerializedName("CURRENCY")
    var cURRENCY: String? = null,
    @SerializedName("CART_LP_CODE")
    var cARTLPCODE: String? = null
)