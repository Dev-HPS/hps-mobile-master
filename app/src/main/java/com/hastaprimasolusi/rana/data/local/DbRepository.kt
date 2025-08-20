package com.hastaprimasolusi.rana.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hastaprimasolusi.rana.data.MessageDao
import com.hastaprimasolusi.rana.utils.UtilsPref

/**
 * Created By maasrahman on 6/22/20
 */

interface DbRepository {
    val allMessage: LiveData<List<MessageModel>>
    val unReadMessage: LiveData<List<MessageModel>>

    fun insert(message: MessageModel)
    suspend fun insertS(message: MessageModel)
    fun update(id: String, x: String)
    fun delete(id: String)
}

class DbRepositoryImp(private val messageDao: MessageDao) : DbRepository {

    override val allMessage: LiveData<List<MessageModel>> = messageDao.getMessage(UtilsPref.getUserRole())
    override val unReadMessage: LiveData<List<MessageModel>> = messageDao.getUnreadMessage(UtilsPref.getUserRole(), "0")

    override fun insert(message: MessageModel) {
        messageDao.insert(message)
    }

    override suspend fun insertS(message: MessageModel){
        messageDao.insertS(message)
    }

    override fun update(id: String, x: String){
        messageDao.updateRead(x, id)
    }

    override fun delete(id: String) {
        messageDao.deleteNotif(id)
    }
}