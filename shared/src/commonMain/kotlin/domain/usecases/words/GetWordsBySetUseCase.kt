package domain.usecases.words

import data.model.words.WordResponse
import data.prefs.IDataStoreManager
import data.repository.word.IWordApiRepository
import data.repository.word.IWordDaoRepository
import domain.token.ICheckToken
import domain.token.ITokenRefresher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetWordsBySetUseCase(
    private val repo: IWordApiRepository,
    private val dao: IWordDaoRepository,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken,
    private val prefs: IDataStoreManager
) : IGetWordsBySetUseCase {

    override suspend fun invoke(setId: String): Result<List<WordResponse>> {
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
        } else Result.success(data)

    }

    override fun invokeFlow(setId: String): Flow<Result<List<WordResponse>>> = flow {
        val response = if (!prefs.isOfflineMode() && !prefs.isGuest()) {
            checkToken.safeApiCallWithRefresh(
                call = { repo.getWordsBySet(setId) },
                onTokenExpired = { tokenRefresher.refreshToken() }
            )
        } else {
            dao.getWordsBySetFlow(setId).first()
        }
        emitAll(dao.getWordsBySetFlow(setId).map { result ->
            if (result.data != null) {
                Result.success(result.data)
            } else {
                if (response.errorMsg.contains("Failed to connect")) {
                    Result.failure(Exception("Failed to connect"))
                } else Result.failure(Exception(response.errorMsg))
            }
        })
    }
}