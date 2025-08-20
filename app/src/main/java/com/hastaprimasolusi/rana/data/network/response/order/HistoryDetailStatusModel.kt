package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class HistoryDetailStatusModel(
    @SerializedName("STATUS_DATE")
    var sTATUSDATE: String? = null,
    @SerializedName("STATUS_TEXT")
    var sTATUSTEXT: String? = null
)