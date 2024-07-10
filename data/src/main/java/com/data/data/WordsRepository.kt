package com.data.data

import com.data.room.HistoryDAO
import com.data.utils.CustomTranslator
import com.data.utils.Language
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordsRepository(
    private val dao: HistoryDAO,
    private val translator: CustomTranslator
) {

    suspend fun getTranslate(text: String, language: Language): String {
        return translator.translate(text, Language.UKRAINIAN, language).translatedText
    }

    suspend fun addNewWord(newWord: WordEntity) = withContext(Dispatchers.IO) {
        dao.insertAll(newWord)
    }

    fun getLastWord(count: Int): Observable<List<WordEntity>> {
        return dao.getLastWords(count)
    }
}