package com.data.repository.translate

import com.data.api.TranslateService
import com.data.model.TranslationResponse
import com.data.translate.Language

class TranslateRepository(
    private val service: TranslateService,
) : ITranslateRepository {

    override suspend fun getTranslate(
        text: String,
        originalLanguage: Language,
        resLanguage: Language
    ): String {
//        val request = TranslateRequest(
//            contents = listOf(text),
//            sourceLanguageCode = originalLanguage.code,
//            targetLanguageCode = resLanguage.code
//        )
        val response = service.translate(text, "${originalLanguage.code}|${resLanguage.code}")
  //      val response = service.translateText(request)
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
