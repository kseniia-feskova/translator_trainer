package com.example.translatortrainer.data

import androidx.recyclerview.widget.DiffUtil
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

object WordDiffCallback : DiffUtil.ItemCallback<WordEntity>() {
    override fun areItemsTheSame(oldItem: WordEntity, newItem: WordEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WordEntity, newItem: WordEntity): Boolean {
        return oldItem == newItem
    }
}
