package com.hastaprimasolusi.rana.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.hastaprimasolusi.rana.data.local.MessageModel

/**
 * Created By maasrahman on 6/22/20
 */
@Dao
interface MessageDao {
    @Query("SELECT * FROM f_message WHERE role=:roleType ORDER BY created_at DESC")
    fun getMessage(roleType: String): LiveData<List<MessageModel>>

    @Query("SELECT * FROM f_message WHERE role=:roleType AND is_read=:isRead ORDER BY created_at DESC")
    fun getUnreadMessage(roleType: String, isRead: String): LiveData<List<MessageModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: MessageModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertS(message: MessageModel)

    @Query("UPDATE f_message SET is_read=:read WHERE id_message=:id")
    fun updateRead(read: String, id: String)

    @Query("DELETE FROM f_message WHERE id_message=:id")
    fun deleteNotif(id: String)
}