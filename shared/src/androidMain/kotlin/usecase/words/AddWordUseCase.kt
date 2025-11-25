package usecase.words

import domain.GuestLimitException
import presentation.usecases.words.IAddWordUseCase
import presentation.usecases.words.IGetWordByOriginal
import presentation.usecases.words.IGetWordByTranslated
import presentation.utils.GUEST_MAX_WORDS
import data.model.words.add.AddWordRequest
import data.prefs.IDataStoreManager
import data.repository.word.IWordDaoRepository
import data.repository.word.IWordApiRepository
import domain.cache.ISetsCacheProvider
import domain.token.ICheckToken
import domain.token.ITokenRefresher
import mapper.toUI
import presentation.model.WordUI

class AddWordUseCase(
    private val apiRepo: IWordApiRepository,
    private val dao: IWordDaoRepository,
    private val prefs: IDataStoreManager,
    private val cache: ISetsCacheProvider,
    private val tokenRefresher: ITokenRefresher,
    private val findWordByOrigin: IGetWordByOriginal,
    private val findWordByTranslate: IGetWordByTranslated,
    private val checkToken: ICheckToken
) : IAddWordUseCase {

    override suspend fun invoke(
        originalText: String,
        translatedText: String
    ): Result<WordUI> {
        if (prefs.isGuest()) {
            if (!checkLimit()) {
                return Result.failure(GuestLimitException())
            }
        }
        val wordInDB = findWordByOrigin.invoke(originalText)
        if (wordInDB.isSuccess) {
            wordInDB.getOrNull()?.apply {
                return Result.success(this)
            }
        }

        val wordInDBByTranslated = findWordByTranslate.invoke(translatedText)
        if (wordInDBByTranslated.isSuccess) {
            wordInDB.getOrNull()?.apply {
                return Result.success(this)
            }
        }

        val course = prefs.getCourse()
            ?: return Result.failure(Exception("Prefs are empty. Check them, please"))
        val request = AddWordRequest(
            originalText = originalText,
            translatedText = translatedText,
            sourceLanguage = course.sourceLanguage,
            targetLanguage = course.targetLanguage,
            courseId = course.id
        )
        val response = if (prefs.isGuest() || prefs.isOfflineMode()) {
            dao.addWord(request)
        } else {
            checkToken.safeApiCallWithRefresh(
                call = { apiRepo.addWord(request) },
                onTokenExpired = { tokenRefresher.refreshToken() }
            )
        }
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

    private suspend fun checkLimit(): Boolean {
        val allWords = apiRepo.getAllWords().data?.size ?: 0
        return allWords < GUEST_MAX_WORDS
    }
}