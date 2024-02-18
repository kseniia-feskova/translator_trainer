package com.example.translatortrainer.data

import com.example.translatortrainer.room.HistoryDAO
import com.example.translatortrainer.utils.CustomTranslator
import com.example.translatortrainer.utils.Language
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(
    private val dao: HistoryDAO,
    private val translator: CustomTranslator
) {

    suspend fun getTranslate(text: String, language: Language): String {
        return translator.translate(text, Language.RUSSIAN, language).translatedText
    }

    suspend fun addNewWord(newWord: WordEntity) = withContext(Dispatchers.IO) {
        dao.insertAll(newWord)
    }

    fun getLastWord(count: Int): Observable<List<WordEntity>> {
        return dao.getLastWords(count)
    }
}