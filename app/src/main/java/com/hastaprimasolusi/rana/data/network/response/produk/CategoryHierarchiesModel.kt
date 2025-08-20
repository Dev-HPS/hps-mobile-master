package com.hastaprimasolusi.rana.data.network.response.produk


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryHierarchiesModel(
    @SerializedName("CAT_ID")
    var cATID: Int? = null,
    @SerializedName("CAT_NAME")
    var cATNAME: String? = null
) : Parcelable