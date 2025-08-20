package com.hastaprimasolusi.rana.data.network.response.payment


import com.google.gson.annotations.SerializedName

data class PaymentStatusModel(
    @SerializedName("STS_TRX_ID")
    var sTSTRXID: String? = null,
    @SerializedName("STS_TRX_NAME")
    var sTSTRXNAME: String? = null
){
    override fun toString(): String {
        return sTSTRXNAME.toString()
    }
}