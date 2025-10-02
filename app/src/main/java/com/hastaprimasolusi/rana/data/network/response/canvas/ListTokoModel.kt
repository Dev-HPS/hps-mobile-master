package com.hastaprimasolusi.rana.data.network.response.canvas

import com.google.gson.annotations.SerializedName

/**
 * Created by maasrahman on 25/02/22.
 */
data class ListTokoModel(
    @SerializedName("ADDRESS")
    var aDDRESS: String? = null,
    @SerializedName("CODE")
    var cODE: String? = null,
    @SerializedName("ID")
    var iD: String? = null,
    @SerializedName("NAME")
    var nAME: String? = null,
    @SerializedName("URL")
    var uRL: String? = null,
    @SerializedName("LATITUDE")
    var lATITUDE: String? = null,
    @SerializedName("LONGITUDE")
    var lONGITUDE: String? = null
)