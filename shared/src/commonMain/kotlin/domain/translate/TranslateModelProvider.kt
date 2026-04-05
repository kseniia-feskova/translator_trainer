package domain.translate

import data.translate.Language

interface ITranslateModelProvider {
    fun translate(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    )

    fun downloadModel(
        sourceLanguage: Language,
        targetLanguage: Language,
        onError: (Exception) -> Unit
    )

    fun close()
}