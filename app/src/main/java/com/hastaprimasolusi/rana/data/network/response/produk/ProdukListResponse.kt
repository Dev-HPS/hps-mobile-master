package com.hastaprimasolusi.rana.data.network.response.produk


import com.google.gson.annotations.SerializedName

data class ProdukListResponse(
    @SerializedName("PROMO")
    var pROMO: List<ProdukPromoModel>? = null,
    @SerializedName("DATA")
    var dATA: List<ProdukModel>? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)