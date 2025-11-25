package usecase.words

import data.repository.IWordApiRepository
import domain.cache.ISetsCacheProvider
import domain.token.ICheckToken
import domain.token.ITokenRefresher
import presentation.usecases.words.IDeleteWordUseCase

class DeleteWordUseCase(
    private val repo: IWordApiRepository,
    private val tokenRefresher: ITokenRefresher,
    private val cacheProvider: ISetsCacheProvider,
    private val checkToken:ICheckToken
) : IDeleteWordUseCase {

    override suspend fun invoke(wordId: String): Result<Unit> {
        val response = checkToken.safeApiCallWithRefresh(
            call = { repo.delete(wordId) },
            onTokenExpired = { tokenRefresher.refreshToken() }
        )
        return if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else {
            cacheProvider.clear()
            Result.success(Unit)
        }
    }
}
