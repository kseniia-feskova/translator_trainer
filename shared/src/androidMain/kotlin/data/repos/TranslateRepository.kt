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
    ): String {
        val response = service.translate(text, "${originalLanguage.code}|${resLanguage.code}")
        if (response.isSuccessful && response.body() != null) {
            println(response.body())
            return getTranslation(response.body()!!)
        } else {
            println("Ошибка: ${response.code()}")
            return ""
        }
    }

    private fun getTranslation(response: TranslationResponse): String {
        val directTranslation = response.responseData.translatedText
        if (directTranslation.isNotEmpty()) {
            return directTranslation
        }

        val match = response.matches.maxByOrNull { it.match }
        return match?.translation ?: "Перевод не найден"
    }
}
