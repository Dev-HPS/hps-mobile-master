package com.hastaprimasolusi.rana.data.network.response


import com.google.gson.annotations.SerializedName

data class DeliveryCourierModel(
    @SerializedName("DEL_METHOD")
    var dELMETHOD: String? = null,
    @SerializedName("DEL_METHOD_ID")
    var dELMETHODID: String? = null,
    @SerializedName("DEL_METHOD_NAME")
    var dELMETHODNAME: String? = null,
    @SerializedName("DEL_METHOD_PROFILE")
    var dELMETHODPROFILE: String? = null
)