package presentation.usecases.words

import presentation.model.WordUI

interface IGetWordByTranslated {

    suspend fun invoke(translated: String): Result<WordUI>

}