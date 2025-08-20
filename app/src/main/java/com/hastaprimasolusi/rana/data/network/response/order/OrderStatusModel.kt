package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class OrderStatusModel(
    @SerializedName("STS_CODE")
    var sTSCODE: String? = null,
    @SerializedName("STS_TEXT")
    var sTSTEXT: String? = null
){
    override fun toString(): String {
        return sTSTEXT.toString()
    }
}