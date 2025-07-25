package usecase.words

import domain.token.ITokenRefresher
import presentation.usecases.words.IGetWordsBySetUseCase
import data.repository.IWordRepository
import domain.token.ICheckToken
import mapper.toUI
import presentation.model.WordUI

class GetWordsBySetUseCase(
    private val repo: IWordRepository,
    private val tokenRefresher: ITokenRefresher,
    private val checkToken: ICheckToken
) : IGetWordsBySetUseCase {

    override suspend fun invoke(setId: String): Result<List<WordUI>> {

        val response = checkToken.safeApiCallWithRefresh(
            call = { repo.getWordsBySet(setId) },
            onTokenExpired = { tokenRefresher.refreshToken() })
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