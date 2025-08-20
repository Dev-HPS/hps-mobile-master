package com.hastaprimasolusi.rana.data.network.response.order

import com.google.gson.annotations.SerializedName

/**
 * Created By maasrahman on 5/24/20
 */
data class CourierModel (
    @SerializedName("DEL_METHOD_ID")
    var dELMETHODID: String? = null,
    @SerializedName("DEL_METHOD")
    var dELMETHOD: String? = null,
    @SerializedName("DEL_METHOD_NAME")
    var dELMETHODNAME: String? = null,
    @SerializedName("DEL_METHOD_PROFILE")
    var dELMETHODPROFILE: String? = null
)