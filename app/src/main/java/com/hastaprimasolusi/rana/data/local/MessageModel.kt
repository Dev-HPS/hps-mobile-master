package com.hastaprimasolusi.rana.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created By maasrahman on 6/22/20
 */
@Entity(tableName = "f_message")
class MessageModel (
    @PrimaryKey
    @ColumnInfo(name = "id_message")
    val id: String,
    @ColumnInfo(name ="code")
    val code: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "message")
    val message: String,
    @ColumnInfo(name = "is_read")
    val isRead: String,
    @ColumnInfo(name = "role")
    val role: String,
    @ColumnInfo(name = "created_at")
    val createdAt: String
)