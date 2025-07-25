package presentation.usecases.words

import presentation.model.WordUI
import java.util.UUID

interface IGetWordByIdUseCase {

    suspend fun invoke(id: UUID): WordUI?

}