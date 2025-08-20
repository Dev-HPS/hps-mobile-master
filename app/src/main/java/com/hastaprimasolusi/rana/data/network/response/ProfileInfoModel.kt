package com.hastaprimasolusi.rana.data.network.response


import com.google.gson.annotations.SerializedName

data class ProfileInfoModel(
    @SerializedName("ACCOUNT_BALANCE")
    var aCCOUNTBALANCE: String? = null,
    @SerializedName("ACCOUNT_LEVEL")
    var aCCOUNTLEVEL: String? = null,
    @SerializedName("ACCOUNT_NUMBER")
    var aCCOUNTNUMBER: String? = null,
    @SerializedName("ACCOUNT_POINT")
    var aCCOUNTPOINT: String? = null
)