package usecase.sets

import data.prefs.IDataStoreManager
import data.repository.set.ISetApiRepository
import data.repository.set.ISetDaoRepository
import domain.cache.ISetsCacheProvider
import domain.token.ICheckToken
import domain.token.ITokenRefresher
import mapper.toUI
import presentation.model.SetOfCards
import presentation.usecases.sets.IGetAllSetsUseCase

class GetAllSetsUseCase(
    private val repo: ISetApiRepository,
    private val dao: ISetDaoRepository,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken,
    private val prefs: IDataStoreManager
) : IGetAllSetsUseCase {

    override suspend fun invoke(courseId: String): Result<List<SetOfCards>> {
        val cached = cache.getSets()?.map { it.toUI() }
        if (cached != null) {
            return Result.success(cached)
        } else {
            val response = if (prefs.isGuest() || prefs.isOfflineMode()) {
                dao.getAllSets(courseId)
            } else {
                checkToken.safeApiCallWithRefresh(
                    call = { repo.getAllSets(courseId) },
                    onTokenExpired = { tokenRefresher.refreshToken() })
            }
            val data = response.data
            return if (response.errorMsg.isNotEmpty()) {
                if (response.errorMsg.contains("Failed to connect")) {
                    Result.failure(Exception("Failed to connect"))
                } else Result.failure(Exception(response.errorMsg))
            } else if (data == null) {
                Result.failure(Exception("Empty user data"))
            } else {
                cache.updateSets(data)
                Result.success(data.map { it.toUI() })
            }
        }
    }

}