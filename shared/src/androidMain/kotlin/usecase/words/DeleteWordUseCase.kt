package usecase.words

import android.util.Log
import data.prefs.IDataStoreManager
import data.repository.word.IWordApiRepository
import data.repository.word.IWordDaoRepository
import domain.cache.ISetsCacheProvider
import domain.token.ICheckToken
import domain.token.ITokenRefresher
import presentation.usecases.words.IDeleteWordUseCase

class DeleteWordUseCase(
    private val repo: IWordApiRepository,
    private val repoDao: IWordDaoRepository,
    private val tokenRefresher: ITokenRefresher,
    private val cacheProvider: ISetsCacheProvider,
    private val checkToken: ICheckToken,
    private val prefs: IDataStoreManager,
) : IDeleteWordUseCase {

    override suspend fun invoke(wordId: String): Result<Unit> {
        if (prefs.isGuest()) {
            val deleteError = repoDao.delete(wordId).errorMsg
            return if (deleteError.isEmpty()) {
                cacheProvider.clear()
                Result.success(Unit)
            } else {
                Result.failure(Exception(deleteError))
            }
        } else {
            val deleteError = repoDao.delete(wordId).errorMsg
            Log.e("DeleteWordUseCase", "delete from dao error = $deleteError")
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
}
