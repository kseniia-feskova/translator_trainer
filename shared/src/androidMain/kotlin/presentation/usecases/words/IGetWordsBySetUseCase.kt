package presentation.usecases.words

import presentation.model.WordUI

interface IGetWordsBySetUseCase {
    suspend fun invoke(setId: String): Result<List<WordUI>>
}