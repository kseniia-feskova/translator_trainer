package com.domain.usecase.sets

import com.data.repository.sets.ISetRepository
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.cache.ISetsCacheProvider
import com.presentation.data.IDataStoreManager
import com.presentation.usecases.sets.IUpdateSetsUseCase
import java.util.UUID

class UpdateSetsUseCase(
    private val repo: ISetRepository,
    private val prefs: IDataStoreManager,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher
) : IUpdateSetsUseCase {
    override suspend fun invoke() {
        val course = prefs.getCourse()
        if (course != null) {
            val response = safeApiCallWithRefresh(
                call = { repo.getAllSets(UUID.fromString(course.id)) },
                onTokenExpired = { tokenRefresher.refreshToken() })
            val data = response.data
            if (response.errorMsg.isNotEmpty() && data != null) {
                val sets = data.toUI()
                cache.addSets(sets)
            }
        }
    }
}