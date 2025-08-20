package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel

data class CanvasBagModel(
    @SerializedName("CURRENCY")
    var cURRENCY: String? = null,
    @SerializedName("ID")
    var iD: String? = null,
    @SerializedName("MODIFIED_AT")
    var mODIFIEDAT: String? = null,
    @SerializedName("PRODUCT")
    var pRODUCT: List<ProdukModel>? = null,
    @SerializedName("TEXT")
    var tEXT: String? = null,
    @SerializedName("TOTAL_AMT")
    var tOTALAMT: String? = null,
    @SerializedName("TOTAL_QTY")
    var tOTALQTY: String? = null
)