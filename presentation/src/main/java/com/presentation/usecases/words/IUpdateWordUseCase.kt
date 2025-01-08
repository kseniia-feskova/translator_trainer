package com.presentation.usecases.words

import com.presentation.model.WordUI

interface IUpdateWordUseCase {

    suspend fun invoke(newWord: WordUI)

}