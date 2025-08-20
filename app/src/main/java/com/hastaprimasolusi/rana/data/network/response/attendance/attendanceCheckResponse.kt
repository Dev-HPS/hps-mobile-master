package com.hastaprimasolusi.rana.data.network.response.attendance



import com.google.gson.annotations.SerializedName

data class AttendanceCheckResponse(
    @SerializedName("DATA")
    var dATA: List<AttendanceModel>? = null,
    @SerializedName("RC")
    var rC: String? = null,
    @SerializedName("RCM")
    var rCM: String? = null
)


data class AttendanceModel(
    @SerializedName("ATTENDANCE_PICTURE")
    var aTTENDANCEPICTURE: String? = null,
    @SerializedName("ATTENDANCE_DATE")
    var aTTENDANCEDATE: String? = null,
    @SerializedName("ATTENDANCE_TYPE")
    var aTTENDANCETYPE: String? = null,
    @SerializedName("ATTENDANCE_TIME")
    var aTTENDANCETIME: String? = null
)