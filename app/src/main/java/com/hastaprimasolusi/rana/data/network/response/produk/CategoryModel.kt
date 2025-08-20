package com.hastaprimasolusi.rana.data.network.response.produk


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hastaprimasolusi.rana.data.network.response.produk.CategoryHierarchiesModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryModel(
    @SerializedName("CATEGORY_ID")
    var cATEGORYID: String? = null,
    @SerializedName("CATEGORY_NAME")
    var cATEGORYNAME: String? = null,
    @SerializedName("CATEGORY_ICON")
    var cATEGORYICON: String? = null,
    @SerializedName("CATEGORY_COUNT")
    var cATEGORYCOUNT: String? = null
) : Parcelable