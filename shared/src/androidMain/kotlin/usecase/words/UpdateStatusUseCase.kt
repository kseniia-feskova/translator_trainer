package usecase.words

import data.model.words.update.UpdateWordStatusRequest
import data.prefs.IDataStoreManager
import data.repository.word.IWordApiRepository
import data.repository.word.IWordDaoRepository
import domain.token.ICheckToken
import domain.token.ITokenRefresher
import mapper.toStatus
import mapper.toUI
import presentation.model.Level
import presentation.model.WordUI
import presentation.usecases.words.IUpdateStatusUseCase

class UpdateStatusUseCase(
    private val repo: IWordApiRepository,
    private val dao: IWordDaoRepository,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken,
    private val prefs: IDataStoreManager
) : IUpdateStatusUseCase {

    override suspend fun invoke(wordId: String, level: Level): Result<WordUI> {

        val response = if (prefs.isGuest() || prefs.isOfflineMode()) {
            dao.updateStatus(wordId, UpdateWordStatusRequest(level.toStatus()))
        } else {
            checkToken.safeApiCallWithRefresh(
                call = { repo.updateStatus(wordId, UpdateWordStatusRequest(level.toStatus())) },
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
            Result.success(data.toUI())
        }
    }

}