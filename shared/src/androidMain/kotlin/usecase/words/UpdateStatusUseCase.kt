package usecase.words

import data.model.words.update.UpdateWordStatusRequest
import data.repository.IWordApiRepository
import domain.cache.ISetsCacheProvider
import domain.token.ICheckToken
import domain.token.ITokenRefresher
import mapper.toStatus
import mapper.toUI
import presentation.model.Level
import presentation.model.WordUI
import presentation.usecases.words.IUpdateStatusUseCase

class UpdateStatusUseCase(
    private val repo: IWordApiRepository,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken:ICheckToken
) : IUpdateStatusUseCase {

    override suspend fun invoke(wordId: String, level: Level): Result<WordUI> {

        val response = checkToken.safeApiCallWithRefresh(
            call = { repo.updateStatus(wordId, UpdateWordStatusRequest(level.toStatus())) },
            onTokenExpired = { tokenRefresher.refreshToken() })
        val data = response.data

        return if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else {
            cache.addSets(null)
            Result.success(data.toUI())
        }
    }

}