package com.presentation.usecases.words

import presentation.model.WordUI

interface IAddWordUseCase {
    suspend fun invoke(
        originalText: String,
        translatedText: String
    ): Result<WordUI>
}