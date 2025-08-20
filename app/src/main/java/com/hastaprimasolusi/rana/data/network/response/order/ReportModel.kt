package com.hastaprimasolusi.rana.data.network.response.order


import com.google.gson.annotations.SerializedName

data class ReportModel(
    @SerializedName("RESUME")
    var rESUME: ReportResumeModel? = null,
    @SerializedName("TRANSAKSI")
    var tRANSAKSI: List<ReportTransModel>? = null
)