package com.presentation.usecases.words

import presentation.model.WordUI

interface IAddWordByApiUseCase {

    suspend fun invoke(
        originalText: String,
        translatedText: String
    ): Result<WordUI>

    suspend fun invokeOffline(
        originalText: String,
        translatedText: String
    ): Result<WordUI>
}