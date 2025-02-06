package com.domain.usecase.sets

import com.data.model.sets.AddSetRequest
import com.data.repository.sets.ISetRepository
import com.domain.mapper.toUIWithoutWords
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.model.SetOfCards
import com.presentation.usecases.sets.IAddSetUseCase
import java.util.UUID

class AddSetUseCase(
    val repo: ISetRepository,
    val tokenRefresher: ITokenRefresher
) : IAddSetUseCase {
    override suspend fun invoke(
        name: String,
        isDefault: Boolean,
        courseId: UUID,
        listOfWords: List<UUID>
    ): Result<SetOfCards> {
        val request = AddSetRequest(name, isDefault, courseId, listOfWords)
        val response = safeApiCallWithRefresh(
            call = { repo.addSet(request) },
            onTokenExpired = { tokenRefresher.refreshToken() })
        val data = response.data
        return if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data.toUIWithoutWords())
    }
}