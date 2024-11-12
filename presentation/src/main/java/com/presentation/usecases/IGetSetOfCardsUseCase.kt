package com.presentation.usecases

import com.presentation.model.SetOfCards

interface IGetSetOfCardsUseCase {

    suspend fun invoke(setId: Int): SetOfCards?

    suspend fun invoke(setName: String): SetOfCards?

}