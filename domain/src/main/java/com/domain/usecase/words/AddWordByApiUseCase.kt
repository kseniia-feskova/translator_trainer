package com.domain.usecase.words

import com.data.model.words.add.AddWordRequest
import com.data.prefs.IDataStoreManager
import com.data.repository.words.api.IWordApiRepository
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.cache.ISetsCacheProvider
import com.presentation.model.WordUI
import com.presentation.usecases.words.IAddWordByApiUseCase

class AddWordByApiUseCase(
    private val apiRepo: IWordApiRepository,
    private val prefs: IDataStoreManager,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher,
) : IAddWordByApiUseCase {

    override suspend fun invoke(originalText: String, translatedText: String): Result<WordUI> {
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