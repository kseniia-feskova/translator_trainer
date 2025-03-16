package com.domain.usecase.words

import com.data.model.words.get.bytranslate.WordByOriginalRequest
import com.data.prefs.IDataStoreManager
import com.data.repository.words.IWordRepository
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.model.WordUI
import com.presentation.usecases.words.IGetWordByOriginal

class GetWordByOriginal(
    private val repo: IWordRepository,
    private val prefs: IDataStoreManager,
    private val tokenRefresher: ITokenRefresher
) : IGetWordByOriginal {
    override suspend fun invoke(original: String): Result<WordUI> {
        val course = prefs.getCourse() ?: return Result.failure(Exception("No course selected"))

        val request = WordByOriginalRequest(
            courseId = course.id,
            original = original
        )

        val response = safeApiCallWithRefresh(
            call = { repo.getWordByOriginal(request) },
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