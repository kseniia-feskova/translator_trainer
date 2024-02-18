package com.example.translatortrainer.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WordEntity(
    @ColumnInfo(name = "number")
    var number: Int? = -1,

    @ColumnInfo(name = "word")
    var word: String? = "",

    @ColumnInfo(name = "translation")
    var translation: String? = "",

    @ColumnInfo(name = "language")
    val language: String? = "",

    @ColumnInfo(name = "status")
    var status: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)
