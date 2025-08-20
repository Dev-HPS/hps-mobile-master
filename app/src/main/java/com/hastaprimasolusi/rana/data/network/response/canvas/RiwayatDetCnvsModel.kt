package com.hastaprimasolusi.rana.data.network.response.canvas


import com.google.gson.annotations.SerializedName

data class RiwayatDetCnvsModel(
    @SerializedName("CAPTION")
    var cAPTION: String? = null,
    @SerializedName("CODE")
    var cODE: String? = null,
    @SerializedName("CURRENCY")
    var cURRENCY: String? = null,
    @SerializedName("DATETIME")
    var dATETIME: String? = null,
    @SerializedName("DATETIME_HUMAN")
    var dATETIMEHUMAN: String? = null,
    @SerializedName("ID")
    var iD: String? = null,
    @SerializedName("INFO_DETAIL")
    var iNFODETAIL: List<RiwayatCnvsProdModel>? = null,
    @SerializedName("INFO_OUTLET")
    var iNFOOUTLET: RiwayatCnvsOutletModel? = null,
    @SerializedName("INFO_RIWAYAT")
    var iNFORIWAYAT: List<RiwayatCnvsStatusModel>? = null,
    @SerializedName("INFO_STATUS_PEMBAYARAN")
    var iNFOSTATUSPEMBAYARAN: RiwayatCnvsBayarModel? = null,
    @SerializedName("LABEL")
    var lABEL: String? = null,
    @SerializedName("STATUS_TEXT")
    var sTATUSTEXT: String? = null,
    @SerializedName("STS_BAYAR")
    var sTSBAYAR: String? = null,
    @SerializedName("TOTAL_AMT")
    var tOTALAMT: String? = null,
    @SerializedName("TYPE")
    var tYPE: String? = null,
    @SerializedName("ORDER_STATUS_PAY")
    var oRDERSTATUSPAY: String? = null,
    @SerializedName("STATUS")
    var sTATUS: String? = null,
    @SerializedName("ADMIN_FEE")
    var aDMINFEE: String? = null,
    @SerializedName("ONGKIR")
    var oNGKIR: String? = null,
    @SerializedName("TOTAL_ORDER_AMT")
    var tOTALORDERAMT: String? = null
)