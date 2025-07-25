package usecase.sets

import domain.GuestLimitException
import com.presentation.utils.GUEST_MAX_SETS
import data.model.sets.AddSetRequest
import data.prefs.IDataStoreManager
import data.repository.ISetRepository
import domain.cache.ISetsCacheProvider
import domain.token.ICheckToken
import domain.token.ITokenRefresher
import mapper.toUIWithoutWords
import presentation.model.SetOfCards
import presentation.usecases.sets.IAddSetUseCase

class AddSetUseCase(
    private val repo: ISetRepository,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher,
    private val prefs: IDataStoreManager,
    private val checkToken: ICheckToken
) : IAddSetUseCase {

    override suspend fun invoke(
        name: String,
        isDefault: Boolean,
        courseId: String,
        listOfWords: List<String>
    ): Result<SetOfCards> {
        if (prefs.isGuest()) {
            if (!checkLimit()) {
                return Result.failure(GuestLimitException())
            }
        }
        val request = AddSetRequest(name, isDefault, courseId, listOfWords)
        val response = checkToken.safeApiCallWithRefresh(
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