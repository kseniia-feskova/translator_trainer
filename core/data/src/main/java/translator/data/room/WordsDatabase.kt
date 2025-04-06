package translator.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import translator.data.model.sets.SetOfWords
import translator.data.model.sets.SetWordCrossRef
import translator.data.model.WordEntity

@Database(
    entities = [WordEntity::class, SetOfWords::class, SetWordCrossRef::class],
    version = 4
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun setsDao(): SetsDao
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {

    @TypeConverter
    fun fromList(list: List<String>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toList(data: String?): List<String> {
        return data?.split(",")?.map { it.trim() } ?: emptyList()
    }
}