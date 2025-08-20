package com.hastaprimasolusi.rana.data.network

/**
 * Created By maasrahman on 5/14/20
 */

sealed class ResultData<out T : Any> {
    class Success<out T : Any>(val data: T) : ResultData<T>()
    class Error(val exception: Throwable) : ResultData<Nothing>()
}