package data.repository

import data.model.translate.TranslationResponse
import data.translate.Language

interface ITranslateRepository {
    suspend fun getTranslate(
        text: String,
        originalLanguage: Language,
        resLanguage: Language
    ): TranslationResponse?

}