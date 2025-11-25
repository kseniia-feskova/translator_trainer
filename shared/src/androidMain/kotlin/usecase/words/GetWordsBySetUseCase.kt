package usecase.words

import data.prefs.IDataStoreManager
import domain.token.ITokenRefresher
import presentation.usecases.words.IGetWordsBySetUseCase
import data.repository.word.IWordApiRepository
import data.repository.word.IWordDaoRepository
import domain.token.ICheckToken
import mapper.toUI
import presentation.model.WordUI

class GetWordsBySetUseCase(
    private val repo: IWordApiRepository,
    private val dao: IWordDaoRepository,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken,
    private val prefs: IDataStoreManager
) : IGetWordsBySetUseCase {

    override suspend fun invoke(setId: String): Result<List<WordUI>> {
        val response = if (prefs.isGuest() || prefs.isOfflineMode()) {
            dao.getWordsBySet(setId)
        } else {
            checkToken.safeApiCallWithRefresh(
                call = { repo.getWordsBySet(setId) },
                onTokenExpired = { tokenRefresher.refreshToken() })
        }
        val data = response.data
        return if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data.map { it.toUI() })

    }
}