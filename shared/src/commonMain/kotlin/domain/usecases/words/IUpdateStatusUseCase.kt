package domain.usecases.words

import data.model.words.WordResponse
import data.model.words.WordStatus

interface IUpdateStatusUseCase {

    suspend fun invoke(wordId: String, level: WordStatus): Result<WordResponse>

}