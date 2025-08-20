package com.hastaprimasolusi.rana.data.network.response.report


import com.google.gson.annotations.SerializedName

data class MutasiDetailModel(
    @SerializedName("MUT_DEBIT")
    var mUTDEBIT: String? = null,
    @SerializedName("MUT_KREDIT")
    var mUTKREDIT: String? = null,
    @SerializedName("MUT_SALDO")
    var mUTSALDO: String? = null,
    @SerializedName("MUT_TRX_CODE")
    var mUTTRXCODE: String? = null,
    @SerializedName("MUT_TRX_DATE")
    var mUTTRXDATE: String? = null,
    @SerializedName("MUT_TRX_DESC")
    var mUTTRXDESC: String? = null
)