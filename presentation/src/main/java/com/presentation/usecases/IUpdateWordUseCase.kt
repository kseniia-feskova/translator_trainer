package com.presentation.usecases

import com.presentation.model.WordUI

interface IUpdateWordUseCase {

    suspend fun invoke(newWord: WordUI)

}