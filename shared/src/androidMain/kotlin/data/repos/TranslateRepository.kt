package data.repos

import data.api.TranslateService
import data.model.translate.TranslationResponse
import data.repository.ITranslateRepository
import data.translate.Language

class TranslateRepository(
    private val service: TranslateService,
) : ITranslateRepository {

    override suspend fun getTranslate(
        text: String,
        originalLanguage: Language,
        resLanguage: Language
    ): TranslationResponse? {
        val response = service.translate(text, "${originalLanguage.code}|${resLanguage.code}")
        if (response.isSuccessful && response.body() != null) {
            println(response.body())
            return response.body()
        } else {
            println("Ошибка: ${response.code()}")
            return null
        }
    }

}
