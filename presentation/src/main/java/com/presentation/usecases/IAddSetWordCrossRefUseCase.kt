package com.presentation.usecases

interface IAddSetWordCrossRefUseCase {
    suspend fun invoke(wordID: Int, setID: Int)
}