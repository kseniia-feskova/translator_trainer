package domain.usecases.words

import data.model.words.WordResponse
import java.util.UUID

interface IGetWordByIdUseCase {

    suspend fun invoke(id: UUID): WordResponse?

}