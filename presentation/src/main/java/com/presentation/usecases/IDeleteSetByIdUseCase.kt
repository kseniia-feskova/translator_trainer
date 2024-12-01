package com.presentation.usecases

interface IDeleteSetByIdUseCase {

    suspend fun invoke(setId: Int)

}