package com.domain.usecase.words

import com.data.model.words.get.bytranslate.WordByOriginalRequest
import com.data.repository.words.api.IWordsApiRepository
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.data.IDataStoreManager
import com.presentation.model.WordUI
import com.presentation.usecases.words.IGetWordByOriginal
import java.util.UUID

class GetWordByOriginal(
    private val repo: IWordsApiRepository,
    private val prefs: IDataStoreManager,
    private val tokenRefresher: ITokenRefresher
) : IGetWordByOriginal {
    override suspend fun invoke(original: String): Result<WordUI> {
        val course = prefs.getCourse() ?: return Result.failure(Exception("No course selected"))

        val request = WordByOriginalRequest(
            courseId = UUID.fromString(course.id),
            original = original
        )

        val response = safeApiCallWithRefresh(
            call = { repo.getWordByOriginal(request) },
            onTokenExpired = { tokenRefresher.refreshToken() })

        val data = response.data

        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data.toUI())
    }
}