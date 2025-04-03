package com.domain.usecase.sets

import com.data.model.sets.AddSetRequest
import com.data.prefs.IDataStoreManager
import com.data.repository.sets.ISetRepository
import com.domain.GuestLimitException
import com.domain.mapper.toUIWithoutWords
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.cache.ISetsCacheProvider
import com.presentation.model.SetOfCards
import com.presentation.usecases.sets.IAddSetUseCase
import com.presentation.utils.GUEST_MAX_SETS
import java.util.UUID

class AddSetUseCase(
    private val repo: ISetRepository,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher,
    private val prefs: IDataStoreManager,
) : IAddSetUseCase {
    override suspend fun invoke(
        name: String,
        isDefault: Boolean,
        courseId: UUID,
        listOfWords: List<UUID>
    ): Result<SetOfCards> {
        if (prefs.isGuest()) {
            if (!checkLimit()) {
                return Result.failure(GuestLimitException())
            }
        }
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
        } else {
            cache.clear()
            Result.success(data.toUIWithoutWords())
        }
    }

    private suspend fun checkLimit(): Boolean {
        val course = prefs.getCourse() ?: return false
        val allWords = repo.getAllSets(course.id).data?.size ?: 0
        return allWords < GUEST_MAX_SETS
    }
}