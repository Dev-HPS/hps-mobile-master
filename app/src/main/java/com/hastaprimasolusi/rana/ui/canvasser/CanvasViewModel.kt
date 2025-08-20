package com.hastaprimasolusi.rana.ui.canvasser

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hastaprimasolusi.rana.data.ApiService
import com.hastaprimasolusi.rana.data.local.DbRepository
import com.hastaprimasolusi.rana.data.local.MessageModel
import com.hastaprimasolusi.rana.data.network.ApiRepository
import com.hastaprimasolusi.rana.data.network.ResultData
import com.hastaprimasolusi.rana.data.network.requesthelper.AttendanceRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.CartRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.CategoryRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.CheckoutRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.ConfirmRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.KembaliBarangRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.ListProdukRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.PayRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.PembatalanRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.QrRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.QrRequestSpg
import com.hastaprimasolusi.rana.data.network.response.LpModel
import com.hastaprimasolusi.rana.data.network.response.PayMethodModel
import com.hastaprimasolusi.rana.data.network.response.PayMethodResponse
import com.hastaprimasolusi.rana.data.network.response.ProfileModel
import com.hastaprimasolusi.rana.data.network.response.UserModel
import com.hastaprimasolusi.rana.data.network.response.canvas.ListTokoModel
import com.hastaprimasolusi.rana.data.network.response.canvas.RiwayatCanvasModel
import com.hastaprimasolusi.rana.data.network.response.canvas.RiwayatDetCnvsModel
import com.hastaprimasolusi.rana.data.network.response.order.CartListModel
import com.hastaprimasolusi.rana.data.network.response.order.CartProdukModel
import com.hastaprimasolusi.rana.data.network.response.order.CartResponse
import com.hastaprimasolusi.rana.data.network.response.order.OrderStatusModel
import com.hastaprimasolusi.rana.data.network.response.order.PaymentModel
import com.hastaprimasolusi.rana.data.network.response.order.PaymentResponse
import com.hastaprimasolusi.rana.data.network.response.order.PembatalanResponse
import com.hastaprimasolusi.rana.data.network.response.produk.CategoryModel
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukListResponse
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * Created By maasrahman on 5/30/20
 */
