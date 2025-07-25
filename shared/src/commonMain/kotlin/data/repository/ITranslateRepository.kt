package data.repository

import data.translate.Language

interface ITranslateRepository {
    suspend fun getTranslate(
        text: String,
        originalLanguage: Language,
        resLanguage: Language
    ): String

}