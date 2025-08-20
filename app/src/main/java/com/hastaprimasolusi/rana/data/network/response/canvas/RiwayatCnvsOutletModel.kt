package com.hastaprimasolusi.rana.data.network.response.canvas


import com.google.gson.annotations.SerializedName

data class RiwayatCnvsOutletModel(
    @SerializedName("ALAMAT")
    var aLAMAT: String? = null,
    @SerializedName("LABEL")
    var lABEL: String? = null,
    @SerializedName("NAMA_OUTLET")
    var nAMAOUTLET: String? = null,
    @SerializedName("NAMA_PEMILIK")
    var nAMAPEMILIK: String? = null,
    @SerializedName("PROFILE")
    var pROFILE: String? = null
)