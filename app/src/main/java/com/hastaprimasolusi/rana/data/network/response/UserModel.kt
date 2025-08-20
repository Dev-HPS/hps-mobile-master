package com.hastaprimasolusi.rana.data.network.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    @SerializedName("NAME")
    var nAME: String? = null,
    @SerializedName("CODE")
    var cODE: String? = null,
    @SerializedName("ALAMAT")
    var aLAMAT: String? = null,
    @SerializedName("DESA_ID")
    var dESAID: String? = null,
    @SerializedName("DESA_NAME")
    var dESANAME: String? = null,
    @SerializedName("EMAIL")
    var eMAIL: String? = null,
    @SerializedName("ID")
    var iD: String? = null,
    @SerializedName("KEC_ID")
    var kECID: String? = null,
    @SerializedName("KEC_NAME")
    var kECNAME: String? = null,
    @SerializedName("KOTA_KAB_ID")
    var kOTAKABID: String? = null,
    @SerializedName("KOTA_KAB_NAME")
    var kOTAKABNAME: String? = null,
    @SerializedName("LAST_LOGIN")
    var lASTLOGIN: String? = null,
    @SerializedName("LOGIN")
    var lOGIN: String? = null,
    @SerializedName("PHONE")
    var pHONE: String? = null,
    @SerializedName("PICTURE")
    var pICTURE: String? = null,
    @SerializedName("PROV_ID")
    var pROVID: String? = null,
    @SerializedName("PROV_NAME")
    var pROVNAME: String? = null,
    @SerializedName("ROLE")
    var rOLE: Int? = null,
    @SerializedName("ROLE_NAME")
    var rOLENAME: String? = null,
    @SerializedName("ROLE_DISPLAY_NAME")
    var rOLEDISPLAYNAME: String? = null,
    @SerializedName("OWNER_NAME")
    var oWNERNAME: String? = null,
    @SerializedName("OUTLET_NAME")
    var oUTLETNAME: String? = null,
    @SerializedName("ADDRESS")
    var aDDRESS: String? = null,
    @SerializedName("PROFILE")
    var pROFILE: String? = null,
    @SerializedName("ACCOUNT_NUMBER")
    var aCCOUNTNUMBER: String? = null,
    @SerializedName("ACCOUNT_BALANCE")
    var aCCOUNTBALANCE: String? = null,
    @SerializedName("ACCOUNT_POINT")
    var aCCOUNTPOINT: String? = null,
    @SerializedName("ACCOUNT_LEVEL")
    var aCCOUNTLEVEL: String? = null
) : Parcelable