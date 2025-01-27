package com.data.repository.translate

import com.data.api.TranslateService
import com.data.model.TranslationResponse

class TranslateRepository(
    private val service: TranslateService,
) : ITranslateRepository {

    override suspend fun getTranslate(
        text: String,
    ): String {
        val response = service.translate(text, "de|ru")
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

        val match = response.matches.firstOrNull { it.segment == "Katze" }
        return match?.translation ?: "Перевод не найден"
    }
}
