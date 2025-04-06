package com.domain.usecase.words

import translator.data.model.words.get.bytranslate.WordByTranslatedRequest
import translator.data.prefs.IDataStoreManager
import translator.data.repository.words.IWordRepository
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.model.WordUI
import com.presentation.usecases.words.IGetWordByTranslated

class GetWordByTranslated(
    private val repo: IWordRepository,
    private val prefs: IDataStoreManager,
    private val tokenRefresher: ITokenRefresher
) : IGetWordByTranslated {

    override suspend fun invoke(translated: String): Result<WordUI> {
        val course = prefs.getCourse() ?: return Result.failure(Exception("No course selected"))

        val request = WordByTranslatedRequest(
            courseId = course.id,
            translate = translated
        )

        val response = safeApiCallWithRefresh(
            call = { repo.getWordByTranslated(request) },
            onTokenExpired = { tokenRefresher.refreshToken() })

        val data = response.data

        return if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data.toUI())

    }
}