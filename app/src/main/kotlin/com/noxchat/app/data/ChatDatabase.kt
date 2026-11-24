package com.noxchat.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ChatEntity::class,
        MessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ChatDatabase : RoomDatabase() {

    abstract fun chatDao(): ChatDao

    abstract fun messageDao(): MessageDao

    companion object {

        @Volatile
        private var instance: ChatDatabase? = null

        fun getInstance(context: Context): ChatDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ChatDatabase::class.java,
                    "noxchat_database"
                )
                    .build()
                    .also { database ->
                        instance = database
                    }
            }
        }
    }
}