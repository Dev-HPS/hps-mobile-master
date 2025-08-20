package com.hastaprimasolusi.rana.ui.mitra

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hastaprimasolusi.rana.data.local.DbRepository
import com.hastaprimasolusi.rana.data.local.MessageModel
import com.hastaprimasolusi.rana.data.network.ApiRepository
import com.hastaprimasolusi.rana.data.network.ResultData
import com.hastaprimasolusi.rana.data.network.requesthelper.*
import com.hastaprimasolusi.rana.data.network.response.*
import com.hastaprimasolusi.rana.data.network.response.order.*
import com.hastaprimasolusi.rana.data.network.response.produk.CategoryModel
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukPromoModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created By maasrahman on 5/14/20
 */
class MitraViewModel(val apiRepository: ApiRepository, private val dbRepository: DbRepository): ViewModel(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    var userModel: UserModel? = null
    var paramSearch = MutableLiveData<String>()
    val listPromo = MutableLiveData<MutableList<ProdukPromoModel>>()
    val listProdukCategory = MutableLiveData<MutableList<ProdukModel>>()
    val listProduk = MutableLiveData<MutableList<ProdukModel>>()
    val listProdukSearch = MutableLiveData<MutableList<ProdukModel>>()
    val listCart = MutableLiveData<MutableList<CartProdukModel>>()
    val listHistory = MutableLiveData<MutableList<HistoryOrderModel>>()
    val listCategory = MutableLiveData<MutableList<CategoryModel>>()
    val listCategorySemua = MutableLiveData<MutableList<CategoryModel>>()
    val historyDetail = MutableLiveData<HistoryDetailModel>()
    val listStatus = MutableLiveData<List<OrderStatusModel>>()
    val listProdukStok = MutableLiveData<MutableList<ProdukModel>>()
    val profileResponse = MutableLiveData<ProfileModel>()
    var produkDetailModel = MutableLiveData<ProdukModel>()
    var cartData: CartListModel? = null
    var isCartLoading = false
    var isHistoryLoading = false
    val loadingStok = MutableLiveData<Boolean>()
    val loadingProduk = MutableLiveData<Boolean>()
    val loadingCart = MutableLiveData<Boolean>()
    val loadingHistory = MutableLiveData<Boolean>()
    val loadingHistoryDetail = MutableLiveData<Boolean>()
    val loadingCheckOut = MutableLiveData<Boolean>()
    val loadingPayment = MutableLiveData<Boolean>()
    val loadingProfile = MutableLiveData<Boolean>()
    val showError = MutableLiveData<String>()
    val showErrorBayar = MutableLiveData<String>()
    val showErrorProd = MutableLiveData<String>()
    val showErrorRiwayat = MutableLiveData<String>()
    val showErrorDetail = MutableLiveData<String>()
    val isUnAuthorized = MutableLiveData<Boolean>()
    val cart14 = MutableLiveData<Boolean>()
    val riwayat14 = MutableLiveData<Boolean>()
    val paymethodSelected = MutableLiveData<PayMethodModel>()
    var categorySelected: CategoryModel? = null
    var dtStart: String = ""
    var dtEnd: String = ""

    fun clearData(){
        listProduk.postValue(null)
        listCart.postValue(null)
        listCategory.postValue(null)
    }

    fun getUnreadNotif() : LiveData<List<MessageModel>>{
        return dbRepository.unReadMessage
    }

    fun getNotif() : LiveData<List<MessageModel>>{
        return dbRepository.allMessage
    }

    fun updateNotif(isRead: String, id: String) = launch{
        withContext(Dispatchers.IO){
            dbRepository.update(id, isRead)
        }
    }

    fun homeRequest(limit: String, offset: String){
        val request = ListProdukRequest(offset = offset, limit = limit, search = paramSearch.value ?: "")
        loadingProduk.postValue(true)

        launch {
            val result = withContext(Dispatchers.Main){ apiRepository.getListProduk(request) }
            val category = withContext(Dispatchers.Main){ apiRepository.getListCategory(CategoryRequest(limit = "7")) }
            loadingProduk.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            val list = listProduk.value ?: mutableListOf()
                            response.dATA?.let { prod ->
                                list.addAll(prod)
                            }
                            response.pROMO?.let { promo ->
                                listPromo.postValue(promo.toMutableList())
                            }
                            listProduk.postValue(list)
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
                    showError.postValue(result.exception.message.toString())
                }
            }
            when(val cart = withContext(Dispatchers.Main){ apiRepository.cartList() }){
                is ResultData.Success -> {
                    val response = cart.data
                    when(response.rC){
                        "0000" -> {
                            response.dATA?.let { cartResp ->
                                cartData = cartResp
                                cartResp.cARTPRODUCT?.let { prod ->
                                    val list = listCart.value ?: mutableListOf()
                                    list.clear()
                                    list.addAll(prod)
                                    listCart.postValue(list)
                                }
                            }
                        }
                    }
                }
            }
            when(category){
                is ResultData.Success -> {
                    val response = category.data
                    when(response.rC){
                        "0000" -> {
                            response.dATA?.let {
                                listCategory.postValue(it.toMutableList())
                            }
                        }
                    }
                }
            }
            when(val status = withContext(Dispatchers.Main){ apiRepository.getOrderStatus("1") }){
                is ResultData.Success -> {
                    val response = status.data
                    when(response.rC){
                        "0000" -> {
                            response.dATA?.let { statusResp ->
                                listStatus.postValue(statusResp)
                            }
                        }
                    }
                }
            }
        }
    }

    fun getProdukByCategori(limit: String, offset: String){
        val request = ListProdukRequest(offset = offset, limit = limit, category = categorySelected?.cATEGORYID)
        loadingProduk.postValue(true)

        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.getListProduk(request) }
            loadingProduk.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            val list = listProdukCategory.value ?: mutableListOf()
                            response.dATA?.let { prod ->
                                list.addAll(prod)
                            }
                            listProdukCategory.postValue(list)
                        }
                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }
                        else -> {
                            showErrorProd.postValue(response.rCM)
                        }
                    }
                }
                is ResultData.Error -> { showErrorProd.postValue(result.exception.message.toString()) }
            }
        }
    }

    fun getProduk(limit: String, offset: String){
        val request = ListProdukRequest(offset = offset, limit = limit)
        launch {
            when(val result = withContext(Dispatchers.IO){ apiRepository.getListProduk(request) }){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            val list = listProduk.value ?: mutableListOf()
                            response.dATA?.let { prod ->
                                list.addAll(prod)
                            }
                            listProduk.postValue(list)
                        }
                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }
                        else -> {
                            showErrorProd.postValue(response.rCM)
                        }
                    }
                }
                is ResultData.Error -> { showErrorProd.postValue(result.exception.message.toString()) }
            }
        }
    }

    fun getProdukBySearch(paramString: String, limit: String, offset: String, isLoading: Boolean){
        if(isLoading) loadingProduk.postValue(true)
        launch {
            val request = ListProdukRequest(offset = offset, limit = limit, search = paramString)
            val result = withContext(Dispatchers.IO){ apiRepository.getListProduk(request) }
            if(isLoading) loadingProduk.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            val list = listProdukSearch.value ?: mutableListOf()
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
                is ResultData.Error -> { showErrorProd.postValue(result.exception.message.toString()) }
            }
        }
    }

    fun getKategoryAll(){
        loadingProduk.postValue(true)
        launch {
            val result = withContext(Dispatchers.Main){ apiRepository.getListCategory(CategoryRequest()) }
            loadingProduk.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            response.dATA?.let {
                                listCategorySemua.postValue(it.toMutableList())
                            }
                        }
                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showErrorProd.postValue(response.rCM.toString())
                    }
                }
                is ResultData.Error -> showErrorProd.postValue(result.exception.message)
            }
        }
    }

    fun getProdukDetail(idProduk: String){
        loadingCart.postValue(true)

        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.getProdukDetail(idProduk) }
            loadingCart.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            response.dATA?.let { prod ->
                                produkDetailModel.postValue(prod)
                            }
                        }
                        "0014" -> {
                            cart14.postValue(true)
                        }
                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }
                        else -> {
                            showError.postValue(response.rCM)
                        }
                    }
                }
                is ResultData.Error -> { showError.postValue(result.exception.message.toString()) }
            }
        }
    }

    fun getCart(){
        loadingCart.postValue(true)

        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.cartList() }
            loadingCart.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            response.dATA?.let { cart ->
                                cartData = cart
                                cart.cARTPRODUCT?.let { prod ->
                                    val list = listCart.value ?: mutableListOf()
                                    list.clear()
                                    list.addAll(prod)
                                    listCart.postValue(list)
                                }
                                if(listCart.value.isNullOrEmpty()){
                                    cart14.postValue(true)
                                }
                            }
                        }
                        "0014" -> {
                            cart14.postValue(true)
                        }
                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }
                        else -> {
                            showError.postValue(response.rCM)
                        }
                    }
                }
                is ResultData.Error -> { showError.postValue(result.exception.message.toString()) }
            }
        }
    }

    fun updateCart(model: CartProdukModel, jml: String){
        val request = CartRequest(prodId = model.pRODID.toString(), prodQty = jml, prodUnit = model.pRODPRICE?.first()?.pRODUNIT)

        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.cartUpdate(request) }
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            val list = listCart.value
                            list?.find { it.pRODID == model.pRODID }?.pRODPRICE?.first()?.pRODQTY = jml
                            listCart.postValue(list)
                        }
                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }
                    }
                }
                is ResultData.Error -> { }
            }
        }
    }

    fun deleteCart(model: CartProdukModel){
        loadingCart.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.cartDelete(model.pRODID.toString()) }
            loadingCart.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            val list = listCart.value
                            list?.remove(model)
                            listCart.postValue(list)
                        }
                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }
                        else -> { showError.postValue(response.rCM) }
                    }
                }
                is ResultData.Error -> { showError.postValue(result.exception.message.toString()) }
            }
        }
    }

    fun actionCart(id: String, jml: String, prodUnit: String, onSuccess:(type: String, model: CartResponse) -> Unit){
        loadingCart.postValue(true)
        val request = CartRequest(prodId = id, prodQty = jml, prodUnit = prodUnit)
        val list = listCart.value
        val isExist = list?.find { it.pRODID.toString() == id }
        if(isExist != null){
            val jmlTotal = jml.toInt() + (isExist.pRODQTY?.toIntOrNull() ?: 0)
            request.prodQty = jmlTotal.toString()
            launch {
                val result = withContext(Dispatchers.IO){ apiRepository.cartUpdate(request) }
                loadingCart.postValue(false)
                when(result){
                    is ResultData.Success -> {
                        onSuccess("update", result.data)
                    }
                    is ResultData.Error -> { showError.postValue(result.exception.message.toString()) }                }
            }
        }else{
            launch {
                val result = withContext(Dispatchers.IO){ apiRepository.cartAdd(request) }
                loadingCart.postValue(false)
                when(result){
                    is ResultData.Success -> {
                        onSuccess("add", result.data)
                    }
                    is ResultData.Error -> { showError.postValue(result.exception.message.toString()) }
                }
            }
        }
    }

    fun addCart(type: String, model: CartProdukModel){
        val list = listCart.value ?: mutableListOf()
        if(type == "add"){
            list.add(model)
        }else{
            list.find { it.pRODID == model.pRODID }?.pRODQTY = model.pRODQTY
        }
        listCart.postValue(list)
    }

    fun actionCheckOut(onSuccess: (String) -> Unit) {
        loadingCheckOut.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.checkOut() }
            loadingCheckOut.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val list = listCart.value
                    list?.clear()
                    listCart.postValue(list)
                    onSuccess(result.data.rCM.toString())
                }
                is ResultData.Error -> {
                    showError.postValue(result.exception.message.toString())
                }
            }
        }
    }

    fun getHistory(limit: String, offset: String, status: String, isLoading: Boolean){
        if(isLoading) loadingHistory.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.getHistoryPesan(limit, offset, status, dtStart, dtEnd) }
            if(isLoading) loadingHistory.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    isHistoryLoading = true
                    when (response.rC) {
                        "0000" -> {
                            val list = listHistory.value ?: mutableListOf()
                            if(isLoading) list.clear()
                            response.dATA?.let { row ->
                                list.addAll(row)
                            }
                            listHistory.postValue(list)
                        }
                        "0001" -> {
                            isUnAuthorized.postValue(true)
                        }
                        "0014" -> {
                            if(isLoading){
                                riwayat14.postValue(true)
                            }else{
                                showErrorRiwayat.postValue(response.rCM)
                            }
                        }
                        else -> {
                            if(!isLoading){
                                showErrorRiwayat.postValue(response.rCM)
                            }
                        }
                    }
                }
                is ResultData.Error -> {
                    if(isLoading){
                        showError.postValue(result.exception.message.toString())
                    }else{
                        showErrorRiwayat.postValue(result.exception.toString())
                    }
                }
            }
        }
    }

    fun getHistoryDetail(id: String){
        loadingHistoryDetail.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.getHistoryPesanDetail(id) }
            loadingHistoryDetail.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            historyDetail.postValue(response.dATA)
                        }
                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showErrorDetail.postValue(response.rCM)
                    }
                }
                is ResultData.Error -> { showErrorDetail.postValue(result.exception.message) }
            }
        }
    }

    fun confirmPesanan(confirmRequest: ConfirmRequest, listener:() -> Unit){
        loadingCheckOut.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.confirmPesanan(confirmRequest) }
            loadingCheckOut.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    if(response.rC == "0000"){
                        listener()
                    }else{
                        showError.postValue(response.rCM)
                    }
                }
                is ResultData.Error -> { showError.postValue(result.exception.message) }
            }
        }
    }

    fun getPaymentMethod(listener:(PayMethodResponse) -> Unit){
        loadingPayment.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.getPayMethod() }
            loadingPayment.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    if(response.rC == "0000"){
                        listener(response)
                    }else{
                        showErrorBayar.postValue(response.rCM)
                    }
                }
                is ResultData.Error -> { showErrorBayar.postValue(result.exception.message) }
            }
        }
    }

    fun setPaymentMethod(listener:(PaymentResponse) -> Unit){
        loadingHistoryDetail.postValue(true)
        launch {
            val request = PayRequest(historyDetail.value?.oRDERNO, paymethodSelected.value?.pAYMETHODID)
            val result = withContext(Dispatchers.IO) { apiRepository.setPayMethod(request) }
            loadingHistoryDetail.postValue(false)
            when(result){
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
                is ResultData.Error -> { showErrorBayar.postValue(result.exception.message) }
            }
        }
    }

    fun cekDetailBayar(listener: (PaymentModel) -> Unit){
        loadingCheckOut.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.getDetailPembayaran(historyDetail.value?.oRDERNO ?: "") }
            loadingCheckOut.postValue(false)
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
                        else -> showErrorDetail.postValue(response.rCM)
                    }
                }
                is ResultData.Error -> showErrorDetail.postValue(result.exception.message)
            }
        }
    }

    fun cekStatusBayar(listener: (PaymentModel) -> Unit){
        loadingCheckOut.postValue(true)
        launch {
            val request = PayRequest(payCode = historyDetail.value?.oRDERNO)
            val result = withContext(Dispatchers.IO){ apiRepository.getStatusPembayaran(request) }
            loadingCheckOut.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            response.dATA?.let { listener(it) }
                        }
                        "0001" -> isUnAuthorized.postValue(true)
                        else -> showErrorDetail.postValue(response.rCM)
                    }
                }
                is ResultData.Error -> showErrorDetail.postValue(result.exception.message)
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

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}