package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class CartResponse(
    @SerializedName("DATA")
    var dATA: CartModel? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)