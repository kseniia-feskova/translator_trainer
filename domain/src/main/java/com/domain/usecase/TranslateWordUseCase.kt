package com.domain.usecase

import com.data.repository.translate.ITranslateRepository
import com.domain.mapper.toData
import com.presentation.usecases.ITranslateWordUseCase
import com.presentation.utils.Language

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