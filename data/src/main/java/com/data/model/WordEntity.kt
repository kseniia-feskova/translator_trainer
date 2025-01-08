package com.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date
import java.util.UUID


@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val original: String,
    val translation: String,
    val status: WordStatus,
    val dateAdded: Date = Date() // по умолчанию дата текущая
)


enum class WordStatus {
    New, Learning, GoodLearning, Known;

    fun inc(): WordStatus {
        return when (this) {
            New -> Learning
            Learning -> GoodLearning
            GoodLearning -> Known
            Known -> Known
        }
    }
}


fun getPreviousDay(): Date {
    val calendar = Calendar.getInstance()  // Получаем текущую дату
    calendar.add(Calendar.DAY_OF_YEAR, -1) // Отнимаем один день
    return calendar.time                   // Получаем дату предыдущего дня
}