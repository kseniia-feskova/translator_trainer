package presentation.usecases

import presentation.utils.Language

interface ITranslateWordUseCase {

    suspend fun invoke(
        text: String,
        originalLanguage: Language,
        resLanguage: Language,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    )

    fun closeResources()
}