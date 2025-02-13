package com.domain.usecase.sets

import com.data.repository.sets.ISetRepository
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.cache.ISetsCacheProvider
import com.presentation.model.SetOfCards
import com.presentation.usecases.sets.IGetAllSetsUseCase
import java.util.UUID

class GetAllSetsUseCase(
    private val repo: ISetRepository,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher
) : IGetAllSetsUseCase {
    override suspend fun invoke(courseId: UUID): Result<List<SetOfCards>> {
        val cached = cache.getSets()
        if (cached != null) {
            return Result.success(cached)
        } else {
            val response = safeApiCallWithRefresh(
                call = { repo.getAllSets(courseId) },
                onTokenExpired = { tokenRefresher.refreshToken() })
            val data = response.data
            return if (response.errorMsg.isNotEmpty()) {
                if (response.errorMsg.contains("Failed to connect")) {
                    Result.failure(Exception("Failed to connect"))
                } else Result.failure(Exception(response.errorMsg))
            } else if (data == null) {
                Result.failure(Exception("Empty user data"))
            } else {
                val sets = data.toUI()
                cache.addSets(sets)
                Result.success(sets)
            }
        }
    }

}