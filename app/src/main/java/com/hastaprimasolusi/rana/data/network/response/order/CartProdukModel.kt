package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName
import com.hastaprimasolusi.rana.data.network.response.produk.ProdPriceModel

data class CartProdukModel(
    @SerializedName("PROD_CATEGORY")
    var pRODCATEGORY: String? = null,
    @SerializedName("PROD_CODE")
    var pRODCODE: String? = null,
    @SerializedName("PROD_DISCOUNT")
    var pRODDISCOUNT: String? = null,
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
//    @SerializedName("PROD_PRICE")
//    var pRODPRICE: String? = null,
    //DEVEL PRICE
    @SerializedName("PROD_PRICE")
    var pRODPRICE: List<ProdPriceModel>? = null,
    @SerializedName("PROD_PRICE_LIST")
    var pRODPRICELIST: String? = null,
    @SerializedName("PROD_QTY")
    var pRODQTY: String? = null,
    @SerializedName("PROD_TOTAL_AMT")
    var pRODTOTALAMT: String? = null,
    @SerializedName("PROD_UNIT_NAME")
    var pRODUNITNAME: String? = null,
    @SerializedName("ID")
    var iD: String? = null,
    @SerializedName("TEXT")
    var tEXT: String? = null,
    @SerializedName("TOTAL_QTY")
    var tOTALQTY: String? = null,
    @SerializedName("TOTAL_AMT")
    var tOTALAMT: String? = null
)