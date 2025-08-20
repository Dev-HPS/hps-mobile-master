package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class CheckOutModel(
    @SerializedName("ORDER_DATE")
    var oRDERDATE: String? = null,
    @SerializedName("ORDER_NO")
    var oRDERNO: String? = null,
    @SerializedName("ORDER_STATUS")
    var oRDERSTATUS: String? = null
)