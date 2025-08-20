package com.hastaprimasolusi.rana.ui.lp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hastaprimasolusi.rana.data.local.DbRepository
import com.hastaprimasolusi.rana.data.local.MessageModel
import com.hastaprimasolusi.rana.data.network.ApiRepository
import com.hastaprimasolusi.rana.data.network.ResultData
import com.hastaprimasolusi.rana.data.network.requesthelper.ApproveRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.DeliverRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.ListProdukRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.PayRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.PembatalanRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.QrRequest
import com.hastaprimasolusi.rana.data.network.response.DeliveryCourierModel
import com.hastaprimasolusi.rana.data.network.response.ProfileModel
import com.hastaprimasolusi.rana.data.network.response.UserModel
import com.hastaprimasolusi.rana.data.network.response.order.HistoryOrderModel
import com.hastaprimasolusi.rana.data.network.response.order.OrderDetailModel
import com.hastaprimasolusi.rana.data.network.response.order.OrderStatusModel
import com.hastaprimasolusi.rana.data.network.response.order.PaymentModel
import com.hastaprimasolusi.rana.data.network.response.order.PembatalanResponse
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Created By maasrahman on 5/21/20
 */
class LpViewModel(val apiRepository: ApiRepository, private val dbRepository: DbRepository): ViewModel(), CoroutineScope {
    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    var userModel: UserModel? = null
    val listHistory = MutableLiveData<MutableList<HistoryOrderModel>>()
    val historyDetail = MutableLiveData<OrderDetailModel>()
    val listCourier = MutableLiveData<MutableList<DeliveryCourierModel>>()
    val listProdukStok = MutableLiveData<MutableList<ProdukModel>>()
    val listStatus = MutableLiveData<List<OrderStatusModel>>()
    val listProdukKembali = MutableLiveData<List<ProdukModel>>()
    val loadingHistory = MutableLiveData<Boolean>()
    val loadingHistoryDetail = MutableLiveData<Boolean>()
    val loadingProses = MutableLiveData<Boolean>()
    val loadingStok = MutableLiveData<Boolean>()
    val loadingProfile = MutableLiveData<Boolean>()
    val showErrorPesanan = MutableLiveData<String>()
    val showError = MutableLiveData<String>()
    val showErrorProd = MutableLiveData<String>()
    val isUnAuthorized = MutableLiveData<Boolean>()
    var dtStart: String = ""
    var dtEnd: String = ""
    var updateImage = MutableLiveData<Boolean>()
    var buktiBayarImg = MutableLiveData<String>()
    fun getUnreadNotif() : LiveData<List<MessageModel>>{
        return dbRepository.unReadMessage
    }

    fun getNotif() : LiveData<List<MessageModel>> {
        return dbRepository.allMessage
    }

    fun updateNotif(isRead: String, id: String) = launch {
        withContext(Dispatchers.IO){
            dbRepository.update(id, isRead)
        }
    }

