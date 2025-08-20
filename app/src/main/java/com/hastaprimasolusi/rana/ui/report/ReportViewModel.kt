package com.hastaprimasolusi.rana.ui.report

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hastaprimasolusi.rana.data.network.ApiRepository
import com.hastaprimasolusi.rana.data.network.ResultData
import com.hastaprimasolusi.rana.data.network.response.UserModel
import com.hastaprimasolusi.rana.data.network.response.order.OrderStatusModel
import com.hastaprimasolusi.rana.data.network.response.order.ReportResumeModel
import com.hastaprimasolusi.rana.data.network.response.order.ReportTransModel
import com.hastaprimasolusi.rana.data.network.response.payment.PaymentStatusModel
import com.hastaprimasolusi.rana.data.network.response.report.MutasiDetailModel
import com.hastaprimasolusi.rana.data.network.response.report.MutasiInfoModel
import com.hastaprimasolusi.rana.data.network.response.report.MutasiResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ReportViewModel(val apiRepository: ApiRepository): ViewModel(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    var searchParam = ""
    var statusParam = ""
    var typeParam = ""
    var offset = 0
    var limit = 10
    var dateStartParam = ""
    var dateEndParam = ""
    var isReloading = false

    var userModel: UserModel? = null
    val orderType = MutableLiveData<MutableList<OrderStatusModel>>()
    val listStatus = MutableLiveData<MutableList<OrderStatusModel>>()
    val mutasiResume = MutableLiveData<MutasiInfoModel>()
    val mutasiData = MutableLiveData<MutableList<MutasiDetailModel>>()
    val reportResume = MutableLiveData<ReportResumeModel>()
    val reportData = MutableLiveData<MutableList<ReportTransModel>>()
    val loadingProgress = MutableLiveData<Boolean>()
    val loadingStatus = MutableLiveData<Boolean>()
    val disableLoadMore = MutableLiveData<Boolean>()
    val showErrorReport = MutableLiveData<String>()

    fun getReport(isLoading: Boolean, isOffsetReset: Boolean){
        if(isOffsetReset) offset = 0
        if(isLoading) loadingProgress.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.getReport(searchParam, statusParam, typeParam,
                offset.toString(), limit.toString(), dateStartParam, dateEndParam) }
            if(isLoading) loadingProgress.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            response.dATA?.rESUME?.let { reportResume.postValue(it) }
                            if(isOffsetReset){
                                response.dATA?.tRANSAKSI?.let { reportData.postValue(it.toMutableList()) }
                            }else{
                                val list = reportData.value ?: mutableListOf()
                                response.dATA?.tRANSAKSI?.let {
                                    list.addAll(it)
                                } ?: run { disableLoadMore.postValue(true) }
                                reportData.postValue(list)
                            }
                            offset += limit
                        }
                        else -> showErrorReport.postValue(response.rCM)
                    }
                }
                is ResultData.Error -> {
                    showErrorReport.postValue(result.exception.message)
                }
            }
        }
    }

    fun getPaymentStatus(){
        loadingStatus.postValue(true)
        launch {
//            val result = withContext(Dispatchers.IO){ apiRepository.getPaymentStatus() }
//            loadingStatus.postValue(false)
//            when(result){
//                is ResultData.Success -> {
//                    val response = result.data
//                    when(response.rC){
//                        "0000" -> {
//                            response.dATA?.let {
//                                listStatus.postValue(it.toMutableList())
//                            }
//                        }
//                        else -> showErrorReport.postValue(response.rCM)
//                    }
//                }
//                is ResultData.Error -> showErrorReport.postValue(result.exception.message)
//            }
        }
    }

    fun getOrderType(){

    }

    fun getMutasi(limit: String, offset: String, date1: String, date2: String, isLoading: Boolean){
        if(isLoading) loadingProgress.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.getMutasi(offset, limit, date1, date2) }
            if(isLoading) loadingProgress.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    if(response.rC == "0000"){
                        if(isLoading){
                            response.dATA?.mUTASI?.let { mutasiData.postValue(it.toMutableList()) }
                        }else{
                            val list = mutasiData.value ?: mutableListOf()
                            response.dATA?.mUTASI?.let { list.addAll(it) }
                            mutasiData.postValue(list)
                        }

                    }else{
                        showErrorReport.postValue(response.rCM)
                    }
                }
                is ResultData.Error -> showErrorReport.postValue(result.exception.message)
            }
        }
    }

    fun cairkanDana(listener:(String) -> Unit, errorListener:(String) -> Unit){
        loadingProgress.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.pencairanDana() }
            loadingProgress.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    if(response.rC == "0000"){
                        listener(response.rCM.toString())
                    }else{
                        errorListener(response.rCM.toString())
                    }
                }
                is ResultData.Error -> errorListener(result.exception.message.toString())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}