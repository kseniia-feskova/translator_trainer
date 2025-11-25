package presentation.usecases.words

import presentation.model.WordUI

interface IGetWordByOriginal {
    suspend fun invoke(original: String): Result<WordUI>
}