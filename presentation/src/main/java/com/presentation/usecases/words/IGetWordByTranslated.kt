package com.presentation.usecases.words

import com.presentation.model.WordUI

interface IGetWordByTranslated {

    suspend fun invoke(translated: String): Result<WordUI>

}