package com.domain.usecase.words

import com.data.model.words.get.bytranslate.WordByTranslatedRequest
import com.data.repository.words.api.IWordsApiRepository
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.data.IDataStoreManager
import com.presentation.model.WordUI
import com.presentation.usecases.words.IGetWordByTranslated
import java.util.UUID

class GetWordByTranslated(
    private val repo: IWordsApiRepository,
    private val prefs: IDataStoreManager,
    private val tokenRefresher: ITokenRefresher
) : IGetWordByTranslated {

    override suspend fun invoke(translated: String): Result<WordUI> {
        val course = prefs.getCourse() ?: return Result.failure(Exception("No course selected"))

        val request = WordByTranslatedRequest(
            courseId = UUID.fromString(course.id),
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