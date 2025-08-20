package com.hastaprimasolusi.rana.data.network.response.master


import com.google.gson.annotations.SerializedName

data class ProvinsiModel(
    @SerializedName("PROV_ID")
    var pROVID: String? = null,
    @SerializedName("PROV_NAME")
    var pROVNAME: String? = null
){
    override fun toString(): String {
        return this.pROVNAME.toString()
    }
}