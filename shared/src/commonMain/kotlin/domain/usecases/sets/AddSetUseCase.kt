package domain.usecases.sets

import data.model.sets.AddSetRequest
import data.model.sets.SetResponse
import data.prefs.IDataStoreManager
import data.repository.set.ISetApiRepository
import data.repository.set.ISetDaoRepository
import domain.utils.GuestLimitException
import domain.cache.ISetsCacheProvider
import domain.token.ICheckToken
import domain.token.ITokenRefresher

class AddSetUseCase(
    private val repo: ISetApiRepository,
    private val dao: ISetDaoRepository,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher,
    private val prefs: IDataStoreManager,
    private val checkToken: ICheckToken
) : IAddSetUseCase {
    companion object {
        private const val GUEST_MAX_SETS = 3
    }

    override suspend fun invoke(
        name: String,
        isDefault: Boolean,
        courseId: String,
        listOfWords: List<String>
    ): Result<SetResponse> {
        if (prefs.isGuest()) {
            if (!checkLimit()) {
                return Result.failure(GuestLimitException())
            }
        }
        val request = AddSetRequest(name, isDefault, courseId, listOfWords)
        val response = if (prefs.isGuest() || prefs.isOfflineMode()) {
            dao.addSet(request)
        } else {
            checkToken.safeApiCallWithRefresh(
                call = { repo.addSet(request) },
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
            cache.clear()
            Result.success(data)
        }
    }

    private suspend fun checkLimit(): Boolean {
        val course = prefs.getCourse() ?: return false
        val allWords =
            if (prefs.isGuest() || prefs.isOfflineMode()) dao.getAllSets(course.id).data?.size
                ?: 0 else repo.getAllSets(course.id).data?.size ?: 0
        return allWords < GUEST_MAX_SETS
    }
}