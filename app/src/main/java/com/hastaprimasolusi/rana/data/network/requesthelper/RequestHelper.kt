package com.hastaprimasolusi.rana.data.network.requesthelper

import com.google.gson.annotations.SerializedName

/**
 * Created By maasrahman on 5/9/20
 */

data class LoginRequest(
    @SerializedName("username")
    var userName: String? = null,
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("ftoken")
    var fToken: String? = null,
)

data class CategoryRequest(
    @SerializedName("limit")
    var limit: String? = null,
    @SerializedName("type")
    var type: String? = null,
)

data class PayRequest(
    @SerializedName("PAY_CODE")
    var payCode: String? = null,
    @SerializedName("PAY_METHOD")
    var paymethod: String? = null,
)

data class ListProdukRequest(
    @SerializedName("offset")
    var offset: String? = null,
    @SerializedName("limit")
    var limit: String? = null,
    @SerializedName("search")
    var search: String? = null,
    @SerializedName("category")
    var category: String? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("lp_code")
    var lpCode: String? = null,
)

data class CartRequest(
    @SerializedName("PROD_ID")
    var prodId: String? = null,
    @SerializedName("PROD_QTY")
    var prodQty: String? = null,
    @SerializedName("PROD_UNIT")
    var prodUnit: String? = null,
    @SerializedName("LP_CODE")
    var lpCode: String? = null,
)

data class ApproveRequest(
    @SerializedName("ORDER_ID")
    var oRDERID: String? = null,
    @SerializedName("ORDER_PRODUCT")
    var oRDERPRODUCT: List<ApproveProduct>? = null,
    @SerializedName("TRX_PAYMENT")
    var tRXPAYMENT: String? = null,
)

data class ApproveProduct(
    @SerializedName("PRODUCT_ID")
    var pRODUCTID: String? = null,
    @SerializedName("PRODUCT_UNIT")
    var pRODUCTUNIT: String? = null,
)

data class ConfirmRequest(
    @SerializedName("ORDER_NO")
    var oRDERNO: String? = null,
    @SerializedName("ORDER_CONFIRM")
    var oRDERCONFIRM: String? = null,
    @SerializedName("ORDER_TEXT")
    var oRDERTEXT: String? = null,
)

data class DeliverRequest(
    @SerializedName("ORDER_NO")
    var oRDERNO: String? = null,
    @SerializedName("DEL_METHOD_ID")
    var dELMETHODID: String? = null,
)

data class QrRequest(
    @SerializedName("ID_OUTLET")
    var iDOUTLET: String? = null,
    @SerializedName("ID_TRANSAKSI")
    var iDTRANSAKSI: String? = null,
    @SerializedName("LAT")
    var lAT: String? = null,
    @SerializedName("LONG")
    var lONG: String? = null,
    @SerializedName("DETAIL_ADDRESS")
    var dETAILADDRESS: String? = null,
)

data class QrRequestSpg(
    @SerializedName("OUTLET_NAME")
    var oUTLETNAME: String? = null,
    @SerializedName("OUTLET_OWNER")
    var oUTLETOWNER: String? = null,
    @SerializedName("OUTLET_PHONE")
    var oUTLETPHONE: String? = null,
    @SerializedName("TRX_SPG_ACTIVITY")
    var activitySPG: String? = null,
    @SerializedName("ID_TRANSAKSI")
    var iDTRANSAKSI: String? = null,
    @SerializedName("LAT")
    var lAT: String? = null,
    @SerializedName("LONG")
    var lONG: String? = null,
    @SerializedName("DETAIL_ADDRESS")
    var dETAILADDRESS: String? = null,
)

data class PembatalanRequest(
    @SerializedName("ORDER_CODE")
    var oRDERCODE: String? = null,
)

data class RegisterRequest(
    @SerializedName("NAMA")
    var nAMA: String? = null,
    @SerializedName("NAMA_TOKO")
    var nAMATOKO: String? = null,
    @SerializedName("NAMA_OUTLET")
    var nAMAOUTLET: String? = null,
    @SerializedName("NO_TELPON")
    var nOTELPON: String? = null,
    @SerializedName("EMAIL")
    var eMAIL: String? = null,
    @SerializedName("NAMA_PEMILIK")
    var nAMAPEMILIK: String? = null,
    @SerializedName("DESA_KEL_ID")
    var dESAKELID: String? = null,
    @SerializedName("ALAMAT")
    var aLAMAT: String? = null,
    @SerializedName("OUTLET_IMG")
    var oUTLETIMG: String? = null,
    @SerializedName("OUTLET_LONGITUDE")
    var oUTLETLONGITUDE: String? = null,
    @SerializedName("OUTLET_LATITUDE")
    var oUTLETLATITUDE: String? = null,
    @SerializedName("NIK")
    var nIK: String? = null,
    @SerializedName("ZIP_CODE")
    var zIPCODE: String? = null,
    @SerializedName("PASSWORD")
    var password: String? = null,
    @SerializedName("RE_PASSWORD")
    var rePassword: String? = null,
)

data class AttendanceRequest(
    @SerializedName("STATUS")
    var sTATUS: String? = null,
    @SerializedName("DETAIL_ADDRESS")
    var dETAILADDRESS: String? = null,
    @SerializedName("IMAGE")
    var iMAGE: String? = null,
    @SerializedName("LONG")
    var lONGITUDE: String? = null,
    @SerializedName("LAT")
    var lATITUDE: String? = null,
)

data class PasswordRequest(
    @SerializedName("PASSWORD_BARU")
    var pASSWORDBARU: String? = null,
    @SerializedName("RE_PASSWORD_BARU")
    var rEPASSWORDBARU: String? = null,
)

data class PhotoRequest(
    @SerializedName("IMG")
    var iMG: String? = null,
)

data class ConfirmKirimRequest(
    @SerializedName("ORDER_NO")
    var oRDERNO: String? = null,
    @SerializedName("CONFIRM")
    var cONFIRM: String? = null,
    @SerializedName("TEXT")
    var tEXT: String? = null,
)

data class CheckoutRequest(
    @SerializedName("LP_CODE")
    var lpCode: String? = null,
    @SerializedName("TRX_PAYMENT")
    var iMG: String? = null,
)

data class KembaliBarangRequest(
    @SerializedName("LP_ID")
    var lPID: String? = null,
)