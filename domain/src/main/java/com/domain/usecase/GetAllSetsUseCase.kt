package com.domain.usecase

import com.data.repository.sets.ISetRepository
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.model.SetOfCards
import com.presentation.usecases.IGetAllSetsUseCase
import java.util.UUID

class GetAllSetsUseCase(
    private val repo: ISetRepository,
    private val tokenRefresher: ITokenRefresher
) : IGetAllSetsUseCase {
    override suspend fun invoke(courseId: UUID): Result<List<SetOfCards>> {
        val response = safeApiCallWithRefresh(
            call = { repo.getAllSets(courseId) },
            onTokenExpired = { tokenRefresher.refreshToken() })
        val data = response.data
        return if (response.errorMsg.isNotEmpty()) {
            Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data.toUI())
    }

}