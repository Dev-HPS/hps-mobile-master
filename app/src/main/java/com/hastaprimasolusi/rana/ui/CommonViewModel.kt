package com.hastaprimasolusi.rana.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.ApiRepository
import com.hastaprimasolusi.rana.data.network.ResultData
import com.hastaprimasolusi.rana.data.network.requesthelper.PhotoRequest
import com.hastaprimasolusi.rana.data.network.response.UserModel
import com.hastaprimasolusi.rana.utils.UtilsPref
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by maasrahman on 22/09/20.
 */
class CommonViewModel(private val apiRepository: ApiRepository): ViewModel(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val loadingPhoto = MutableLiveData<Boolean>()
    val showErrorCommon = MutableLiveData<String>()
    val isUnAuthorized = MutableLiveData<Boolean>()

    fun updateProfile(context: Context, photoRequest: PhotoRequest, listener:(UserModel) -> Unit, error:(String) -> Unit){
        loadingPhoto.postValue(true)
        launch {
            val result = withContext(Dispatchers.IO){ apiRepository.updatePhoto(photoRequest) }
            loadingPhoto.postValue(false)
            when(result){
                is ResultData.Success -> {
                    val response = result.data
                    when(response.rC){
                        "0000" -> {
                            val objUser = UtilsPref.loadString(context.getString(R.string.userData))
                            val userModel = Gson().fromJson(objUser, UserModel::class.java)
                            userModel.pICTURE = response.dATA?.imgUrl
                            UtilsPref.saveString(context.getString(R.string.userData), Gson().toJson(userModel))
                            listener(userModel)
                        }
                        "0001" -> isUnAuthorized.postValue(true)
                        else -> error(response.rCM.toString())
                    }
                }
                is ResultData.Error -> error(result.exception.message.toString())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}