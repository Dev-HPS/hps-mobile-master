package com.hastaprimasolusi.rana.data.network.response


import com.google.gson.annotations.SerializedName

data class LpModel(
    @SerializedName("LP_CODE")
    var lPCODE: String? = null,
    @SerializedName("LP_ID")
    var lPID: String? = null,
    @SerializedName("LP_NAME")
    var lPNAME: String? = null,
    @SerializedName("LP_OWNER_NAME")
    var lPOWNERNAME: String? = null
)