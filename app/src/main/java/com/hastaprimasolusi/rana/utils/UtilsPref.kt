package com.hastaprimasolusi.rana.utils

import com.google.gson.Gson
import com.hastaprimasolusi.rana.data.network.response.UserModel
import com.orhanobut.hawk.Hawk

object UtilsPref {
    fun saveString(key:String, data: String){
        Hawk.put(key, data)
    }

    fun loadString(key:String) : String {
        if(Hawk.contains(key)){
            return Hawk.get(key)
        }
        return ""
    }

    fun clearString(key:String) {
        Hawk.delete(key)
    }

    fun saveBoolean(key: String, data: Boolean){
        Hawk.put(key, data)
    }

    fun loadBoolean(key:String) : Boolean {
        return Hawk.get(key, false)
    }

    fun getUserRole() : String {
        val userData = Gson().fromJson(loadString("userData"), UserModel::class.java)
        println("USER ROLE ${userData?.rOLENAME}")
        return userData?.rOLENAME ?: "undefined"
    }

}