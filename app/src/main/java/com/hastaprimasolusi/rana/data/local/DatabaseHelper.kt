package com.hastaprimasolusi.rana.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hastaprimasolusi.rana.data.MessageDao

/**
 * Created By maasrahman on 6/22/20
 */
@Database(entities = [MessageModel::class], version = 1, exportSchema = false)
abstract class DatabaseHelper : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseHelper? = null

        fun getDatabase(context: Context): DatabaseHelper {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseHelper::class.java,
                    "dmlt.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}