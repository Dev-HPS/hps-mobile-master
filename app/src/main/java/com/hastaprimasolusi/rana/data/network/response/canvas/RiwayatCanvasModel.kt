package com.hastaprimasolusi.rana.data.network.response.canvas


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RiwayatCanvasModel(
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
    @SerializedName("IMAGE")
    var iMAGE: String? = null,
    @SerializedName("LABEL")
    var lABEL: String? = null,
    @SerializedName("QTY_1")
    var qTY1: String? = null,
    @SerializedName("QTY_2")
    var qTY2: String? = null,
    @SerializedName("STATUS_TEXT")
    var sTATUSTEXT: String? = null,
    @SerializedName("STATUS_COLOR")
    var sTATUSCOLOR: String? = null,
    @SerializedName("STS_BAYAR")
    var sTSBAYAR: String? = null,
    @SerializedName("TITLE")
    var tITLE: String? = null,
    @SerializedName("TOTAL_AMT")
    var tOTALAMT: String? = null,
    @SerializedName("TUJUAN")
    var tUJUAN: String? = null,
    @SerializedName("UNIT")
    var uNIT: String? = null,
    @SerializedName("STATUS")
    var sTATUS: String? = null
) : Parcelable