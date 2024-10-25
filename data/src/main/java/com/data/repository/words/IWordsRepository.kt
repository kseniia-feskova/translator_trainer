package com.data.repository.words

import com.data.model.WordEntity
import io.reactivex.Observable

interface IWordsRepository {

    suspend fun addNewWord(newWord: WordEntity)

    fun getLastWord(count: Int): Observable<List<WordEntity>>

    suspend fun deleteWord(word: WordEntity)

    suspend fun deleteById(id: Int)
}