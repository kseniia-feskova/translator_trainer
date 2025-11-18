package com.presentation.usecases

import presentation.model.Translation
import presentation.utils.Language

interface ITranslateWordUseCase {

   suspend fun invoke(text: String, originalLanguage: Language, resLanguage: Language): Translation?

}