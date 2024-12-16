package com.presentation.usecases

interface IDeleteWordUseCase {

    suspend fun invoke(wordId: Int)

}