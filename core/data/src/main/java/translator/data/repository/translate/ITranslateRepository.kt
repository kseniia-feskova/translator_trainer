package translator.data.repository.translate

import translator.data.translate.Language

interface ITranslateRepository {

    suspend fun getTranslate(
        text: String,
        originalLanguage: Language,
        resLanguage: Language
    ): String

}