package com.presentation.usecases

import java.util.UUID

interface IAddWordToSetUseCase {
    suspend fun invoke(setId: Int, wordId: UUID)
}