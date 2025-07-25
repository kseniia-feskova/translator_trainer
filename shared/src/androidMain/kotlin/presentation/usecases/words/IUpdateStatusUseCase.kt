package presentation.usecases.words

import presentation.model.Level
import presentation.model.WordUI

interface IUpdateStatusUseCase {

    suspend fun invoke(wordId: String, level: Level): Result<WordUI>

}