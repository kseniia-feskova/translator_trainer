package com.domain.usecase.words

import com.data.model.words.update.UpdateWordStatusRequest
import com.data.repository.words.api.IWordsApiRepository
import com.domain.mapper.toStatus
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.cache.ISetsCacheProvider
import com.presentation.model.Level
import com.presentation.model.WordUI
import com.presentation.usecases.words.IUpdateStatusUseCase
import java.util.UUID

class UpdateStatusUseCase(
    private val repo: IWordsApiRepository,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher
) : IUpdateStatusUseCase {

    override suspend fun invoke(wordId: UUID, level: Level): Result<WordUI> {

        val response = safeApiCallWithRefresh(
            call = { repo.updateStatus(wordId, UpdateWordStatusRequest(level.toStatus())) },
            onTokenExpired = { tokenRefresher.refreshToken() })
        val data = response.data

        return if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else {
            cache.addSets(null)
            Result.success(data.toUI())
        }
    }

}