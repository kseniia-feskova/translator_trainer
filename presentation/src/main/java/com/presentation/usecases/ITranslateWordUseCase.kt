package com.presentation.usecases

import com.presentation.utils.Language

interface ITranslateWordUseCase {

   suspend fun invoke(text: String, originalLanguage: Language, resLanguage: Language): String

}