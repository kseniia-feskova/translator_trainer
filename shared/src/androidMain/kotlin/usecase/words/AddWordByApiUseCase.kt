package usecase.words

import com.presentation.usecases.words.IAddWordByApiUseCase
import data.model.words.add.AddWordRequest
import data.prefs.IDataStoreManager
import data.repository.IWordApiRepository
import domain.cache.ISetsCacheProvider
import domain.token.ICheckToken
import domain.token.ITokenRefresher
import mapper.toUI
import presentation.model.WordUI

class AddWordByApiUseCase(
    private val apiRepo: IWordApiRepository,
    private val prefs: IDataStoreManager,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken,
) : IAddWordByApiUseCase {

    override suspend fun invoke(originalText: String, translatedText: String): Result<WordUI> {
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
            Result.success(data.toUI())
        })
    }
}