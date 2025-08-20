package com.hastaprimasolusi.rana.data.network.response.produk


import com.google.gson.annotations.SerializedName

data class ProdukDetailResponse(
    @SerializedName("DATA")
    var dATA: ProdukModel? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)