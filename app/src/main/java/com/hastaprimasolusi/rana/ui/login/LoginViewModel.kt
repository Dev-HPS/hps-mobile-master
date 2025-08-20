package com.hastaprimasolusi.rana.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.ApiRepository
import com.hastaprimasolusi.rana.data.network.ResultData
import com.hastaprimasolusi.rana.data.network.requesthelper.LoginRequest
import com.hastaprimasolusi.rana.data.network.response.LoginResponse
import com.hastaprimasolusi.rana.utils.UtilsPref
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created By maasrahman on 5/9/20
 */
class LoginViewModel(private val apiRepository: ApiRepository): ViewModel(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val showLoading = MutableLiveData<Boolean>()
    val showError = MutableLiveData<String>()
    val loginResponse = MutableLiveData<LoginResponse>()

    fun auth(request: LoginRequest){
        showLoading.postValue(true)

        launch {
            val result = withContext(Dispatchers.IO) { apiRepository.authentication(request) }
            showLoading.postValue(false)

            when(result){
                is ResultData.Success -> {
                    loginResponse.postValue(result.data)
                }
                is ResultData.Error -> {
                    showError.postValue(result.exception.message.toString())
                }
            }
        }
    }

    fun savePref(context: Context){
        val it = loginResponse.value
        it?.let { model ->
            UtilsPref.saveBoolean(context.getString(R.string.isLoggedIn), true)
            UtilsPref.saveString(context.getString(R.string.userToken), model.tOKEN.toString())
            UtilsPref.saveString(context.getString(R.string.userData), Gson().toJson(model.dATA))
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}