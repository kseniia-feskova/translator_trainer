package com.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date


@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val original: String,
    val translation: String,
    val status: WordStatus,
    val dateAdded: Date = Date() // по умолчанию дата текущая
)


enum class WordStatus {
    NEW,
    IN_PROGRESS,
    IN_GOOD_PROGRESS,
    LEARNED;

    fun inc(): WordStatus {
        return when (this) {
            NEW -> IN_PROGRESS
            IN_PROGRESS -> IN_GOOD_PROGRESS
            IN_GOOD_PROGRESS -> LEARNED
            LEARNED -> LEARNED
        }
    }
}


fun getPreviousDay(): Date {
    val calendar = Calendar.getInstance()  // Получаем текущую дату
    calendar.add(Calendar.DAY_OF_YEAR, -1) // Отнимаем один день
    return calendar.time                   // Получаем дату предыдущего дня
}