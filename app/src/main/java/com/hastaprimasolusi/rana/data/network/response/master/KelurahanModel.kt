package com.hastaprimasolusi.rana.data.network.response.master


import com.google.gson.annotations.SerializedName

data class KelurahanModel(
    @SerializedName("KEC_ID")
    var kECID: String? = null,
    @SerializedName("KEL_DESA_ID")
    var kELDESAID: String? = null,
    @SerializedName("KEL_DESA_NAME")
    var kELDESANAME: String? = null
){
    override fun toString(): String {
        return this.kELDESANAME.toString()
    }
}