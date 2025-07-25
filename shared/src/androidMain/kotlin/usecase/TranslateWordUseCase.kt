package usecase

import com.presentation.usecases.ITranslateWordUseCase
import data.repository.ITranslateRepository
import mapper.toData
import presentation.utils.Language

class TranslateWordUseCase(
    private val translateRepository: ITranslateRepository,
) : ITranslateWordUseCase {

    override suspend fun invoke(
        text: String,
        originalLanguage: Language,
        resLanguage: Language
    ): String {
        return translateRepository.getTranslate(
            text,
            originalLanguage.toData(),
            resLanguage.toData()
        )
    }

}