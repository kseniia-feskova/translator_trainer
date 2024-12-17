package com.presentation.usecases

interface IAddWordToSetUseCase {
    suspend fun invoke(setId: Int, wordId: Int)
}