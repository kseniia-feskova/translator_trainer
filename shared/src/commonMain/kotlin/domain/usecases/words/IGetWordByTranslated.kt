package domain.usecases.words

import data.model.words.WordResponse

interface IGetWordByTranslated {

    suspend fun invoke(translated: String): Result<WordResponse>

}