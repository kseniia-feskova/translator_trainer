package com.data.data

import com.data.room.HistoryDAO
import com.data.translate.CustomTranslator
import com.data.translate.Language
import com.data.translate.Translation
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordsRepository(
    private val dao: HistoryDAO,
    private val translator: CustomTranslator
) {

    suspend fun getTranslate(text: String, language: Language): Translation {
        return translator.translate(text, Language.UKRAINIAN, language)
    }

    suspend fun addNewWord(newWord: WordEntity) = withContext(Dispatchers.IO) {
        dao.insertAll(newWord)
    }

    fun getLastWord(count: Int): Observable<List<WordEntity>> {
        return dao.getLastWords(count)
    }
}