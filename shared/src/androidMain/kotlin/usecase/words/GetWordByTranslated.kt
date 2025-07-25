package usecase.words

import com.presentation.usecases.words.IGetWordByTranslated
import data.model.words.get.bytranslate.WordByTranslatedRequest
import data.prefs.IDataStoreManager
import data.repository.IWordRepository
import domain.token.ICheckToken
import domain.token.ITokenRefresher
import mapper.toUI
import presentation.model.WordUI

class GetWordByTranslated(
    private val repo: IWordRepository,
    private val prefs: IDataStoreManager,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken
) : IGetWordByTranslated {

    override suspend fun invoke(translated: String): Result<WordUI> {
        val course = prefs.getCourse() ?: return Result.failure(Exception("No course selected"))

        val request = WordByTranslatedRequest(
            courseId = course.id,
            translate = translated
        )

        val response = checkToken.safeApiCallWithRefresh(
            call = { repo.getWordByTranslated(request) },
            onTokenExpired = { tokenRefresher.refreshToken() })

        val data = response.data

        return if (response.errorMsg.isNotEmpty()) {
            if (response.errorMsg.contains("Failed to connect")) {
                Result.failure(Exception("Failed to connect"))
            } else Result.failure(Exception(response.errorMsg))
        } else if (data == null) {
            Result.failure(Exception("Empty user data"))
        } else Result.success(data.toUI())

    }
}