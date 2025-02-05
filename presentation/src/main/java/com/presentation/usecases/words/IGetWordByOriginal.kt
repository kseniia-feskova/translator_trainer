package com.presentation.usecases.words

import com.presentation.model.WordUI

interface IGetWordByOriginal {
    suspend fun invoke(original: String): Result<WordUI>
}