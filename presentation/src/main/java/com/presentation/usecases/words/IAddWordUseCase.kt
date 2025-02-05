package com.presentation.usecases.words

import com.presentation.model.WordUI

interface IAddWordUseCase {
    suspend fun invoke(
        originalText: String,
        translatedText: String
    ): Result<WordUI>
}