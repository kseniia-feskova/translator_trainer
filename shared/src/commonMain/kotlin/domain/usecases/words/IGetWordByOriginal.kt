package domain.usecases.words

import data.model.words.WordResponse

interface IGetWordByOriginal {
    suspend fun invoke(original: String): Result<WordResponse>
}