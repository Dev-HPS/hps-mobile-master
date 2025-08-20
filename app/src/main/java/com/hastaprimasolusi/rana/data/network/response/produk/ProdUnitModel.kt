package com.hastaprimasolusi.rana.data.network.response.produk

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by maasrahman on 30/11/20.
 */
@Parcelize
data class ProdUnitModel (
    @SerializedName("UNIT_ID")
    var unitId: String? = null,
    @SerializedName("UNIT_NAME")
    var unitName: String? = null
): Parcelable