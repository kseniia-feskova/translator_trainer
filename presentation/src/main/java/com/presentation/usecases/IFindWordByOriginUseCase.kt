package com.presentation.usecases

import com.presentation.model.WordUI

interface IFindWordByOriginUseCase {
    suspend fun invoke(origin: String): WordUI?
}

interface IFindWordByTranslatedUseCase {
    suspend fun invoke(translated: String): WordUI?

}