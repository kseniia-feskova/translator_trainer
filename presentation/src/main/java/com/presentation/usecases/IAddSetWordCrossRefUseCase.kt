package com.presentation.usecases

import java.util.UUID

interface IAddSetWordCrossRefUseCase {
    suspend fun invoke(wordID: UUID, setID: Int)
}