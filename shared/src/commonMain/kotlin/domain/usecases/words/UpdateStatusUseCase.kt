package domain.usecases.words

import data.model.words.WordResponse
import data.model.words.WordStatus
import data.model.words.update.UpdateWordStatusRequest
import data.prefs.IDataStoreManager
import data.repository.word.IWordApiRepository
import data.repository.word.IWordDaoRepository
import domain.cache.ISetsCacheProvider
import domain.token.ICheckToken
import domain.token.ITokenRefresher

class UpdateStatusUseCase(
    private val repo: IWordApiRepository,
    private val dao: IWordDaoRepository,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken,
    private val prefs: IDataStoreManager,
    private val cache: ISetsCacheProvider
) : IUpdateStatusUseCase {

    override suspend fun invoke(wordId: String, level: WordStatus): Result<WordResponse> {

        val response = if (prefs.isGuest() || prefs.isOfflineMode()) {
            dao.updateStatus(wordId, UpdateWordStatusRequest(level))
        } else {
            checkToken.safeApiCallWithRefresh(
                call = { repo.updateStatus(wordId, UpdateWordStatusRequest(level)) },
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
            cache.clear() //clear cache for reload on the sets screen
            Result.success(data)
        }
    }

}