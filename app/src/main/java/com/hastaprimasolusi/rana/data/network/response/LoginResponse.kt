package com.hastaprimasolusi.rana.data.network.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("DATA")
    var dATA: UserModel? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null,
    @SerializedName("TOKEN")
    var tOKEN: String? = null
)

data class UpdatePhotoResponse(
    @SerializedName("DATA")
    var dATA: ImageUrlModel? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)

data class ImageUrlModel(
    @SerializedName("IMG_URL")
    var imgUrl: String? = null
)