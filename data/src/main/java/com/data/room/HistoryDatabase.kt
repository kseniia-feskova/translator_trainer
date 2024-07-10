package com.example.translatortrainer.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.data.room.HistoryDAO
import com.data.data.WordEntity

@Database(entities = [WordEntity::class], version = 2)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDAO

    companion object {

        private var instance: HistoryDatabase? = null

        @Synchronized
        fun getInstance(context: Context): HistoryDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java,
                    "history_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}