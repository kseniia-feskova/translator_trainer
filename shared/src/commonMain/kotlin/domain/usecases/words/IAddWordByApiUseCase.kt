package domain.usecases.words

import data.model.words.WordResponse

interface IAddWordByApiUseCase {

    suspend fun invoke(
        originalText: String,
        translatedText: String
    ): Result<WordResponse>

    suspend fun invokeOffline(
        originalText: String,
        translatedText: String
    ): Result<WordResponse>
}