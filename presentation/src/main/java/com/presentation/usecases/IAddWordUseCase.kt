package com.presentation.usecases

import com.presentation.model.WordUI

interface IAddWordUseCase {
    suspend fun invoke(setId: Int? = null, newWord: WordUI)
}