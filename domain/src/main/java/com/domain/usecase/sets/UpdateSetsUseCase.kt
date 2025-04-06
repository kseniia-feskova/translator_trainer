package com.domain.usecase.sets

import translator.data.prefs.IDataStoreManager
import translator.data.repository.sets.ISetRepository
import com.domain.mapper.toUI
import com.domain.token.ITokenRefresher
import com.domain.token.safeApiCallWithRefresh
import com.presentation.cache.ISetsCacheProvider
import com.presentation.usecases.sets.IUpdateSetsUseCase

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
                call = { repo.getAllSets(course.id) },
                onTokenExpired = { tokenRefresher.refreshToken() })
            val data = response.data
            if (response.errorMsg.isNotEmpty() && data != null) {
                val sets = data.toUI()
                cache.addSets(sets)
            }
        }
    }
}