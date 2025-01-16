package com.domain.usecase.words

import com.data.model.SetWordCrossRef
import com.data.model.words.AddWordRequest
import com.data.repository.words.api.IWordsApiRepository
import com.data.repository.words.room.IWordsDaoRepository
import com.domain.mapper.toDao
import com.domain.mapper.toNewWordEntity
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.data.IDataStoreManager
import com.presentation.model.WordUI
import com.presentation.usecases.words.IAddWordUseCase

class AddWordUseCase(
    private val repo: IWordsDaoRepository,
    private val apiRepo: IWordsApiRepository,
    private val prefs: IDataStoreManager,
    private val tokenRefresher: ITokenRefresher
) : IAddWordUseCase {

    override suspend fun invoke(
        setId: Int?,
        originalText: String,
        translatedText: String
    ): Result<WordUI> {
        val sourceLanguage = prefs.getOriginalLanguage()
        val targetLanguage = prefs.getResultLanguage()
        val courseId = prefs.getCourseId()
        val allWordsId = prefs.getAllWordsSetId()
        if (sourceLanguage == null || targetLanguage == null || courseId == null || allWordsId == null) {
            return Result.failure(Exception("Prefs are empty. Check them, please"))
        }
        val request = AddWordRequest(
            originalText = originalText,
            translatedText = translatedText,
            sourceLanguage = sourceLanguage.code,
            targetLanguage = targetLanguage.code,
            courseId = courseId
        )
        val response = safeApiCallWithRefresh(
            call = { apiRepo.addWord(request) },
            onTokenExpired = { tokenRefresher.refreshToken() }
        )
        val data = response.data
        return (if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else {
            addWordToDao(setId = setId, newWord = data.toDao())
            Result.success(data.toUI())
        })
    }

    private suspend fun addWordToDao(setId: Int?, newWord: WordUI) {
        //TODO refactor dao
        val wordId = repo.addNewWord(newWord.toNewWordEntity())
        if (wordId != -1L) {
            repo.addWordToAllWordsSet(newWord.id)
            if (setId != null) {
                repo.insertSetWordCrossRef(SetWordCrossRef(setId = setId, wordId = newWord.id))
            }
        }
    }
}