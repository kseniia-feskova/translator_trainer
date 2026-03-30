package domain.usecases

import data.translate.Language

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