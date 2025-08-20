package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName
import com.hastaprimasolusi.rana.data.network.response.produk.ProdPriceModel

data class HistoryDetailProdModel(
    @SerializedName("ORDER_PAY_AMT")
    var oRDERPAYAMT: String? = null,
    @SerializedName("ORDER_PAY_QTY")
    var oRDERPAYQTY: String? = null,
    @SerializedName("ORDER_PROD_AMT")
    var oRDERPRODAMT: String? = null,
//    @SerializedName("ORDER_PROD_PRICE")
//    var oRDERPRODPRICE: String? = null,
    //DEVEL PRICE
    @SerializedName("ORDER_PROD_PRICE")
    var oRDERPRODPRICE: List<ProdPriceModel>? = null,
    @SerializedName("ORDER_PROD_STOCK")
    var oRDERPRODSTOCK: String? = null,
    @SerializedName("ORDER_PROD_QTY")
    var oRDERPRODQTY: String? = null,
    @SerializedName("ORDER_PROD_TEXT")
    var oRDERPRODTEXT: String? = null,
    @SerializedName("ORDER_PROD_UNIT")
    var oRDERPRODUNIT: String? = null,
    @SerializedName("PROD_ID")
    var pRODID: String? = null,
    @SerializedName("PROD_NAME")
    var pRODNAME: String? = null,
    @SerializedName("PROD_PIC_LARGE")
    var pRODPICLARGE: String? = null,
    @SerializedName("PROD_PIC_MEDIUM")
    var pRODPICMEDIUM: String? = null,
    @SerializedName("PROD_PIC_SMALL")
    var pRODPICSMALL: String? = null,
    var isChecked: Boolean = false
)