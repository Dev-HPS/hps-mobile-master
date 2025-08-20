package com.hastaprimasolusi.rana.data.network.response.report


import com.google.gson.annotations.SerializedName

data class MutasiModel(
    @SerializedName("INFO")
    var iNFO: MutasiInfoModel? = null,
    @SerializedName("MUTASI")
    var mUTASI: List<MutasiDetailModel>? = null
)