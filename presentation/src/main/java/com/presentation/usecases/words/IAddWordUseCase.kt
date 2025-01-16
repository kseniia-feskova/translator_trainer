package com.presentation.usecases.words

import com.presentation.model.WordUI

interface IAddWordUseCase {
    suspend fun invoke(
        setId: Int? = null,
        originalText: String,
        translatedText: String
    ): Result<WordUI>
}