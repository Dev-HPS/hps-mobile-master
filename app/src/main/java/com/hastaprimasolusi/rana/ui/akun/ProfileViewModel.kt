package com.hastaprimasolusi.rana.ui.akun

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hastaprimasolusi.rana.data.network.ApiRepository
import com.hastaprimasolusi.rana.data.network.ResultData
import com.hastaprimasolusi.rana.data.network.requesthelper.PasswordRequest
import com.hastaprimasolusi.rana.data.network.requesthelper.RegisterRequest
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created By maasrahman on 7/1/20
 */
class ProfileViewModel(private val apiRepository: ApiRepository): ViewModel(), CoroutineScope {
    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val loadingProgress = MutableLiveData<Boolean>()
    val showError = MutableLiveData<String>()
    val isSession = MutableLiveData<Boolean>()

    fun updatePassword(passwordRequest: PasswordRequest, listener:() -> Unit){
        loadingProgress.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.resetPassword(passwordRequest) }
            loadingProgress.postValue(false)
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

    fun updateProfile(registerRequest: RegisterRequest, listener: () -> Unit){
        loadingProgress.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.updateProfile(registerRequest) }
            loadingProgress.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            listener()
                        }
                        "0001" -> isSession.postValue(true)
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