package com.hastaprimasolusi.rana.data.network.response.master


import com.google.gson.annotations.SerializedName

data class KecamatanModel(
    @SerializedName("KEC_ID")
    var kECID: String? = null,
    @SerializedName("KEC_NAME")
    var kECNAME: String? = null,
    @SerializedName("KOTA_KAB_ID")
    var kOTAKABID: String? = null
){
    override fun toString(): String {
        return this.kECNAME.toString()
    }
}