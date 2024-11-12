package com.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.data.model.SetOfWords
import com.data.model.SetWordCrossRef
import com.data.model.WordEntity
import java.util.Date


@Database(
    entities = [WordEntity::class, SetOfWords::class, SetWordCrossRef::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date {
        return if(value == null){
            Date()
        } else {
            Date(value)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date = Date()): Long {
        return date.time
    }
}