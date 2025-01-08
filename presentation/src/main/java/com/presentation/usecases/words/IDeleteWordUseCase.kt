package com.presentation.usecases.words

import java.util.UUID

interface IDeleteWordUseCase {

    suspend fun invoke(wordId: UUID)

}