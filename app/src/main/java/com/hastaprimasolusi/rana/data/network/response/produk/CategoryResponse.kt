package com.hastaprimasolusi.rana.data.network.response.produk


import com.google.gson.annotations.SerializedName
import com.hastaprimasolusi.rana.data.network.response.produk.CategoryModel

data class CategoryResponse(
    @SerializedName("DATA")
    var dATA: List<CategoryModel>? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)