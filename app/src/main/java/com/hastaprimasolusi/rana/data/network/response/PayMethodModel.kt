package com.hastaprimasolusi.rana.data.network.response


import com.google.gson.annotations.SerializedName

data class PayMethodModel(
    @SerializedName("PAY_METHOD_GROUP")
    var pAYMETHODGROUP: String? = null,
    @SerializedName("PAY_METHOD_ID")
    var pAYMETHODID: String? = null,
    @SerializedName("PAY_METHOD_LOGO")
    var pAYMETHODLOGO: String? = null,
    @SerializedName("PAY_METHOD_NAME")
    var pAYMETHODNAME: String? = null,
    @SerializedName("PAY_METHOD_TEXT")
    var pAYMETHODTEXT: String? = null,
    @SerializedName("PAY_METHOD_ADMIN")
    var pAYMETHODADMIN: String? = null
)