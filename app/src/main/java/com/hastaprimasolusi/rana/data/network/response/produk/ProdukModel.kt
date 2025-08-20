package com.hastaprimasolusi.rana.data.network.response.produk


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hastaprimasolusi.rana.data.network.response.produk.CategoryHierarchiesModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProdukModel(
    @SerializedName("PROD_AVAILABILITY")
    var pRODAVAILABILITY: String? = null,
    @SerializedName("PROD_CATEGORY")
    var pRODCATEGORY: String? = null,
    @SerializedName("PROD_CATEGORY_HIERARCHIES")
    var pRODCATEGORYHIERARCHIES: List<CategoryHierarchiesModel>? = null,
    @SerializedName("PROD_CATEGORY_HIERARCHIES_TEXT")
    var pRODCATEGORYHIERARCHIESTEXT: String? = null,
    @SerializedName("PROD_CATEGORY_NAME")
    var pRODCATEGORYNAME: String? = null,
    @SerializedName("PROD_CODE")
    var pRODCODE: String? = null,
    @SerializedName("PROD_CURRENCY")
    var pRODCURRENCY: String? = null,
    @SerializedName("PROD_DESCRIPTION")
    var pRODDESCRIPTION: String? = null,
    @SerializedName("PROD_DISCOUNT")
    var pRODDISCOUNT: String? = null,
    @SerializedName("PROD_ID")
    var pRODID: String? = null,
    @SerializedName("PROD_IS_INTERN")
    var pRODISINTERN: String? = null,
    @SerializedName("PROD_NAME")
    var pRODNAME: String? = null,
    @SerializedName("PROD_PIC_LARGE")
    var pRODPICLARGE: String? = null,
    @SerializedName("PROD_PIC_MEDIUM")
    var pRODPICMEDIUM: String? = null,
    @SerializedName("PROD_PIC_SMALL")
    var pRODPICSMALL: String? = null,
//    @SerializedName("PROD_PRICE")
//    var pRODPRICE: String? = null,
    //DEVEL PRICE
    @SerializedName("PROD_PRICE")
    var pRODPRICE: List<ProdPriceModel>? = null,
    @SerializedName("PROD_PRICE_LIST")
    var pRODPRICELIST: String? = null,
    @SerializedName("PROD_SLUG")
    var pRODSLUG: String? = null,
    @SerializedName("PROD_STATUS")
    var pRODSTATUS: String? = null,
    @SerializedName("PROD_STOCK")
    var pRODSTOCK: String? = null,
    @SerializedName("PROD_UNIT")
    var pRODUNIT: String? = null,
    @SerializedName("PROD_UNIT_NAME")
    var pRODUNITNAME: String? = null,
    @SerializedName("PROD_UNITS")
    var pRODUNITS: List<ProdUnitModel>? = null,
    @SerializedName("PROD_LP_CODE")
    var pRODLPCODE: String? = null,
    @SerializedName("PROD_LP_NAME")
    var pRODLPNAME: String? = null,
    @SerializedName("PROD_LP_OWNER_NAME")
    var pRODLPOWNERNAME: String? = null,
    @SerializedName("PROD_LP_ID")
    var pRODLPID: String? = null,
    @SerializedName("PROD_IMG_XS")
    var pRODIMGXS: String? = null,
    @SerializedName("PROD_QTY")
    var pRODQTY: Int? = null,
    var jumlah: Int = 0
) : Parcelable