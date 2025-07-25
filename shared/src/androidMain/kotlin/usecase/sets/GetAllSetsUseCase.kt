package usecase.sets

import data.repository.ISetRepository
import domain.cache.ISetsCacheProvider
import domain.token.ICheckToken
import domain.token.ITokenRefresher
import mapper.toUI
import presentation.model.SetOfCards
import presentation.usecases.sets.IGetAllSetsUseCase

class GetAllSetsUseCase(
    private val repo: ISetRepository,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken
) : IGetAllSetsUseCase {

    override suspend fun invoke(courseId: String): Result<List<SetOfCards>> {
        val cached = cache.getSets()?.map { it.toUI() }
        if (cached != null) {
            return Result.success(cached)
        } else {
            val response = checkToken.safeApiCallWithRefresh(
                call = { repo.getAllSets(courseId) },
                onTokenExpired = { tokenRefresher.refreshToken() })
            val data = response.data
            return if (response.errorMsg.isNotEmpty()) {
                if (response.errorMsg.contains("Failed to connect")) {
                    Result.failure(Exception("Failed to connect"))
                } else Result.failure(Exception(response.errorMsg))
            } else if (data == null) {
                Result.failure(Exception("Empty user data"))
            } else {
                cache.addSets(data)
                Result.success(data.map { it.toUI() })
            }
        }
    }

}