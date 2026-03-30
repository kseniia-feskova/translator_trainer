package domain.usecases.words

import data.model.words.WordResponse

interface IAddWordUseCase {
    suspend fun invoke(
        originalText: String,
        translatedText: String
    ): Result<WordResponse>
}