package domain.usecases.words

import data.model.words.WordResponse

interface IGetWordByIdUseCase {

    suspend fun invoke(id: String): WordResponse?

}