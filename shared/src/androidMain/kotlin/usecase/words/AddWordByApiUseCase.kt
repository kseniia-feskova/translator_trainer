package usecase.words

import data.model.words.WordResponse
import data.model.words.add.AddWordRequest
import data.prefs.IDataStoreManager
import data.repository.word.IWordApiRepository
import data.repository.word.IWordDaoRepository
import domain.cache.ISetsCacheProvider
import domain.token.ICheckToken
import domain.token.ITokenRefresher
import domain.usecases.words.IAddWordByApiUseCase

class AddWordByApiUseCase(
    private val apiRepo: IWordApiRepository,
    private val daoRepo: IWordDaoRepository,
    private val prefs: IDataStoreManager,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken,
) : IAddWordByApiUseCase {

    override suspend fun invokeOffline(
        originalText: String,
        translatedText: String
    ): Result<WordResponse> {
        val course = prefs.getCourse() ?: return Result.failure(Exception("Prefs are empty. Check them, please"))
        val request = AddWordRequest(
            originalText = originalText,
            translatedText = translatedText,
            sourceLanguage = course.sourceLanguage,
            targetLanguage = course.targetLanguage,
            courseId = course.id
        )
        val response = daoRepo.addWord(request, course.selectedSetId)
        val data = response.data
        return (if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else {
            cache.clear()
            Result.success(data)
        })
    }

    override suspend fun invoke(originalText: String, translatedText: String): Result<WordResponse> {
        val course = prefs.getCourse()
            ?: return Result.failure(Exception("Prefs are empty. Check them, please"))
        val request = AddWordRequest(
            originalText = originalText,
            translatedText = translatedText,
            sourceLanguage = course.sourceLanguage,
            targetLanguage = course.targetLanguage,
            courseId = course.id
        )
        val response = checkToken.safeApiCallWithRefresh(
            call = { apiRepo.addWord(request) },
            onTokenExpired = { tokenRefresher.refreshToken() }
        )
        val data = response.data
        return (if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else {
            cache.clear()
            Result.success(data)
        })
    }
}