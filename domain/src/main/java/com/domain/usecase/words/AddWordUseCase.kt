package com.domain.usecase.words

import com.data.model.SetWordCrossRef
import com.data.model.words.AddWordRequest
import com.data.repository.words.api.IWordsApiRepository
import com.data.repository.words.room.IWordsDaoRepository
import com.domain.mapper.toNewWordEntity
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.data.IDataStoreManager
import com.presentation.model.WordUI
import com.presentation.usecases.words.IAddWordUseCase
import kotlinx.coroutines.flow.firstOrNull

class AddWordUseCase(
    private val repo: IWordsDaoRepository,
    private val apiRepo: IWordsApiRepository,
    private val prefs: IDataStoreManager,
    private val tokenRefresher: ITokenRefresher
) : IAddWordUseCase {

    override suspend fun invoke(setId: Int?, newWord: WordUI): Result<WordUI> {
        return addWordToApi(setId, newWord)
    }

    private suspend fun addWordToApi(setId: Int?, newWord: WordUI): Result<WordUI> {
        val sourceLanguage = prefs.getOriginalLanguage()
        val targetLanguage = prefs.getResultLanguage()
        val userId = prefs.listenUserId().firstOrNull()
        if (sourceLanguage == null || targetLanguage == null || userId == null) {
            return Result.failure(Exception("Prefs are empty. Check them, please"))
        }
        val request = AddWordRequest(
            originalText = newWord.originalText,
            translatedText = newWord.resText,
            sourceLanguage = sourceLanguage.code,
            targetLanguage = targetLanguage.code,
            userId = userId
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
            addWordToDao(setId = setId, newWord = newWord)
            Result.success(data.toUI())
        })
    }

    private suspend fun addWordToDao(setId: Int?, newWord: WordUI) {
        val wordId = repo.addNewWord(newWord.toNewWordEntity())
        if (wordId != -1L) {
            repo.addWordToAllWordsSet(newWord.id)
            if (setId != null) {
                repo.insertSetWordCrossRef(
                    SetWordCrossRef(setId = setId, wordId = newWord.id)
                )
            }
        }
    }
}