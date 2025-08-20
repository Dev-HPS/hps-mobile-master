package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class CartListModel(
    @SerializedName("CART_CURRENCY")
    var cARTCURRENCY: String? = null,
    @SerializedName("CART_ID")
    var cARTID: String? = null,
    @SerializedName("CART_MODIFIED_AT")
    var cARTMODIFIEDAT: String? = null,
    @SerializedName("CART_PRODUCT")
    var cARTPRODUCT: List<CartProdukModel>? = null,
    @SerializedName("PRODUCT")
    var pOSPRODUCT: List<CartProdukModel>? = null,
    @SerializedName("CART_TEXT")
    var cARTTEXT: String? = null,
    @SerializedName("CART_TOTAL_AMT")
    var cARTTOTALAMT: String? = null,
    @SerializedName("CART_TOTAL_QTY")
    var cARTTOTALQTY: String? = null,
    @SerializedName("CART_LP_CODE")
    var cARTLPCODE: String? = null,
    @SerializedName("CART_ONGKIR")
    var cARTONGKIR: String? = null,
    @SerializedName("CART_TOTAL_SUB")
    var cARTTOTALSUB: String? = null,
    //FOR Canvasser Bag
    @SerializedName("ID")
    var iD: String? = null,
    @SerializedName("TEXT")
    var tEXT: String? = null,
    @SerializedName("MODIFIED_AT")
    var mODIFIEDAT: String? = null,
    @SerializedName("TOTAL_QTY")
    var tOTALQTY: String? = null,
    @SerializedName("TOTAL_AMT")
    var tOTALAMT: String? = null,
    @SerializedName("CURRENCY")
    var cURRENCY: String? = null
)