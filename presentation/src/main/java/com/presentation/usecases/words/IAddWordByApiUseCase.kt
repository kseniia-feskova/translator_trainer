package com.presentation.usecases.words

import com.presentation.model.WordUI

interface IAddWordByApiUseCase {

    suspend fun invoke(
        originalText: String,
        translatedText: String
    ): Result<WordUI>

}