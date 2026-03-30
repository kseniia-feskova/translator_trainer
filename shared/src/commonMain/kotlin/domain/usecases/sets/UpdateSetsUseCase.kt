package domain.usecases.sets

import data.prefs.IDataStoreManager
import data.repository.set.ISetApiRepository
import data.repository.set.ISetDaoRepository
import domain.cache.ISetsCacheProvider
import domain.token.ICheckToken
import domain.token.ITokenRefresher

//load from db or api to cache
class UpdateSetsUseCase(
    private val repo: ISetApiRepository,
    private val dao: ISetDaoRepository,
    private val prefs: IDataStoreManager,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken
) : IUpdateSetsUseCase {
    override suspend fun invoke() {
        val course = prefs.getCourse()
        if (course != null) {
            val response = if (prefs.isGuest() || prefs.isOfflineMode()) {
                dao.getAllSets(course.id)
            } else {
                checkToken.safeApiCallWithRefresh(
                    call = { repo.getAllSets(course.id) },
                    onTokenExpired = { tokenRefresher.refreshToken() })
            }
            val data = response.data
            if (response.errorMsg.isNotEmpty() && data != null) {
                cache.updateSets(data)
            }
        }
    }
}