package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class QrResponseModel(
    @SerializedName("CODE")
    var cODE: String? = null,
    @SerializedName("DATE")
    var dATE: String? = null,
    @SerializedName("ID")
    var iD: String? = null,
    @SerializedName("STATUS")
    var sTATUS: String? = null,
    @SerializedName("STATUS_TEXT")
    var sTATUSTEXT: String? = null
)