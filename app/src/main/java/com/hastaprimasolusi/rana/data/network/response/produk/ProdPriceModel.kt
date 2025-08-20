package com.hastaprimasolusi.rana.data.network.response.produk


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProdPriceModel(
    @SerializedName("PROD_QTY")
    var pRODQTY: String? = null,
    @SerializedName("PROD_PRICE")
    var pRODPRICE: String? = null,
    @SerializedName("PROD_STOCK")
    var pRODSTOCK: String? = null,
    @SerializedName("PROD_UNIT")
    var pRODUNIT: String? = null,
    @SerializedName("PROD_UNIT_NAME")
    var pRODUNITNAME: String? = null,
    @SerializedName("PROD_TOTAL_AMT")
    var pRODTOTALAMT: String? = null,
    @SerializedName("PROD_UNIT_VALUE_MIN")
    var pRODUNITMIN: String? = null,
    @SerializedName("PROD_UNIT_VALUE_MAX")
    var pRODUNITMAX: String? = null
): Parcelable