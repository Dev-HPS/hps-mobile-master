package com.hastaprimasolusi.rana.ui.daftarmitra

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hastaprimasolusi.rana.data.network.ApiRepository
import com.hastaprimasolusi.rana.data.network.ResultData
import com.hastaprimasolusi.rana.data.network.requesthelper.RegisterRequest
import com.hastaprimasolusi.rana.data.network.response.master.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created By maasrahman on 7/1/20
 */
class DaftarViewModel(private val apiRepository: ApiRepository): ViewModel(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val loadingProses = MutableLiveData<Boolean>()
    val listProv = MutableLiveData<MutableList<ProvinsiModel>>()
    val listKota = MutableLiveData<MutableList<KabKotaModel>>()
    val listKec = MutableLiveData<MutableList<KecamatanModel>>()
    val listKel = MutableLiveData<MutableList<KelurahanModel>>()

    val provSelected = MutableLiveData<ProvinsiModel>()
    val kotaSelected = MutableLiveData<KabKotaModel>()
    val kecSelected = MutableLiveData<KecamatanModel>()
    val kelSelected = MutableLiveData<KelurahanModel>()

    val showError = MutableLiveData<String>()

    fun getProvinsi() {
        loadingProses.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.getProv() }
            loadingProses.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    if(response.rC == "0000"){
                        response.dATA?.let {
                            listProv.postValue(it.toMutableList())
                        }
                    }else{
                        showError.postValue(response.rCM.toString())
                    }
                }
                is ResultData.Error -> {
                    showError.postValue(result.exception.message.toString())
                }
            }
        }
    }

    fun getKabKota(idProv: String){
        loadingProses.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.getKabKota(idProv) }
            loadingProses.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    if(response.rC == "0000"){
                        response.dATA?.let {
                            listKota.postValue(it.toMutableList())
                        }
                    }else{
                        showError.postValue(response.rCM.toString())
                    }
                }
                is ResultData.Error -> {
                    showError.postValue(result.exception.message.toString())
                }
            }
        }
    }

    fun getKec(idKota: String){
        loadingProses.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.getKec(idKota) }
            loadingProses.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    if(response.rC == "0000"){
                        response.dATA?.let {
                            listKec.postValue(it.toMutableList())
                        }
                    }else{
                        showError.postValue(response.rCM.toString())
                    }
                }
                is ResultData.Error -> {
                    showError.postValue(result.exception.message.toString())
                }
            }
        }
    }

    fun getKel(idKec: String){
        loadingProses.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.getKel(idKec) }
            loadingProses.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    if(response.rC == "0000"){
                        response.dATA?.let {
                            listKel.postValue(it.toMutableList())
                        }
                    }else{
                        showError.postValue(response.rCM.toString())
                    }
                }
                is ResultData.Error -> {
                    showError.postValue(result.exception.message.toString())
                }
            }
        }
    }

    fun register(registerRequest: RegisterRequest, listener:() -> Unit){
        loadingProses.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.registerMitra(registerRequest) }
            loadingProses.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    if(response.rC == "0000"){
                        listener()
                    }else{
                        showError.postValue(response.rCM.toString())
                    }
                }
                is ResultData.Error -> {
                    showError.postValue(result.exception.message.toString())
                }
            }
        }
    }

    fun registerToko(registerRequest: RegisterRequest, listener:() -> Unit){
        loadingProses.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.daftarToko(registerRequest) }
            loadingProses.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    if(response.rC == "0000"){
                        listener()
                    }else{
                        showError.postValue(response.rCM.toString())
                    }
                }
                is ResultData.Error -> {
                    showError.postValue(result.exception.message.toString())
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}