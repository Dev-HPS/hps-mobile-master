package com.hastaprimasolusi.rana.data.network.response.master


import com.google.gson.annotations.SerializedName

data class KabKotaModel(
    @SerializedName("KOTA_KAB_ID")
    var kOTAKABID: String? = null,
    @SerializedName("KOTA_KAB_NAME")
    var kOTAKABNAME: String? = null,
    @SerializedName("PROV_ID")
    var pROVID: String? = null
){
    override fun toString(): String {
        return this.kOTAKABNAME.toString()
    }
}