package com.hastaprimasolusi.rana.data.network.response


import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("DATA")
    var dATA: ProfileModel? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)