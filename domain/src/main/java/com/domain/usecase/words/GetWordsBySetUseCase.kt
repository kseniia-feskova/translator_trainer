package com.domain.usecase.words

import com.data.repository.words.api.IWordsApiRepository
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.model.WordUI
import com.presentation.usecases.words.IGetWordsBySetUseCase
import java.util.UUID

class GetWordsBySetUseCase(
    private val repo: IWordsApiRepository,
    private val tokenRefresher: ITokenRefresher
) : IGetWordsBySetUseCase {
    override suspend fun invoke(setId: UUID): Result<List<WordUI>> {
        val response = safeApiCallWithRefresh(
            call = { repo.getWordsBySet(setId) },
            onTokenExpired = { tokenRefresher.refreshToken() })
        val data = response.data
        return if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data.map { it.toUI() })
    }
}