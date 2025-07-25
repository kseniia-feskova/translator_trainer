package usecase.sets

import com.presentation.usecases.sets.IUpdateSetsUseCase
import data.prefs.IDataStoreManager
import data.repository.ISetRepository
import domain.cache.ISetsCacheProvider
import domain.token.ICheckToken
import domain.token.ITokenRefresher

class UpdateSetsUseCase(
    private val repo: ISetRepository,
    private val prefs: IDataStoreManager,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken
) : IUpdateSetsUseCase {
    override suspend fun invoke() {
        val course = prefs.getCourse()
        if (course != null) {
            val response = checkToken.safeApiCallWithRefresh(
                call = { repo.getAllSets(course.id) },
                onTokenExpired = { tokenRefresher.refreshToken() })
            val data = response.data
            if (response.errorMsg.isNotEmpty() && data != null) {
                cache.addSets(data)
            }
        }
    }
}