class CanvasViewModel(val apiRepository: ApiRepository, private val dbRepository: DbRepository) :
    ViewModel(), CoroutineScope {
    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val errorResponse = "Terjadi kesalahan saat memuat data, silahkan coba kembali"
    var userModel: UserModel? = null
    val listHistory = MutableLiveData<MutableList<RiwayatCanvasModel>>()
    val historyDetail = MutableLiveData<RiwayatDetCnvsModel>()
    val listKategori = MutableLiveData<MutableList<CategoryModel>>()
    val listKategoriLP = MutableLiveData<MutableList<CategoryModel>>()
    val listCart = MutableLiveData<MutableList<CartProdukModel>>()
    val listPos = MutableLiveData<MutableList<CartProdukModel>>()
    val listProdKategori = mutableMapOf<String, MutableList<ProdukModel>>()
    val listLpKategori = mutableMapOf<String, MutableList<ProdukModel>>()
    val listProdukStok = MutableLiveData<MutableList<ProdukModel>>()
    val listProdukSearch = MutableLiveData<MutableList<ProdukModel>>()
    val listStatus = MutableLiveData<List<OrderStatusModel>>()
    val listLp = MutableLiveData<MutableList<LpModel>>()
    val listProdukKembali = MutableLiveData<List<ProdukModel>>()
    val listProdukNew = mutableMapOf<String, List<ProdukModel>>()
    val listToko = MutableLiveData<List<ListTokoModel>>()
    var selectedTokoJual: ListTokoModel? = null
    var cartData: CartListModel? = null
    val loadingKategori = MutableLiveData<Boolean>()
    val loadingHistory = MutableLiveData<Boolean>()
    val loadingHistoryDetail = MutableLiveData<Boolean>()
    val loadingProses = MutableLiveData<Boolean>()
    val loadingCart = MutableLiveData<Boolean>()
    val loadingPos = MutableLiveData<Boolean>()
    val loadingCheckOut = MutableLiveData<Boolean>()
    val loadingStok = MutableLiveData<Boolean>()
    val loadingPayment = MutableLiveData<Boolean>()
    val loadingProduk = MutableLiveData<Boolean>()
    val loadingProfile = MutableLiveData<Boolean>()
    val loadingToko = MutableLiveData<Boolean>()
    val showError = MutableLiveData<String>()
    val showErrorPesanan = MutableLiveData<String>()
    val showErrorProd = MutableLiveData<String>()
    val showErrorBayar = MutableLiveData<String>()
    val isUnAuthorized = MutableLiveData<Boolean>()
    val paymethodSelected = MutableLiveData<PayMethodModel>()
    var selectedLp: LpModel? = null
    var dtStart = ""
    var dtEnd = ""
    var updateImage = MutableLiveData<Boolean>()
    val showAttendance = MutableLiveData<Boolean>()
    val showClockIn = MutableLiveData<String>()
    val showClockOut = MutableLiveData<String>()
    private val location = MutableLiveData<Location>()
    val detailAddress = MutableLiveData<String>()
    var buktiBayarImg = MutableLiveData<String>()

    fun setLocationAddress(loc: Location, detail: String) {
        location.postValue(loc)
        detailAddress.postValue(detail)
    }

    fun getUnreadNotif(): LiveData<List<MessageModel>> {
        return dbRepository.unReadMessage
    }

    fun getNotif(): LiveData<List<MessageModel>> {
        return dbRepository.allMessage
    }

    fun updateNotif(isRead: String, id: String) = launch {
        withContext(Dispatchers.IO) {
            dbRepository.update(id, isRead)
        }
    }

    fun homeRequest(limit: String, offset: String, status: String) {
        loadingHistory.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) {
                if (userModel?.rOLENAME == "spg" || userModel?.rOLENAME == "msr") {
                    apiRepository.getRiwayatSpg(limit, offset, status, dtStart, dtEnd)
                } else {
                    apiRepository.getRiwayatCanvasser(limit, offset, status, dtStart, dtEnd)
                }
            }
            attendanceCheck()
            val orderStatus = withContext(Dispatchers.IO) { apiRepository.getOrderStatus("2") }
            loadingHistory.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            val list = listHistory.value ?: mutableListOf()
                            list.clear()
                            response.dATA?.let { row ->
                                list.addAll(row)
                            }
                            listHistory.postValue(list)
                        }

                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }

                        else -> {
                            showError.postValue(response.rCM.toString())
                        }
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(errorResponse)
                }
            }
            when (orderStatus) {
                is ResultData.Success -> {
                    val response = orderStatus.data
                    if (response.rC == "0000") {
//                        showAttendance.postValue(false)
//                        showClockIn.postValue("00:00")
//                        showClockOut.postValue("12:00")
                        response.dATA?.let {
                            listStatus.postValue(it)
                        }
                    }
                }
            }
            when (val cart = withContext(Dispatchers.Main) { apiRepository.cartList() }) {
                is ResultData.Success -> {
                    val response = cart.data
                    when (response.rC) {
                        "0000" -> {
                            response.dATA?.let { cart ->
                                cartData = cart
                                cart.cARTPRODUCT?.let { prod ->
                                    val list = listCart.value ?: mutableListOf()
                                    list.clear()
                                    list.addAll(prod)
                                    listCart.postValue(list)
                                }
                            }
                        }

                        "0014" -> listCart.postValue(null)
                    }
                }
            }
            when (val pos = withContext(Dispatchers.IO) {
//                apiRepository.posList()
                if (userModel?.rOLENAME == "spg" || userModel?.rOLENAME == "msr") {
                    apiRepository.posListSpg()
                } else {
                    apiRepository.posList()
                }
            }) {
                is ResultData.Success -> {
                    val response = pos.data
                    when (response.rC) {
                        "0000" -> {
                            response.dATA?.let { pos ->
//                                cartData = cart
                                pos.pOSPRODUCT?.let { prod ->
                                    val list = listPos.value ?: mutableListOf()
                                    list.clear()
                                    list.addAll(prod)
                                    listPos.postValue(list)
                                }
                            }
                        }

                        "0014" -> listPos.postValue(null)
                    }
                }
            }
            when (val cart = withContext(Dispatchers.Main) { apiRepository.cartList() }) {
                is ResultData.Success -> {
                    val response = cart.data
                    when (response.rC) {
                        "0000" -> {
                            response.dATA?.let { cart ->
                                cartData = cart
                                cart.cARTPRODUCT?.let { prod ->
                                    val list = listCart.value ?: mutableListOf()
                                    list.clear()
                                    list.addAll(prod)
                                    listCart.postValue(list)
                                }
                            }
                        }

                        "0014" -> listCart.postValue(null)
                    }
                }
            }
        }
    }

    fun getPesanan(limit: String, offset: String, status: String, isLoading: Boolean) {
        if (isLoading) loadingHistory.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) {
                if (userModel?.rOLENAME == "spg" || userModel?.rOLENAME == "msr") {
                    apiRepository.getRiwayatSpg(limit, offset, status, dtStart, dtEnd)
                } else {
                    apiRepository.getRiwayatCanvasser(limit, offset, status, dtStart, dtEnd)
                }
            }
            if (isLoading) loadingHistory.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            val list = listHistory.value ?: mutableListOf()
                            if (isLoading) list.clear()
                            response.dATA?.let { row ->
                                list.addAll(row)
                            }
                            listHistory.postValue(list)
                        }

                        "0001" -> isUnAuthorized.postValue(true)
                        else -> {
                            if (!isLoading) showErrorPesanan.postValue(response.rCM)
                        }
                    }
                }

                is ResultData.Error -> {
                    if (isLoading) {
                        showError.postValue(errorResponse)
                    } else {
                        showErrorPesanan.postValue(errorResponse)
                    }
                }
            }
        }
    }

    fun getPesananDetail(id: String) {
        loadingHistoryDetail.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) {
                if (userModel?.rOLENAME == "spg" || userModel?.rOLENAME == "msr") {
                    apiRepository.getRiwayatSpgDetail(id)
                } else {
                    apiRepository.getRiwayatCanvasserDetail(id)
                }
            }
            loadingHistoryDetail.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            historyDetail.postValue(response.dATA)
                        }

                        else -> {
                            showError.postValue(response.rCM.toString())
                        }
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(errorResponse)
                }
            }
        }
    }

    fun getKategori() {
        loadingKategori.postValue(true)
        launch {
            val request = CategoryRequest(limit = "50", type = "3")
            val result = withContext(Dispatchers.IO) { apiRepository.getListCategory(request) }
            loadingKategori.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    if (response.dATA != null) {
                        response.dATA?.let {
                            listKategori.postValue(it.toMutableList())
                        }
                    } else {
                        listKategori.postValue(null)
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(errorResponse)
                }
            }
        }
    }

    fun getKategoriLP() {
        loadingKategori.postValue(true)
        launch {
            val request = CategoryRequest(limit = "50")
            val result = withContext(Dispatchers.IO) { apiRepository.getListCategory(request) }
            loadingKategori.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    response.dATA?.let {
                        listKategoriLP.postValue(it.toMutableList())
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(errorResponse)
                }
            }
        }
    }

    fun attendance(
        status: String?,
        detailAddress: String,
        image: String,
        lat: String,
        long: String,
    ) {
//        if (isLoading) loadingProses.postValue(true)
        launch {
            val attendanceRequest =
                AttendanceRequest(status, detailAddress, image, long, lat)
            val result = withContext(Dispatchers.IO) { apiRepository.attendance(attendanceRequest) }
//            if (isLoading) loadingProses.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
//                            response.dATA?.let { produk ->
//                                lpId?.let { id ->
//                                    listProdukNew[id] = produk
//                                    listener(produk)
//                                }
//                            }
                            if (status == "IN") {
                                showClockIn.postValue(getCurrentTimeString())
                            } else {
                                showClockOut.postValue(getCurrentTimeString())
                            }
                        }

                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showError.postValue(response.rCM.toString())
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(result.exception.message)
                }
            }
        }
    }

    fun attendanceCheck(
    ) {
        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.attendanceCheck() }
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            //absen masuk
                            val attendanceIn = response.dATA?.firstOrNull {
                                it.aTTENDANCETYPE.equals("IN", ignoreCase = true)
                            }
                            if (attendanceIn != null) {
                                showClockIn.postValue("${attendanceIn.aTTENDANCEDATE} ${attendanceIn.aTTENDANCETIME}")
                            } else {
                                showAttendance.postValue(false)
                            }
                            //absen pulang
                            response.dATA?.firstOrNull {
                                it.aTTENDANCETYPE.equals("OUT", ignoreCase = true)
                            }?.let { attendanceOut ->
                                showClockOut.postValue("${attendanceOut.aTTENDANCEDATE} ${attendanceOut.aTTENDANCETIME}")
                            }

                        }

                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showError.postValue(response.rCM.toString())
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(result.exception.message)
                }
            }
        }
    }

    fun getCurrentTimeString(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return formatter.format(calendar.time)
    }

    fun getProdukNew(
        lpId: String?,
        limit: String,
        offset: String,
        isLoading: Boolean,
        listener: (List<ProdukModel>) -> Unit,
    ) {
        if (isLoading) loadingProses.postValue(true)
        launch {
            val produkRequest =
                ListProdukRequest(offset = offset, limit = limit, type = "1", lpCode = lpId)
            val result = withContext(Dispatchers.IO) { apiRepository.getListProduk(produkRequest) }
            if (isLoading) loadingProses.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            response.dATA?.let { produk ->
                                lpId?.let { id ->
                                    listProdukNew[id] = produk
                                    listener(produk)
                                }
                            }
                        }

                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showError.postValue(response.rCM.toString())
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(result.exception.message)
                }
            }
        }
    }

    fun getProdukBySearch(
        lpId: String,
        paramString: String,
        limit: String,
        offset: String,
        isLoading: Boolean,
    ) {
        if (isLoading) loadingProduk.postValue(true)
        val request = ListProdukRequest(
            offset = offset,
            limit = limit,
            search = paramString,
            type = "1",
            lpCode = lpId
        )
        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.getListProduk(request) }
            if (isLoading) loadingProduk.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            val list = mutableListOf<ProdukModel>()
                            response.dATA?.let { prod ->
                                list.addAll(prod)
                            }
                            listProdukSearch.postValue(list)
                        }

                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }

                        else -> {
                            showErrorProd.postValue(response.rCM)
                        }
                    }
                }

                is ResultData.Error -> {
                    showErrorProd.postValue(result.exception.message.toString())
                }
            }
        }
    }

    fun loadProdukLp(lpId: String?): List<ProdukModel>? {
        return listProdukNew[lpId]
    }

    fun getProdukLP(
        apiService: ApiService,
        kategoriId: String?,
        success: (ProdukListResponse) -> Unit,
    ) = launch(Dispatchers.Main) {
        apiService.getProductListCall("0", "100", "", kategoriId.toString(), "1")
            .enqueue(object : Callback<ProdukListResponse> {
                override fun onFailure(call: Call<ProdukListResponse>, t: Throwable) {
                    showError.postValue(t.message.toString())
                }

                override fun onResponse(
                    call: Call<ProdukListResponse>,
                    response: Response<ProdukListResponse>,
                ) {
                    response.body()?.let {
                        success(it)
                    }
                }
            })
    }

    fun getProduk(
        apiService: ApiService,
        kategoriId: String?,
        success: (ProdukListResponse) -> Unit,
    ) = launch(Dispatchers.Main) {
        apiService.getProductListCall("0", "100", "", kategoriId.toString(), "2")
            .enqueue(object : Callback<ProdukListResponse> {
                override fun onFailure(call: Call<ProdukListResponse>, t: Throwable) {
                    showError.postValue(t.message.toString())
                }

                override fun onResponse(
                    call: Call<ProdukListResponse>,
                    response: Response<ProdukListResponse>,
                ) {
                    response.body()?.let {
                        success(it)
                    }
                }
            })
    }

    fun getProdukStok(limit: String, offset: String, isLoading: Boolean) {
        if (isLoading) loadingStok.postValue(true)
        launch {
            val request = ListProdukRequest(offset = offset, limit = limit)
            val result = withContext(Dispatchers.IO) { apiRepository.getListProduk(request) }
            if (isLoading) loadingStok.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            val list = listProdukStok.value ?: mutableListOf()
                            if (isLoading) list.clear()
                            response.dATA?.let { row ->
                                list.addAll(row)
                            }
                            listProdukStok.postValue(list)
                        }

                        else -> {
                            if (isLoading) {
                                showError.postValue(response.rCM)
                            } else {
                                showErrorProd.postValue(response.rCM)
                            }
                        }
                    }
                }

                is ResultData.Error -> {
                    if (isLoading) {
                        showError.postValue(result.exception.message.toString())
                    } else {
                        showErrorProd.postValue(result.exception.message.toString())
                    }
                }
            }
        }
    }

    fun updateCart(model: CartProdukModel, jml: String) {
        val request = CartRequest(prodId = model.pRODID.toString(), prodQty = jml)

        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.cartUpdate(request) }
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            val list = listCart.value
                            list?.find { it.pRODID == model.pRODID }?.pRODQTY = jml
                            listCart.postValue(list)
                        }

                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }
                    }
                }

                is ResultData.Error -> {}
            }
        }
    }

    fun deleteCart(model: CartProdukModel) {
        loadingCart.postValue(true)
        launch {
            val result =
                withContext(Dispatchers.IO) { apiRepository.cartDelete(model.pRODID.toString()) }
            loadingCart.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            val list = listCart.value
                            list?.remove(model)
                            listCart.postValue(list)
                        }

                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }

                        else -> {
                            showError.postValue(response.rCM)
                        }
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(errorResponse)
                }
            }
        }
    }

    fun actionCart(
        id: String,
        jml: String,
        prodUnit: String,
        onSuccess: (type: String, model: CartResponse) -> Unit,
    ) {
        loadingCart.postValue(true)
        val request = CartRequest(
            prodId = id,
            prodQty = jml,
            lpCode = selectedLp?.lPCODE,
            prodUnit = prodUnit
        )
        val list = listCart.value
        val isExist = list?.find { it.pRODID.toString() == id }
        if (isExist != null) {
            val jmlTotal = jml.toInt() + (isExist.pRODQTY?.toIntOrNull() ?: 0)
            request.prodQty = jmlTotal.toString()
            launch {
                val result = withContext(Dispatchers.IO) { apiRepository.cartUpdate(request) }
                loadingCart.postValue(false)
                when (result) {
                    is ResultData.Success -> {
                        onSuccess("update", result.data)
                    }

                    is ResultData.Error -> {
                        showError.postValue(errorResponse)
                    }
                }
            }
        } else {
            launch {
                val result = withContext(Dispatchers.IO) { apiRepository.cartAdd(request) }
                loadingCart.postValue(false)
                when (result) {
                    is ResultData.Success -> {
                        if (cartData == null) {
                            result.data.dATA?.let {
                                cartData = CartListModel(
                                    cARTID = it.cARTID,
                                    cARTTEXT = it.cARTTEXT,
                                    cARTMODIFIEDAT = it.cARTMODIFIEDAT,
                                    cARTTOTALQTY = it.cARTTOTALQTY,
                                    cARTTOTALAMT = it.cARTTOTALAMT,
                                    cARTCURRENCY = it.cARTCURRENCY,
                                    cARTLPCODE = it.cARTLPCODE
                                )
                            }
                        }
                        onSuccess("add", result.data)
                    }

                    is ResultData.Error -> {
                        showError.postValue(errorResponse)
                    }
                }
            }
        }
    }

    fun getCart() {
        loadingCart.postValue(true)

        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.cartList() }
            loadingCart.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            response.dATA?.let { cart ->
                                cartData = cart
                                cart.cARTPRODUCT?.let { prod ->
                                    val list = listCart.value ?: mutableListOf()
                                    list.clear()
                                    list.addAll(prod)
                                    listCart.postValue(list)
                                }
                            }
                        }

                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }

                        else -> {
                            showError.postValue(response.rCM)
                        }
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(errorResponse)
                }
            }
        }
    }

    fun actionCheckOut(onSuccess: () -> Unit) {
        loadingCheckOut.postValue(true)
        launch {
            val request = CheckoutRequest(selectedLp?.lPCODE, buktiBayarImg.value)
            val result = withContext(Dispatchers.IO) { apiRepository.checkOutLp(request) }
            loadingCheckOut.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            val list = listCart.value
                            list?.clear()
                            listCart.postValue(list)
                            onSuccess()
                        }

                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showError.postValue(response.rCM.toString())
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(errorResponse)
                }
            }
        }
    }

    fun addCart(type: String, model: CartProdukModel) {
        val list = listCart.value ?: mutableListOf()
        if (type == "add") {
            list.add(model)
        } else {
            list.find { it.pRODID == model.pRODID }?.pRODPRICE = model.pRODPRICE
        }
        listCart.postValue(list)
    }

    fun updatePos(model: CartProdukModel, jml: String) {
        val request = CartRequest(
            prodId = model.pRODID.toString(),
            prodQty = jml,
            prodUnit = model.pRODPRICE?.first()?.pRODUNIT
        )

        launch {
//            val result = withContext(Dispatchers.IO) { apiRepository.posUpdate(request) }
            val result = withContext(Dispatchers.IO) {
                if (userModel?.rOLENAME == "spg" || userModel?.rOLENAME == "msr") {
                    apiRepository.posUpdateSpg(request)
                } else {
                    apiRepository.posUpdate(request)
                }
            }
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            val list = listPos.value
                            list?.find { it.pRODID == model.pRODID }?.pRODPRICE?.first()?.pRODQTY =
                                jml
                            listPos.postValue(list)
                        }

                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }
                    }
                }

                is ResultData.Error -> {}
            }
        }
    }

    fun deletePos(model: CartProdukModel) {
        loadingPos.postValue(true)
        launch {
//            val result =
//                withContext(Dispatchers.IO) { apiRepository.posDelete(model.pRODID.toString()) }
            val result = withContext(Dispatchers.IO) {
                if (userModel?.rOLENAME == "spg" || userModel?.rOLENAME == "msr") {
                    apiRepository.posDeleteSpg(model.pRODID.toString())
                } else {
                    apiRepository.posDelete(model.pRODID.toString())
                }
            }
            loadingPos.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            val list = listPos.value
                            list?.remove(model)
                            listPos.postValue(list)
                        }

                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }

                        else -> {
                            showError.postValue(response.rCM)
                        }
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(errorResponse)
                }
            }
        }
    }

    fun actionPos(
        id: String,
        jml: String,
        prodUnit: String,
        onSuccess: (type: String, model: CartResponse) -> Unit,
    ) {
        loadingPos.postValue(true)
        val request = CartRequest(prodId = id, prodQty = jml, prodUnit = prodUnit)
        val list = listPos.value
        val isExist = list?.find { it.pRODID.toString() == id }
        if (isExist != null) {
            val jmlTotal = jml.toInt() + (isExist.pRODQTY?.toIntOrNull() ?: 0)
            request.prodQty = jmlTotal.toString()
            launch {
//                val result = withContext(Dispatchers.IO) { apiRepository.posUpdate(request) }
                val result = withContext(Dispatchers.IO) {
                    if (userModel?.rOLENAME == "spg" || userModel?.rOLENAME == "msr") {
                        apiRepository.posUpdateSpg(request)
                    } else {
                        apiRepository.posUpdate(request)
                    }
                }
                loadingPos.postValue(false)
                when (result) {
                    is ResultData.Success -> {
                        onSuccess("update", result.data)
                    }

                    is ResultData.Error -> {
                        showError.postValue(errorResponse)
                    }
                }
            }
        } else {
            launch {
//                val result = withContext(Dispatchers.IO) { apiRepository.posAdd(request) }
                val result = withContext(Dispatchers.IO) {
                    if (userModel?.rOLENAME == "spg" || userModel?.rOLENAME == "msr") {
                        apiRepository.posAddSpg(request)
                    } else {
                        apiRepository.posAdd(request)
                    }
                }
                loadingPos.postValue(false)
                when (result) {
                    is ResultData.Success -> {
                        onSuccess("add", result.data)
                    }

                    is ResultData.Error -> {
                        showError.postValue(errorResponse)
                    }
                }
            }
        }
    }

    fun getPos() {
        loadingPos.postValue(true)

        launch {
//            val result = withContext(Dispatchers.IO) { apiRepository.posList() }
            val result = withContext(Dispatchers.IO) {
                if (userModel?.rOLENAME == "spg" || userModel?.rOLENAME == "msr") {
                    apiRepository.posListSpg()
                } else {
                    apiRepository.posList()
                }
            }
            loadingPos.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            response.dATA?.let { cart ->
//                                cartData = cart
                                cart.pOSPRODUCT?.let { prod ->
                                    val list = listPos.value ?: mutableListOf()
                                    list.clear()
                                    list.addAll(prod)
                                    listPos.postValue(list)
                                }
                            }
                        }

                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }

                        else -> {
                            showError.postValue(response.rCM)
                        }
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(errorResponse)
                }
            }
        }
    }

    fun addPos(type: String, model: CartProdukModel) {
        val list = listPos.value ?: mutableListOf()
        if (type == "add") {
            list.add(model)
            listPos.postValue(list)
        } else {
            getPos()
        }
    }

    fun checkProduct(kategoriId: String): MutableList<ProdukModel>? {
        return listProdKategori[kategoriId]
    }

    fun addProduct(kategoriId: String, list: MutableList<ProdukModel>) {
        listProdKategori[kategoriId] = list
    }

    fun checkProdukLp(kategoriId: String): MutableList<ProdukModel>? {
        return listLpKategori[kategoriId]
    }

    fun addProductLp(kategoriId: String, list: MutableList<ProdukModel>) {
        listLpKategori[kategoriId] = list
    }

    fun confirmPos(id: String, success: () -> Unit) {
        loadingPos.postValue(true)
        launch {
            val request = QrRequest(
                id,
                lAT = location.value?.latitude.toString(),
                lONG = location.value?.longitude.toString(),
                dETAILADDRESS = detailAddress.value
            )
            val result = withContext(Dispatchers.IO) { apiRepository.posConfirm(request) }
            loadingPos.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            success()
                        }

                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }

                        else -> showError.postValue(response.rCM.toString())
                    }
                }

                is ResultData.Error -> showError.postValue(errorResponse)
            }
        }
    }

    fun confirmPosSpg(
        name: String,
        owner: String,
        phone: String,
        option: String,
        success: () -> Unit,
    ) {
        loadingPos.postValue(true)
        launch {
            val requestSpg = QrRequestSpg(
                name,
                owner,
                phone,
                option,
                lAT = location.value?.latitude.toString(),
                lONG = location.value?.longitude.toString(),
                dETAILADDRESS = detailAddress.value
            )
            val result = withContext(Dispatchers.IO) {
                apiRepository.posConfirmSpg(requestSpg)
            }

            loadingPos.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            success()
                        }

                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }

                        else -> showError.postValue(response.rCM.toString())
                    }
                }

                is ResultData.Error -> showError.postValue(errorResponse)
            }
        }
    }

    fun selesaiKirim(id: String, success: () -> Unit) {
        loadingProses.postValue(true)
        launch {
            val request = QrRequest(iDTRANSAKSI = id)
            val result = withContext(Dispatchers.IO) { apiRepository.posConfirm(request) }
            loadingProses.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            success()
                        }

                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }

                        else -> showError.postValue(response.rCM.toString())
                    }
                }

                is ResultData.Error -> showError.postValue(errorResponse)
            }
        }
    }

    fun actionBatalkan(orderCode: String, success: (PembatalanResponse) -> Unit) {
        loadingProses.postValue(true)
        launch {
            val request = PembatalanRequest(orderCode)
            val result = withContext(Dispatchers.IO) { apiRepository.actionBatal(request) }
            loadingProses.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> success(response)
                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showError.postValue(response.rCM.toString())
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(errorResponse)
                }
            }
        }
    }

    fun actionKonfirmLp(orderNo: String, status: String, keterangan: String) {
        loadingProses.postValue(true)
        launch {
            val request = ConfirmRequest(orderNo, status, keterangan)
            val result = withContext(Dispatchers.IO) { apiRepository.konfirmKirim(request) }
            loadingProses.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            val model = historyDetail.value
                            model?.sTATUS = if (status == "1") "7" else "21"
                            historyDetail.postValue(model)
                        }

                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }

                        else -> showError.postValue(response.rCM.toString())
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(errorResponse)
                }
            }
        }
    }

    fun getPaymentMethod(listener: (PayMethodResponse) -> Unit) {
        loadingPayment.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.getPayMethod() }
            loadingPayment.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    if (response.rC == "0000") {
                        listener(response)
                    } else {
                        showErrorBayar.postValue(response.rCM)
                    }
                }

                is ResultData.Error -> {
                    showErrorBayar.postValue(result.exception.message)
                }
            }
        }
    }

    fun setPaymentMethod(listener: (PaymentResponse) -> Unit) {
        loadingPayment.postValue(true)
        launch {
            val request =
                PayRequest(historyDetail.value?.cODE, paymethodSelected.value?.pAYMETHODID)
            println("CEK PARAM ${Gson().toJson(request)}")
            val result = withContext(Dispatchers.IO) { apiRepository.setPayMethod(request) }
            loadingPayment.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            listener(response)
                        }

                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }

                        else -> {
                            showErrorBayar.postValue(response.rCM)
                        }
                    }
                }

                is ResultData.Error -> {
                    showErrorBayar.postValue(result.exception.message)
                }
            }
        }
    }

    fun getLp() {
        loadingProses.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.getLp() }
            loadingProses.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            response.dATA?.let { lp ->
                                listLp.postValue(lp.toMutableList())
                            }
                        }

                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }

                        else -> {
                            showError.postValue(response.rCM.toString())
                        }
                    }
                }

                is ResultData.Error -> {
                    showError.postValue(result.exception.message)
                }
            }
        }
    }

    fun cekDetailBayar(listener: (PaymentModel) -> Unit) {
        loadingProses.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) {
                apiRepository.getDetailPembayaran(
                    historyDetail.value?.cODE ?: ""
                )
            }
            loadingProses.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            response.dATA?.let {
                                listener(it)
                            }
                        }

                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showError.postValue(response.rCM)
                    }
                }

                is ResultData.Error -> showError.postValue(result.exception.message)
            }
        }
    }

    fun cekStatusBayar(listener: (PaymentModel) -> Unit) {
        loadingProses.postValue(true)
        launch {
            val request = PayRequest(payCode = historyDetail.value?.cODE)
            val result = withContext(Dispatchers.IO) { apiRepository.getStatusPembayaran(request) }
            loadingProses.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            response.dATA?.let { listener(it) }
                        }

                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showError.postValue(response.rCM)
                    }
                }

                is ResultData.Error -> showError.postValue(result.exception.message)
            }
        }
    }

    fun getProfile(listener: (ProfileModel) -> Unit, error: (String) -> Unit) {
        loadingProfile.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.getProfile() }
            loadingProfile.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> response.dATA?.let { listener(it) }
                        "0001" -> isUnAuthorized.postValue(false)
                        else -> {
                            error(response.rCM.toString())
                        }
                    }
                }

                is ResultData.Error -> {
                    error(result.exception.message.toString())
                }
            }
        }
    }

    fun getPengembalianProduk() {
        loadingProses.postValue(true)
        launch {
            val result =
                withContext(Dispatchers.IO) { apiRepository.getListBarang(selectedLp?.lPID.toString()) }
            loadingProses.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            response.dATA?.let {
                                listProdukKembali.postValue(it)
                            }
                        }

                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showError.postValue(response.rCM.toString())
                    }
                }

                is ResultData.Error -> showError.postValue(result.exception.message)
            }
        }
    }

    fun konfirmasiPengembalian(listener: (String) -> Unit) {
        loadingProses.postValue(true)
        launch {
            val request = KembaliBarangRequest(selectedLp?.lPID.toString())
            val result = withContext(Dispatchers.IO) { apiRepository.kembaliBarang(request) }
            loadingProses.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            listener(response.rCM.toString())
                        }

                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showError.postValue(response.rCM.toString())
                    }
                }

                is ResultData.Error -> showError.postValue(result.exception.message)
            }
        }
    }

    fun getListToko() {
        loadingToko.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.getListToko(location.value?.latitude.toString(),location.value?.longitude.toString()) }
            loadingToko.postValue(false)
            when (result) {
                is ResultData.Success -> {
                    val response = result.data
                    when (response.rC) {
                        "0000" -> {
                            listToko.postValue(response.dATA)
                        }

                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showError.postValue(response.rCM.toString())
                    }
                }

                is ResultData.Error -> showError.postValue(result.exception.message)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}