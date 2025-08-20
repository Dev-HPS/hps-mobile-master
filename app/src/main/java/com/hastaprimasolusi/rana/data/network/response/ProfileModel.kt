package com.hastaprimasolusi.rana.data.network.response


import com.google.gson.annotations.SerializedName

data class ProfileModel(
    @SerializedName("OWNER_NAME")
    var oWNERNAME: String? = null,
    @SerializedName("USER_ACCOUNT_INFO")
    var uSERACCOUNTINFO: ProfileInfoModel? = null,
    @SerializedName("USER_ALAMAT")
    var uSERALAMAT: String? = null,
    @SerializedName("USER_CODE")
    var uSERCODE: String? = null,
    @SerializedName("USER_DISPLAY_NAME")
    var uSERDISPLAYNAME: String? = null,
    @SerializedName("USER_EMAIL")
    var uSEREMAIL: String? = null,
    @SerializedName("USER_ID")
    var uSERID: String? = null,
    @SerializedName("USER_KEC_ID")
    var uSERKECID: String? = null,
    @SerializedName("USER_KEC_NAME")
    var uSERKECNAME: String? = null,
    @SerializedName("USER_KEL_DESA_ID")
    var uSERKELDESAID: String? = null,
    @SerializedName("USER_KEL_DESA_NAME")
    var uSERKELDESANAME: String? = null,
    @SerializedName("USER_KOTA_KAB_ID")
    var uSERKOTAKABID: String? = null,
    @SerializedName("USER_KOTA_KAB_NAME")
    var uSERKOTAKABNAME: String? = null,
    @SerializedName("USER_LAST_LOGIN_APP")
    var uSERLASTLOGINAPP: String? = null,
    @SerializedName("USER_PHONE")
    var uSERPHONE: String? = null,
    @SerializedName("USER_PROV_ID")
    var uSERPROVID: String? = null,
    @SerializedName("USER_PROV_NAME")
    var uSERPROVNAME: String? = null,
    @SerializedName("USER_ROLE")
    var uSERROLE: String? = null,
    @SerializedName("USER_ROLE_NAME")
    var uSERROLENAME: String? = null,
    @SerializedName("USER_STATUS")
    var uSERSTATUS: String? = null,
    @SerializedName("USER_STATUS_NAME")
    var uSERSTATUSNAME: String? = null,
    @SerializedName("USER_URL")
    var uSERURL: String? = null
)