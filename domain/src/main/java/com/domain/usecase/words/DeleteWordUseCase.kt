package com.domain.usecase.words

import com.data.repository.words.api.IWordsApiRepository
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.cache.ISetsCacheProvider
import com.presentation.usecases.words.IDeleteWordUseCase
import java.util.UUID

class DeleteWordUseCase(
    private val repo: IWordsApiRepository,
    private val tokenRefresher: ITokenRefresher,
    private val cacheProvider: ISetsCacheProvider
) : IDeleteWordUseCase {

    override suspend fun invoke(wordId: UUID): Result<Unit> {
        val response = safeApiCallWithRefresh(
            call = { repo.delete(wordId) },
            onTokenExpired = { tokenRefresher.refreshToken() }
        )
        return if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else {
            cacheProvider.clear()
            Result.success(Unit)
        }
    }
}
