package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class OrderFromModel(
    @SerializedName("ADDRESS")
    var aDDRESS: String? = null,
    @SerializedName("OUTLET_NAME")
    var oUTLETNAME: String? = null,
    @SerializedName("OWNER_NAME")
    var oWNERNAME: String? = null,
    @SerializedName("PHONE")
    var pHONE: String? = null,
    @SerializedName("PROFILE")
    var pROFILE: String? = null
)