    fun homeRequest(limit: String, offset: String, status: String){
        loadingHistory.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.getPesanan(limit, offset, status, dtStart, dtEnd) }
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
                    showError.postValue(result.exception.message.toString())
                }
            }
            when (orderStatus) {
                is ResultData.Success -> {
                    val response = orderStatus.data
                    if (response.rC == "0000") {
                        response.dATA?.let {
                            listStatus.postValue(it)
                        }
                    }
                }
            }
        }
    }

    fun getPesanan(limit: String, offset: String, status: String, isLoading: Boolean){
        if(isLoading) loadingHistory.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.getPesanan(limit, offset, status, dtStart, dtEnd) }
            if(isLoading) loadingHistory.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            val list = listHistory.value ?: mutableListOf()
                            if(isLoading) list.clear()
                            response.dATA?.let { row ->
                                list.addAll(row)
                            }
                            listHistory.postValue(list)
                        }
                        "0001" -> isUnAuthorized.postValue(true)
                        else ->{
                            if(isLoading){
                                showError.postValue(response.rCM.toString())
                            }else{
                                showErrorPesanan.postValue(response.rCM.toString())
                            }
                        }
                    }
                }
                is ResultData.Error -> {
                    if(isLoading){
                        showError.postValue(result.exception.message.toString())
                    }else{
                        showErrorPesanan.postValue(result.exception.message.toString())
                    }
                }
            }
        }
    }

    fun getPesananDetail(id: String){
        loadingHistoryDetail.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.getPesananDetail(id) }
            loadingHistoryDetail.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> historyDetail.postValue(response.dATA)
                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showError.postValue(response.rCM.toString())
                    }
                }
                is ResultData.Error -> { showError.postValue(result.exception.message.toString()) }
            }
        }
    }

    fun getProdukStok(limit: String, offset: String, isLoading: Boolean) {
        if(isLoading) loadingStok.postValue(true)
        launch {
            val request = ListProdukRequest(offset = offset, limit = limit)
            val result = withContext(Dispatchers.IO){ apiRepository.getListProduk(request) }
            if(isLoading) loadingStok.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            val list = listProdukStok.value ?: mutableListOf()
                            if(isLoading) list.clear()
                            response.dATA?.let { row ->
                                list.addAll(row)
                            }
                            listProdukStok.postValue(list)
                        }
                        else -> {
                            if(isLoading){
                                showError.postValue(response.rCM)
                            }else{
                                showErrorProd.postValue(response.rCM)
                            }
                        }
                    }
                }
                is ResultData.Error -> {
                    if(isLoading){
                        showError.postValue(result.exception.message.toString())
                    }else{
                        showErrorProd.postValue(result.exception.message.toString())
                    }
                }
            }
        }
    }

    fun approvePesanan(approveRequest: ApproveRequest, listener:() -> Unit){
        loadingProses.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.approvePesanan(approveRequest) }
            loadingProses.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    if(response.rC == "0000"){
//                        val list = listHistory.value
//                        list?.find { it == model }?.oRDERSTATUS = "3"
                        listener()
                    }else{
                        showError.postValue(response.rCM.toString())
                    }
                }
                is ResultData.Error -> { showError.postValue(result.exception.message.toString()) }
            }
        }
    }

    fun getCourier(){
        loadingProses.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.getDeliverCourier() }
            loadingProses.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    if(response.rC == "0000"){
                        val list = listCourier.value ?: mutableListOf()
                        response.dATA?.let {
                            list.addAll(it)
                        }
                        listCourier.postValue(list)
                    }else{
                        showError.postValue(response.rCM.toString())
                    }
                }
                is ResultData.Error -> { showError.postValue(result.exception.message.toString()) }
            }
        }
    }

    fun setCourier(request: DeliverRequest, success:() -> Unit){
        loadingProses.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.setDeliveryCourier(request) }
            loadingProses.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    if(response.rC == "0000"){
                        success()
                    }else{
                        showError.postValue(response.rCM.toString())
                    }
                }
                is ResultData.Error -> { showError.postValue(result.exception.message.toString()) }
            }
        }
    }

    fun actionBatalkan(orderCode: String, success: (PembatalanResponse) -> Unit){
        loadingProses.postValue(true)
        launch {
            val request = PembatalanRequest(orderCode)
            val result = withContext(Dispatchers.IO){ apiRepository.actionBatal(request) }
            loadingProses.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    if(response.rC == "0000"){
                        success(response)
                    }else{
                        showError.postValue(response.rCM.toString())
                    }
                }
                is ResultData.Error -> { showError.postValue(result.exception.message.toString()) }
            }
        }
    }

    fun confirmPos(id: String, success: () -> Unit){
        loadingProses.postValue(true)
        launch {
            val request = QrRequest(iDTRANSAKSI = id)
            val result = withContext(Dispatchers.IO) { apiRepository.posConfirm(request) }
            loadingProses.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            success()
                        }
                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showError.postValue(response.rCM.toString())
                    }
                }
                is ResultData.Error -> showError.postValue(result.exception.message.toString())
            }
        }
    }

    fun cekStatusBayar(listener: (PaymentModel) -> Unit){
        loadingProses.postValue(true)
        launch {
            val request = PayRequest(payCode = historyDetail.value?.oRDERNO)
            val result = withContext(Dispatchers.IO){ apiRepository.getStatusPembayaran(request) }
            loadingProses.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
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

    fun getProfile(listener:(ProfileModel) -> Unit, error:(String) -> Unit){
        loadingProfile.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.getProfile() }
            loadingProfile.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> response.dATA?.let { listener(it) }
                        "0001"-> isUnAuthorized.postValue(false)
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

    fun getPengembalianProduk(){
        loadingStok.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.getBarangKonfirmasi() }
            loadingStok.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
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

    fun konfirmasiPengembalian(listener:(String) -> Unit){
        loadingProses.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.konfirmasiKembaliBarang() }
            loadingProses.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
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

    fun cekDetailBayar(listener: (PaymentModel) -> Unit){
        loadingProses.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.getDetailPembayaran(historyDetail.value?.oRDERNO ?: "") }
            loadingProses.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}