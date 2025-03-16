package com.domain.usecase.words

import com.data.model.words.add.AddWordRequest
import com.data.prefs.IDataStoreManager
import com.data.repository.words.IWordRepository
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.cache.ISetsCacheProvider
import com.presentation.model.WordUI
import com.presentation.usecases.words.IAddWordUseCase
import com.presentation.usecases.words.IGetWordByOriginal
import com.presentation.usecases.words.IGetWordByTranslated

class AddWordUseCase(
    private val apiRepo: IWordRepository,
    private val prefs: IDataStoreManager,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher,
    private val findWordByOrigin: IGetWordByOriginal,
    private val findWordByTranslate: IGetWordByTranslated,
) : IAddWordUseCase {

    override suspend fun invoke(
        originalText: String,
        translatedText: String
    ): Result<WordUI> {
        val wordInDB = findWordByOrigin.invoke(originalText)
        if (wordInDB.isSuccess) {
            wordInDB.getOrNull()?.apply {
                return Result.success(this)
            }
        }

        val wordInDBByTranslated = findWordByTranslate.invoke(translatedText)
        if (wordInDBByTranslated.isSuccess) {
            wordInDB.getOrNull()?.apply {
                return Result.success(this)
            }
        }

        val course = prefs.getCourse()
            ?: return Result.failure(Exception("Prefs are empty. Check them, please"))
        val request = AddWordRequest(
            originalText = originalText,
            translatedText = translatedText,
            sourceLanguage = course.sourceLanguage,
            targetLanguage = course.targetLanguage,
            courseId = course.id
        )
        val response = safeApiCallWithRefresh(
            call = { apiRepo.addWord(request) },
            onTokenExpired = { tokenRefresher.refreshToken() }
        )
        val data = response.data
        return (if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else {
            cache.clear()
            Result.success(data.toUI())
        })
    }
}