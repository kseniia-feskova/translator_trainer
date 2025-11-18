package usecase

import com.presentation.usecases.ITranslateWordUseCase
import data.model.translate.TranslationResponse
import data.repository.ITranslateRepository
import mapper.toData
import presentation.model.Translation
import presentation.utils.Language

class TranslateWordUseCase(
    private val translateRepository: ITranslateRepository,
) : ITranslateWordUseCase {

    override suspend fun invoke(
        text: String,
        originalLanguage: Language,
        resLanguage: Language
    ): Translation? {
        val response =
            translateRepository.getTranslate(text, originalLanguage.toData(), resLanguage.toData())

        if (response == null) {
            return null
        }
        return Translation(
            resource = text,
            translating = getTranslation(response),
            altTranslate = response.matches.map { it.translation }
        )
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